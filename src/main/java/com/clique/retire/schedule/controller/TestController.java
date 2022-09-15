package com.clique.retire.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clique.retire.schedule.service.SinalizadorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "test")
@RestController
@RequestMapping(value = "/rest/test", produces = "application/json;charset=UTF-8")
public class TestController {

	@Autowired
	private SinalizadorService sinalizadorService;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@GetMapping("/atualizar-filiais")
	@ApiOperation("Atualiza a cor de um sinalizador pela filial.")
	public String atualizarCorSinalizador(Integer filial) {
		System.out.println("[TEST-CONTROLLER] mudança de cor.");
		sinalizadorService.verificarLuzesCliqueRetire(filial);
		return "ok";
	}
	
	@RequestMapping(value = "/mandar-mensagem", method = RequestMethod.GET)
	@ApiOperation(value = "Atualiza a cor de um sinalizador pela filial.", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	public String mandarMensagem() {
		
		System.out.println("[TEST-CONTROLLER] envia mensagem.");
		
		messagingTemplate.convertAndSend("/filial/133", "Teste");
		
		return "ok";
	}
}
