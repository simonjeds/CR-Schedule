package com.clique.retire.schedule.client;

import java.util.List;

import com.clique.retire.schedule.dto.BaseResponseDTO;
import com.clique.retire.schedule.dto.SinalizadorOldDTO;

import feign.Headers;
import feign.RequestLine;

@Headers("api_key: psw#frameworkSwagger2017;")
public interface BackofficeClient {
	
	@RequestLine("POST sinalizador/carga")
	@Headers("Content-Type: application/json")
	public BaseResponseDTO carregarStatusSinalizadores(List<SinalizadorOldDTO> sinalizadores);
}