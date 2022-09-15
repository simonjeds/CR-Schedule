package com.clique.retire.schedule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clique.retire.schedule.service.SinalizadorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "pedido")
@RestController
@RequestMapping(value = "/rest/pedido", produces = "application/json;charset=UTF-8")
public class PedidoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);
	
	@Autowired
	private SinalizadorService sinalizadorService;

	@RequestMapping(value="/reportar-novo-pedido", method = RequestMethod.GET)
	@ApiOperation(value = "Reporta um novo pedido para o schedule de clique e retire, atualizando o sinalizador de loja", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
    public String reportarNovoPedido(
    		@ApiParam(value = "codigoFilial") @RequestParam("codigoFilial") Integer codigoFilial) {
		
		LOGGER.info("[WS_NOVO_PEDIDO] Chamou");
		try {
			sinalizadorService.verificarLuzesCliqueRetire(codigoFilial);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		LOGGER.info("[WS_NOVO_PEDIDO] Finalizou");
		
		return "ok";
    }
	
}
