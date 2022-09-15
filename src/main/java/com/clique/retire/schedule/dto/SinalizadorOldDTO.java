package com.clique.retire.schedule.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SinalizadorOldDTO {

	private Integer id;
	private boolean ligado;

	public SinalizadorOldDTO(Integer id, boolean ligado) {
		this.id = id;
		this.ligado = ligado;
	}
}