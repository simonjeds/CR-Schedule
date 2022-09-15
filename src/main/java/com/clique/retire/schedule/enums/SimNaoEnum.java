package com.clique.retire.schedule.enums;

import lombok.Getter;

public enum SimNaoEnum {
	
	S("S"),
	N("N");

	@Getter
	private String descricao;
	 
	private SimNaoEnum(String descricao) {		    
        this.descricao = descricao;	        
	}
	
}