package com.clique.retire.schedule.temporizador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clique.retire.schedule.service.SinalizadorService;

import net.javacrumbs.shedlock.core.SchedulerLock;

@Component
public class SinalizadorTemporizador {

	private static final Logger LOGGER = LoggerFactory.getLogger(SinalizadorTemporizador.class);
	
	private static final int TWO_MIN = 2 * 60 * 1000;
		
	@Autowired
	private SinalizadorService sinalizadorService;
	
	@Scheduled(cron = "${cron.expression.verify.light}")
	@SchedulerLock(name = "verificacao_luzes_clique_retire", lockAtMostFor = TWO_MIN, lockAtLeastFor = TWO_MIN)
	private void verificarLuzesCliqueRetireComUrgencia() {
		new Thread(() -> {
			LOGGER.info("[SinalizadorTemporizador] Inicio verificarAtrasos()");

			try {
				sinalizadorService.verificarLuzesCliqueRetire(null);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			}
			
			LOGGER.info("[SinalizadorTemporizador] Fim verificarAtrasos()");
		}).start();
	}
	
}