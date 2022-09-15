package com.clique.retire.schedule.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.clique.retire.schedule.model.drogatel.PendenciaPedido;
import com.clique.retire.schedule.util.Constantes;
import org.springframework.stereotype.Repository;

import com.clique.retire.schedule.enums.FasePedidoEnum;
import com.clique.retire.schedule.enums.SimNaoEnum;

import static com.clique.retire.schedule.util.Constantes.*;

@Repository
public class PendenciaPedidoRepositoryImpl implements PendenciaPedidoRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @SuppressWarnings("unchecked")
  public List<Integer> obterPedidosComControladoAptosParaOCancelamento() {
    StringBuilder sql   = new StringBuilder()
      .append(" SELECT p.pedi_nr_pedido AS numero_pedido ")
      .append(" FROM PEDIDO p (NOLOCK) ")
      .append(" INNER JOIN CLIENTE c (NOLOCK) ON p.CLNT_CD_CLIENTE = c.CLNT_CD_CLIENTE ")
      .append(" INNER JOIN SEPARACAO_PEDIDO sp (NOLOCK) ON sp.PEDI_NR_PEDIDO = p.PEDI_NR_PEDIDO ")
      .append("    AND sp.SPPD_CD_SEPARACAO_PEDIDO = ( ")
      .append("      SELECT MIN(sp.SPPD_CD_SEPARACAO_PEDIDO) FROM SEPARACAO_PEDIDO sp (NOLOCK) ")
      .append("      WHERE sp.PEDI_NR_PEDIDO = p.PEDI_NR_PEDIDO ")
      .append("    ) ")
      .append(" WHERE PEDI_FL_FASE = :fase ")
      .append("    AND PEDI_FL_OPERACAO_LOJA = :loja ")
      .append("    AND NOT EXISTS ( ")
      .append("      SELECT 1 FROM receita_produto_controlado rpc (NOLOCK) ")
      .append("      WHERE rpc.itpd_cd_item_pedido IN ( ")
      .append("        SELECT itpd_cd_item_pedido FROM item_pedido ip (NOLOCK)  WHERE ip.pedi_nr_pedido = p.pedi_nr_pedido ")
      .append("      ) ")
      .append("    ) ")
      .append("    AND DATEADD(HOUR, ")
      .append("      ( SELECT cast(DRIN_TX_VALOR as NUMERIC) ")
      .append("        FROM DROGATEL_INI (NOLOCK) ")
      .append("        WHERE DRIN_TX_ENTRADA = 'CLIQUE_RETIRE_PERIODO_VALIDADE_PEDIDO_CONTROLADO'), sp.SPPD_DH_HORARIO_INICIO ")
      .append("      ) < CURRENT_TIMESTAMP ")
      .append("    AND NOT EXISTS ( ")
      .append("      SELECT 1 FROM lksql01.cosmos.dbo.NOTA_FISCAL nf (nolock) ")
      .append("      WHERE nf.PEDI_NR_PEDIDO = p.PEDI_NR_PEDIDO AND nf.NOFI_TP_NF = :tipoNF AND nf.NOFI_CD_SITUACAO = :statusNF ")
      .append("    )")
      .append("    AND ( ")
      .append("      NOT EXISTS ( ")
      .append("        SELECT 1 FROM PENDENCIA_PEDIDO_DROGATEL ppd (nolock) ")
      .append("        JOIN motivo_drogatel md (NOLOCK) ON md.mtdr_cd_motivo_drogatel = ppd.mtdr_cd_motivo_drogatel ")
      .append("          AND md.mtdr_ds_descricao = :motivo ")
      .append("        WHERE ppd.PEDI_NR_PEDIDO = p.PEDI_NR_PEDIDO AND ppd.PEPD_DS_PENDENCIA = :descricao ")
      .append("      ) OR ( ")
      .append("        DATEADD( ")
      .append("          HOUR, ")
      .append("          ( ")
      .append("             SELECT CAST(drin_tx_valor AS NUMERIC) + 24 ")
      .append("             FROM drogatel_ini (NOLOCK) ")
      .append("             WHERE drin_tx_entrada = 'CLIQUE_RETIRE_PERIODO_VALIDADE_PEDIDO_CONTROLADO' ")
      .append("          ),")
      .append("          sp.sppd_dh_horario_inicio ")
      .append("         ) < CURRENT_TIMESTAMP ")
      .append("         AND NOT EXISTS ( ")
      .append("           SELECT 1 FROM PENDENCIA_PEDIDO_DROGATEL ppd (nolock) ")
      .append("           JOIN motivo_drogatel md (NOLOCK) ON md.mtdr_cd_motivo_drogatel = ppd.mtdr_cd_motivo_drogatel ")
      .append("             AND md.mtdr_ds_descricao = :motivo ")
      .append("           WHERE ppd.PEDI_NR_PEDIDO = p.PEDI_NR_PEDIDO ")
      .append("             AND ppd.PEPD_DS_PENDENCIA = :descricao AND ppd.PEPD_FL_PENDENCIA_RESOLVIDA = 'N' ")
      .append("         ) ")
      .append("      ) ")
      .append("   )");

    return (List<Integer>) em.createNativeQuery(sql.toString())
      .setParameter("fase", FasePedidoEnum.AGUARDANDO_RECEITA.getChave())
      .setParameter("loja", SimNaoEnum.S.getDescricao())
      .setParameter("tipoNF", NF_TIPO_VENDA_SAC)
      .setParameter("statusNF", NF_SITUACAO_CONCLUIDA)
      .setParameter("motivo", PENDENCIA_PEDIDO_MOTIVO)
      .setParameter("descricao", PENDENCIA_DESCRICAO)
      .getResultList();
  }

  @Override
  public List<PendenciaPedido> buscarPedidosNaoEntreguesAptosParaRetirada() {
    StringBuilder sql = new StringBuilder()
      .append("FROM PendenciaPedido ppd ")
      .append("WHERE ppd.resolvido = :resolvido ")
      .append("  AND ppd.motivo.descricao = :fila ")
      .append("  AND ppd.pedido.fasePedido = :fase ");

    return this.em.createQuery(sql.toString(), PendenciaPedido.class)
      .setParameter("resolvido", SimNaoEnum.N)
      .setParameter("fila", FILA_ENTREGA_REAGENDADA)
      .setParameter("fase", FasePedidoEnum.NAO_ENTREGUE)
      .getResultList();
  }

}