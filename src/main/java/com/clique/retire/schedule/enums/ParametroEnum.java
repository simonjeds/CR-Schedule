package com.clique.retire.schedule.enums;

import lombok.Getter;

public enum ParametroEnum {
	
	TEMPO_SEPARACAO_ATRASADA("TEMPO_SEPARACAO_ATRASADA"),
	TEMPO_SEPARACAO_PENDENTE("TEMPO_SEPARACAO_PENDENTE"),
	COR_SINALIZADOR_SEPARACAO_PENDENTE("COR_SINALIZADOR_SEPARACAO_PENDENTE"),
	COR_SINALIZADOR_SEPARACAO_ATRASADA("COR_SINALIZADOR_SEPARACAO_ATRASADA"),
	COR_SINALIZADOR_NOVO_PEDIDO("COR_SINALIZADOR_NOVO_PEDIDO"),
	POWERSHWLL_INSTALAR("POWERSHWLL_INSTALAR"),
	POWERSHWLL_REINICIAR("POWERSHWLL_REINICIAR"),
	POWERSHWLL_LOG("POWERSHWLL_LOG"),
	FASE_APOS_SEPARACAO("FASE_APOS_SEPARACAO");

	@Getter
	private final String descricao;
	 
	ParametroEnum(String descricao) {
        this.descricao = descricao;	        
	}

}