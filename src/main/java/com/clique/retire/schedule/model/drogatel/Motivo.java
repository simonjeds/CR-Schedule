package com.clique.retire.schedule.model.drogatel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MOTIVO_DROGATEL")
public class Motivo {

	@Id
	@Column(name = "MTDR_CD_MOTIVO_DROGATEL")
	private Long id;

	@Column(name = "MTDR_DS_DESCRICAO")
	private String descricao;

	public Motivo(Long id) {
		this.id = id;
	}
}
