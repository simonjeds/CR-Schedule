package com.clique.retire.schedule.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ParametroCliqueRetireRepositoryImpl implements ParametroCliqueRetireCustom {

	@PersistenceContext
	private EntityManager em;

	public String findByNome(String nome) {
		try {
			return (String) em.createNativeQuery("SELECT p.pcrval FROM drgtblpcrparametro p WHERE p.pcrnom = :nome")
				.setParameter("nome", nome)
				.setMaxResults(1)
				.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}
	
}
