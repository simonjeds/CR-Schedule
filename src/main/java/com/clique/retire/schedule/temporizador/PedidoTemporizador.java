package com.clique.retire.schedule.temporizador;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clique.retire.schedule.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoTemporizador {

  private final PedidoService pedidoService;

  @Scheduled(cron = "${cron.expression.verify.controlado.etiquetas}")
  public void imprimirEtiquetasDeControladosPendentes() {
    new Thread(pedidoService::imprimirEtiquetasDeControladosPendentes).start();
  }
    
  @Scheduled(cron = "${cron.expression.verify.controlado}")
  private void confirmarEntregaDePedidosDeControlados() {
    new Thread(pedidoService::confirmarEntregaDePedidosDeControlados).start();
  }
  
  @Scheduled(cron = "${cron.expression.create.expedicao-pedido}")
  private void criarExpedicaoPedidoServico() {
	  new Thread(pedidoService::criarExpedicaoPedidoServico).start();  
  }
  
  @Scheduled(cron = "${cron.expression.end.expedicao-pedido}")
  private void definirExpedicaoPedidoServicoComoNaoEntregue() {
	  new Thread(pedidoService::definirExpedicaoPedidoServicoComoNaoEntregue).start();  
  }

}
