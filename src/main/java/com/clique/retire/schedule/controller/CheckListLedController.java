package com.clique.retire.schedule.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clique.retire.schedule.dto.BaseResponseDTO;
import com.clique.retire.schedule.enums.ParametroEnum;
import com.clique.retire.schedule.repository.ParametroCliqueRetireCustom;
import com.clique.retire.schedule.service.SinalizadorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "checklist")
@RestController
@RequestMapping(value = "/rest/checklist", produces = "application/json;charset=UTF-8")
public class CheckListLedController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SinalizadorService.class);
	
	@Autowired
	private SinalizadorService sinalizadorService;
	
	@Autowired
	private ParametroCliqueRetireCustom parametroCliqueRetireCustom;
	
	@CrossOrigin
	@RequestMapping(value="/instalar-servico-maquina", method = RequestMethod.GET)
	@ApiOperation(value = "Instala ou atualiza o serviço na maquina conforme IP informado.", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
    public ResponseEntity<BaseResponseDTO> instalarServicoSinalizadorLuxaFor(
    		@ApiParam(value = "ip") @RequestParam("ip") String ip) {
		
		try {
			Runtime.getRuntime().exec(parametroCliqueRetireCustom.findByNome(ParametroEnum.POWERSHWLL_INSTALAR.getDescricao()) + ip);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return retornoErro();
		}
		
		return retornoSucesso();
    }
	
	@CrossOrigin
	@RequestMapping(value="/reiniciar-servico-maquina", method = RequestMethod.GET)
	@ApiOperation(value = "Reinicia o serviço na maquina conforme IP informado.", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
    public ResponseEntity<BaseResponseDTO> reiniciarServicoSinalizadorLuxaFor(
    		@ApiParam(value = "ip") @RequestParam("ip") String ip) {
		
		try {
			Runtime.getRuntime().exec(parametroCliqueRetireCustom.findByNome(ParametroEnum.POWERSHWLL_REINICIAR.getDescricao()) + ip);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return retornoErro();
		}
		
		return retornoSucesso();
    }
	
	@CrossOrigin
	@RequestMapping(value="/log-servico-maquina", method = RequestMethod.GET)
	@ApiOperation(value = "Reinicia o serviço na maquina conforme IP informado.", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
    public ResponseEntity<BaseResponseDTO> logServicoSinalizadorLuxaFor(
    		@ApiParam(value = "ip") @RequestParam("ip") String ip) {
		
		try {
			Runtime.getRuntime().exec(parametroCliqueRetireCustom.findByNome(ParametroEnum.POWERSHWLL_LOG.getDescricao()) + ip);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return retornoErro();
		}
		
		return retornoSucesso();
    }
	
	@CrossOrigin
	@RequestMapping(value = "/atualizar-cor-sinalizador", method = RequestMethod.GET)
	@ApiOperation(value = "Atualiza a cor de um sinalizador pela filial.", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	public ResponseEntity<BaseResponseDTO> atualizarCorSinalizador(
			@ApiParam(value = "filial") @RequestParam("filial") Integer filial,
			@ApiParam(value = "corHexadecimal") @RequestParam("corHexadecimal") String corHexadecimal) {
		
		System.out.println("[TEST-CONTROLLER] mudança de cor.");
		
		corHexadecimal = "#".concat(corHexadecimal).replace("##", "#");

		sinalizadorService.atualizarCorPorFilial(filial, corHexadecimal);
		
		return retornoSucesso();
	}
	
	private ResponseEntity<BaseResponseDTO> retornoSucesso(){
		BaseResponseDTO response = new BaseResponseDTO();
		response.setCode(HttpStatus.OK.value());
		response.setData("ok");
		response.setMessage("success");
		return ResponseEntity.ok(response);
	}
	
	private ResponseEntity<BaseResponseDTO> retornoErro(){
		BaseResponseDTO response = new BaseResponseDTO();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setData("Erro");
		response.setMessage("fail");
		return ResponseEntity.ok(response);
	}
	
}