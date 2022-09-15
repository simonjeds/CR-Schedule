package com.clique.retire.schedule.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ParametroCliqueRetireCustom {

	/**
	 * Método para consulta na tabela DRGTBLPCRPARAMETRO pelo nome do parametro .
	 * 
	 * @param nome do parametro
	 * @return ParametroCliqueRetire
	 */
	String findByNome(String nome);

}
