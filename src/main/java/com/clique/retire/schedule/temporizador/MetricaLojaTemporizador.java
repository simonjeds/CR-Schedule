package com.clique.retire.schedule.temporizador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clique.retire.schedule.util.FeignUtil;

import net.javacrumbs.shedlock.core.SchedulerLock;

@Component
public class MetricaLojaTemporizador {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricaLojaTemporizador.class);
	private static final int ONE_MIN = 1 * 60 * 1000;
	
	@Autowired
	private Environment env;

	@Scheduled(cron = "${cron.expression.send.metrics}")
	@SchedulerLock(name = "geracao_metricas_pedidos_clique_retire", lockAtMostFor = ONE_MIN, lockAtLeastFor = ONE_MIN)
	private void gerarMetricasLoja() {
		new Thread(() -> {
			LOGGER.info("[MetricaLojaTemporizador] Inicio gerarMetricasLoja()");

			String url = env.getProperty("url.base.painel.clique.retire");

			FeignUtil.getPainelCliqueRetireClient(url).atualizarMetricas();

			LOGGER.info("[MetricaLojaTemporizador - gerarMetricasLoja] Fim gerarMetricasLoja()");
		}).start();
	}

	@Scheduled(cron = "${cron.expression.find.metrics}")
	@SchedulerLock(name = "calculo_metricas_pedidos_clique_retire", lockAtMostFor = ONE_MIN, lockAtLeastFor = ONE_MIN)
	private void calcularMetricasLoja() {
		new Thread(() -> {
			try {
				LOGGER.info("[MetricaLojaTemporizador] Inicio gerarMetricasLoja()");
	
				String url = env.getProperty("url.base.painel.clique.retire");
				
				FeignUtil.getPainelCliqueRetireClient(url).calcularMetricas();
			
				LOGGER.info("[MetricaLojaTemporizador - gerarMetricasLoja] Fim gerarMetricasLoja()");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}).start();
	}
}