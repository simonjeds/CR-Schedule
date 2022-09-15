package com.clique.retire.schedule.model.drogatel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="USUARIO")
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="USUA_CD_USUARIO")
	private Integer id;
	
	@Column(name="USUA_TX_MATRICULA")
	private String matricula;
	
	@Column(name="USUA_NM_USUARIO")
	private String nome;
	
	@Column(name="USUA_FL_HAB_DESAB")
	private String habilitado;
	
	public Usuario(Integer id) {
		this.id = id;
	}
}
