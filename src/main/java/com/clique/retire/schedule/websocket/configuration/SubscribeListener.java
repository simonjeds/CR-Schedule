package com.clique.retire.schedule.websocket.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeListener.class);
	
	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		try {
//			LOGGER.info("[WEBSOCKET_Sinalizador] Nova inscrição");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}