package com.clique.retire.schedule.repository;

import static com.clique.retire.schedule.util.Constantes.NUMERO_PEDIDO;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.clique.retire.schedule.config.ConfiguracaoLED;
import com.clique.retire.schedule.dto.FilialCorSinalizadorDTO;
import com.clique.retire.schedule.dto.PedidoDTO;
import com.clique.retire.schedule.enums.FasePedidoEnum;
import com.clique.retire.schedule.model.drogatel.Pedido;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PedidoRepositoryImpl implements PedidoRepositoryCustom {

  @PersistenceContext(unitName = "drogatelEntityManager")
  private EntityManager em;
  private final ConfiguracaoLED configuracaoLED;

  @SuppressWarnings("unchecked")
  @Override
  public List<FilialCorSinalizadorDTO> buscarFiliaisEInformacoesDeAtraso(Integer filial) {
    StringBuilder sql = new StringBuilder()
      .append("SELECT ")
      .append("	 polo.polo_cd_polo AS filial, ")
      .append("	 ISNULL(MIN(pedidos.cor), 4) AS codigo_cor ")
      .append("FROM polo polo (NOLOCK)  ")
      .append("LEFT JOIN ( ")
      .append("	 SELECT ")
      .append("		 p.polo_cd_polo, ")
      .append("		 CASE ")
      .append("			 WHEN ( ")
      .append(" 			 p.pedi_fl_fase = '03' ")
      .append("				 AND DATEDIFF(MINUTE, p.pedi_dh_termino_atendimento, CURRENT_TIMESTAMP) < :tempoPendente ")
      .append("			 ) THEN 1 ")
      .append("			 WHEN ( ")
      .append("				 DATEDIFF(MINUTE, p.pedi_dh_termino_atendimento, CURRENT_TIMESTAMP) > :tempoAtrasado ")
      .append("			 ) THEN 2 ")
      .append("			 WHEN ( ")
      .append("			 	 DATEDIFF(MINUTE, p.pedi_dh_termino_atendimento, CURRENT_TIMESTAMP) > :tempoPendente ")
      .append("				 AND DATEDIFF(MINUTE, p.pedi_dh_termino_atendimento, CURRENT_TIMESTAMP) < :tempoAtrasado ")
      .append(" 		 ) THEN 3 ")
      .append("			 ELSE 4 ")
      .append("		 END AS cor ")
      .append("	 FROM pedido p (NOLOCK) ")
      .append("  LEFT JOIN drgtblpdcpedidocompl compl (NOLOCK) ON compl.pdcnrpedido = p.pedi_nr_pedido ")
      .append("	 WHERE p.pedi_fl_fase IN ('03', :faseAposSeparacao) ")
      .append("    AND p.pedi_fl_operacao_loja = 'S' ")
      .append("	   AND COALESCE(p.pedi_fl_formula, 'N') = 'N' ")
      .append("    AND COALESCE(compl.pdcidcpapafila, 'N') = 'N'  ")
      .append("    AND NOT EXISTS ( ")
      .append("		   SELECT 1 FROM drgtblhfphistfasepedido_hst h (NOLOCK) ")
      .append("		   WHERE h.pedi_nr_pedido = p.pedi_nr_pedido AND h.pedi_fl_fase_atual = '29'")
      .append("	   )	")
      .append(") AS pedidos ON polo.polo_cd_polo = pedidos.polo_cd_polo ");

    if (filial != null) {
      sql.append(" WHERE polo.polo_cd_polo = ").append(filial);
    }

    sql.append(" GROUP BY polo.polo_cd_polo");

    List<Tuple> results = (List<Tuple>) em.createNativeQuery(sql.toString(), Tuple.class)
      .setParameter("tempoPendente", configuracaoLED.getTempoSeparacaoPendente())
      .setParameter("tempoAtrasado", configuracaoLED.getTempoSeparacaoAtrasada())
      .setParameter("faseAposSeparacao", configuracaoLED.getFaseAposSeparacao())
      .getResultList();

    return results.stream()
      .map(tuple -> FilialCorSinalizadorDTO.builder()
        .filial(tuple.get("filial", Integer.class))
        .cor(configuracaoLED.obterCorPeloCodigoDaPrioridade(tuple.get("codigo_cor", Integer.class)))
        .build()
      )
      .collect(Collectors.toList());
  }

  @Override
  @SuppressWarnings("unchecked")
  @Transactional("drogatelTransactionManager")
  public List<Integer> buscarPedidosDeControladoAptosParaFaseEntregue() {
    StringBuilder sql = new StringBuilder()
      .append(" SELECT DISTINCT p.pedi_nr_pedido FROM pedido p (NOLOCK) ")
      .append(" INNER JOIN ITEM_PEDIDO ip (nolock) on ip.PEDI_NR_PEDIDO = p.PEDI_NR_PEDIDO and ip.ITPD_FL_PRODUTO_CONTROLADO = 'S' ")
      .append(" WHERE p.pedi_fl_fase = '19' AND p.PEDI_FL_OPERACAO_LOJA = 'S' AND PEDI_FL_SUPERVENDEDOR <> 'S' AND TPFR_CD_TIPO_FRETE <> 1 ")
      .append("   AND COALESCE(p.pedi_fl_formula, 'N') = 'N' ")
      .append("   AND EXISTS ( ")
      .append("     SELECT 1 FROM receita_produto_controlado rpc (NOLOCK) ")
      .append("     WHERE rpc.itpd_cd_item_pedido in (ip.itpd_cd_item_pedido) ")
      .append("   )");

    return em.createNativeQuery(sql.toString()).getResultList();
  }

  @Override
  public void salvarHistoricoPedido(Pedido pedido, FasePedidoEnum novaFase) {
    StringBuilder sqlInsertHistoricoPedido = new StringBuilder()
      .append("INSERT INTO drgtblhfphistfasepedido_hst VALUES (CURRENT_TIMESTAMP, 1, 1, CURRENT_TIMESTAMP, ")
      .append("'JobC&R', 'JobCliqueRetireSchedule', :numeroPedido, ")
      .append("(SELECT polo_cd_polo FROM pedido WHERE pedi_nr_pedido = :numeroPedido), :novaFase, CURRENT_TIMESTAMP)");

    em.createNativeQuery(sqlInsertHistoricoPedido.toString())
      .setParameter("numeroPedido", pedido.getNumeroPedido())
      .setParameter("novaFase", novaFase.getChave())
      .executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PedidoDTO> buscarPedidosParaImpressaoDeEtiquetas() {
    StringBuilder sql = new StringBuilder()
      .append("SELECT DISTINCT p.pedi_nr_pedido AS numero_pedido, p.polo_cd_polo AS codigo_filial ")
      .append("FROM pedido p (NOLOCK) ")
      .append("WHERE p.pedi_fl_fase IN (:aguardandoExpedicao, :expedido, :entregue) ")
      .append("  AND p.pedi_fl_operacao_loja = 'S' ")
      .append("  AND EXISTS ( ")
      .append("    SELECT 1 FROM receita_produto_controlado rpc (NOLOCK) ")
      .append("    JOIN item_pedido ip (NOLOCK) ON ip.itpd_cd_item_pedido = rpc.itpd_cd_item_pedido ")
      .append("    WHERE ip.pedi_nr_pedido = p.pedi_nr_pedido AND rpc.rcpc_fl_etiquetaemitida = 'N' ")
      .append("  ) ");

    List<Tuple> result = this.em.createNativeQuery(sql.toString(), Tuple.class)
      .setParameter("aguardandoExpedicao", FasePedidoEnum.AGUARDANDO_EXPEDICAO.getChave())
      .setParameter("expedido", FasePedidoEnum.EXPEDIDO.getChave())
      .setParameter("entregue", FasePedidoEnum.ENTREGUE.getChave())
      .getResultList();

    return result.stream()
      .map(
        tupla -> PedidoDTO.builder()
          .numeroPedido(tupla.get(NUMERO_PEDIDO, Integer.class))
          .codigoFilial(tupla.get("codigo_filial", Integer.class))
          .build()
      ).collect(Collectors.toList());
  }

  @Override
  public void atualizarFlagEmissaoEtiquetaNasReceitas(List<Integer> pedidos) {
    StringBuilder sql = new StringBuilder()
      .append("UPDATE rpc SET rpc.rcpc_fl_etiquetaemitida = 'S' ")
      .append("FROM receita_produto_controlado rpc ")
      .append("JOIN item_pedido ip ON ip.itpd_cd_item_pedido = rpc.itpd_cd_item_pedido ")
      .append("WHERE ip.pedi_nr_pedido IN :pedidos");

    this.em.createNativeQuery(sql.toString())
      .setParameter("pedidos", pedidos)
      .executeUpdate();
  }
  
  @SuppressWarnings("unchecked")  
  public List<PedidoDTO> buscarPedidosDeServicoParaExpedicao() {
    StringBuilder sql = new StringBuilder()
      .append("SELECT p.pese_nr_servico as numero_pedido, p.polo_cd_polo as codigo_filial ")
      .append("FROM pedido p (nolock) ")
      .append("INNER JOIN pedido_servico ps (nolock) ON p.pese_nr_servico = ps.pese_nr_servico ")
      .append("INNER JOIN drgtblprgpedrotagerada rg (nolock) ON ps.pese_nr_servico = rg.pese_nr_servico ")
      .append("INNER JOIN drgtblirfintegrarotaforn irf (nolock) ON irf.rogcod = rg.rogcod AND irf.irfnumdocmotociclista IS NOT NULL AND irf.irfdthcancelamento IS NULL ")
      .append("WHERE p.pedi_fl_fase = :aguardandoReceita AND ps.pedi_fl_fase = :aguardandoExpedicao AND pedi_fl_operacao_loja = 'S' AND p.pese_nr_servico IS NOT NULL ");

    List<Tuple> result = this.em.createNativeQuery(sql.toString(), Tuple.class)
      .setParameter("aguardandoReceita", FasePedidoEnum.AGUARDANDO_RECEITA.getChave())
      .setParameter("aguardandoExpedicao", FasePedidoEnum.AGUARDANDO_EXPEDICAO.getChave())
      .getResultList();

    return result.stream()
      .map(tupla -> PedidoDTO.builder()
    		  				 .numeroPedido(tupla.get("numero_pedido", Integer.class))
    		  				 .codigoFilial(tupla.get("codigo_filial", Integer.class)).build())
      .collect(Collectors.toList());
  } 
  
  @SuppressWarnings("unchecked")
  public List<PedidoDTO> buscarPedidosDeServicoComOcorrencia() {
    StringBuilder sql = new StringBuilder()
      .append("SELECT ps.pedi_nr_pedido as numero_pedido ")
      .append("FROM pedido p (nolock) ")
      .append("INNER JOIN pedido_servico ps (nolock) ON p.pese_nr_servico = ps.pese_nr_servico ")
      .append("INNER JOIN drgtblprgpedrotagerada rg (nolock) ON ps.pese_nr_servico = rg.pese_nr_servico ")
      .append("INNER JOIN drgtblirfintegrarotaforn irf (nolock) ON irf.rogcod = rg.rogcod AND irf.irfnumdocmotociclista IS NOT NULL ")
      .append("INNER JOIN drgtblrogrotagerada r (nolock) ON rg.rogcod = r.rogcod ")
      .append("INNER JOIN EXPEDICAO_PEDIDO ep (nolock) ON r.EXPE_CD_EXPEDICAO = ep.EXPE_CD_EXPEDICAO AND ep.EXPD_FL_RETORNO = 'N' AND expd_ds_nao_entrega IS NOT NULL AND mtdr_cd_motivo_nao_entrega IS NOT NULL ")
      .append("WHERE p.pedi_fl_fase = :aguardandoReceita AND ps.pedi_fl_fase = :expedido AND pedi_fl_operacao_loja = 'S' AND p.pese_nr_servico IS NOT NULL ");

    List<Tuple> result = this.em.createNativeQuery(sql.toString(), Tuple.class)
      .setParameter("aguardandoReceita", FasePedidoEnum.AGUARDANDO_RECEITA.getChave())
      .setParameter("expedido", FasePedidoEnum.EXPEDIDO.getChave())
      .getResultList();

    return result.stream()
	  .map(tupla -> PedidoDTO.builder().numeroPedido(tupla.get("numero_pedido", Integer.class)).build())
	  .collect(Collectors.toList());
  }
  
  public void atualizarFasePedidoServico(Integer numeroPedido, String fase) {
	  em.createNativeQuery("UPDATE pedido_servico SET pedi_fl_fase = :fase WHERE pedi_nr_pedido = :numeroPedido")
        .setParameter("numeroPedido", numeroPedido)
        .setParameter("fase", fase)
        .executeUpdate();
  }

}
