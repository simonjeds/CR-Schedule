package com.clique.retire.schedule.service;

import com.clique.retire.schedule.enums.SimNaoEnum;
import com.clique.retire.schedule.model.drogatel.PendenciaPedido;
import com.clique.retire.schedule.repository.PendenciaPedidoRepository;
import com.clique.retire.schedule.util.Constantes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clique.retire.schedule.enums.FasePedidoEnum.AGUARDANDO_EXPEDICAO;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetornoMotociclistaService {

  private final PedidoService pedidoService;
  private final PendenciaPedidoRepository pendenciaPedidoRepository;

  @Transactional
  public void alterarFasePedidosNaoEntregues() {
    List<PendenciaPedido> pendencias = this.pendenciaPedidoRepository.buscarPedidosNaoEntreguesAptosParaRetirada();
    if (pendencias.isEmpty()) return;

    String idPedidos = pendencias.stream()
      .map(pendencia -> pendencia.getPedido().getNumeroPedido().toString())
      .collect(Collectors.joining(", "));

    log.info("Realizando troca de fases dos seguintes pedidos: [{}]", idPedidos);

    pendencias.forEach(pendencia -> {
      boolean isFaseAlterada = pedidoService.registrarNovaFasePedido(pendencia.getPedido(), AGUARDANDO_EXPEDICAO);

      if (isFaseAlterada) {
        pendencia.setResolvido(SimNaoEnum.S);
        pendencia.setUltimaAlteracao(new Date());
        pendencia.setDataResolucao(new Date());
        pendencia.setIdUsuarioResolucao(Constantes.USUARIO_ADMINISTRADOR);
        pendencia.setDescricaoSolucao(Constantes.DESCRICAO_RESOLUCAO_PENDENCIA);
      }
    });

    this.pendenciaPedidoRepository.saveAll(pendencias);
  }

}
