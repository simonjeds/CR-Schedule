package com.clique.retire.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO {

	private Integer code;
	private String status;
	private String message;
	private Object data;
	
}
