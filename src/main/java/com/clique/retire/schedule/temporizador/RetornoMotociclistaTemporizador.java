package com.clique.retire.schedule.temporizador;

import com.clique.retire.schedule.service.RetornoMotociclistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetornoMotociclistaTemporizador {

  private final RetornoMotociclistaService service;

  @Scheduled(cron = "${cron.expression.fases.pedido-nao-entregue}")
  public void alterarFasePedidosNaoEntregues() {
    new Thread(service::alterarFasePedidosNaoEntregues).start();
  }

}
