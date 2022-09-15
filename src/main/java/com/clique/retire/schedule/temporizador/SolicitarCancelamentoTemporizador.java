package com.clique.retire.schedule.temporizador;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clique.retire.schedule.service.PendenciaPedidoService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class SolicitarCancelamentoTemporizador {

	private PendenciaPedidoService service;
	
	@Scheduled(cron = "${cron.expression.verify.controlado.acancelar}")
	private void solicitarCancelamentoPedidoViaSAC() {
		new Thread(() -> {

			try {
				service.solicitarCancelamentoPedidoViaSAC();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
		}).start();
	}
	
}