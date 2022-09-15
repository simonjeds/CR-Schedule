package com.clique.retire.schedule.repository;

import com.clique.retire.schedule.config.ConfiguracaoLED;
import com.clique.retire.schedule.dto.FilialCorSinalizadorDTO;
import com.clique.retire.schedule.enums.FasePedidoEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class CancelamentoPedidoRepositoryImpl implements CancelamentoPedidoRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  @SuppressWarnings("unchecked")
  public List<FilialCorSinalizadorDTO> buscarPedidosCanceladosPorFiliaisAptosParaReposicao(List<Integer> filiais) {
    StringBuilder sql = new StringBuilder()
      .append("SELECT p.polo_cd_polo AS filial ")
      .append("FROM pedido p (NOLOCK) ")
      .append("WHERE p.polo_cd_polo IN :codFilial ")
      .append("  AND p.pedi_fl_operacao_loja = 'S' ")
      .append("  AND p.pedi_dh_termino_atendimento > '20211111' ")
      .append("  AND p.pedi_fl_fase = :cancelado ")
      .append("  AND EXISTS ( ")
      .append("    SELECT 1 FROM drgtblhfphistfasepedido_hst hist (NOLOCK) ")
      .append("    WHERE hist.pedi_nr_pedido = p.pedi_nr_pedido AND hist.pedi_fl_fase_atual = '29' ")
      .append("  ) ")
      .append("  AND NOT EXISTS ( ")
      .append("    SELECT 1 FROM drgtblcpccancpedcontrol (NOLOCK) cpc WHERE cpc.pedi_nr_pedido = p.pedi_nr_pedido")
      .append("  ) ")
      .append("GROUP BY p.polo_cd_polo ");

    List<Tuple> pedidos = (List<Tuple>) em.createNativeQuery(sql.toString(), Tuple.class)
      .setParameter("codFilial", filiais)
      .setParameter("cancelado", FasePedidoEnum.CANCELADO.getChave())
      .getResultList();

    // Todas as filiais até agora terão cor preta
    final Map<Integer, String> coresPorFilial = filiais.stream()
      .collect(Collectors.toMap(Function.identity(), x -> ConfiguracaoLED.COR_PRETA));

    // Filiais que retornaram na consulta terão cor vermelha
    pedidos.forEach(tupla -> coresPorFilial.put(tupla.get("filial", Integer.class), ConfiguracaoLED.COR_VERMELHA));

    return coresPorFilial.entrySet().stream()
      .map(entrySet -> FilialCorSinalizadorDTO.builder()
        .filial(entrySet.getKey())
        .cor(entrySet.getValue())
        .build()
      )
      .collect(Collectors.toList());
  }

}
