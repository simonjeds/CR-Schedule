package com.clique.retire.schedule.websocket.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		LOGGER.info("Websocket configurado.");
		
		config.enableSimpleBroker("/filial");
		config.setApplicationDestinationPrefixes("/clique-retire-schedule");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		LOGGER.info("Websocket endpoint configurado.");
		
		registry.addEndpoint("/sinalizador");
	}
}