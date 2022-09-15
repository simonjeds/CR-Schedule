package com.clique.retire.schedule.enums;

import lombok.Getter;

public enum TipoPendenciaPedidoEnum {
	
	A("CARTAO_DE_CREDITO"),
	B("DEPOSITO_BANCARIO"),
	C("CONTATO_CLIENTE"),
	D("DIVERSAS"),
	N("NEGOCIACAO_PEDIDO");

	@Getter
	private String descricao;
	 
	private TipoPendenciaPedidoEnum(String descricao) {		    
        this.descricao = descricao;	        
	}
	
}