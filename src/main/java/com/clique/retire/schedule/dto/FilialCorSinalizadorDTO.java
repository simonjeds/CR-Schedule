package com.clique.retire.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilialCorSinalizadorDTO {

  private Integer filial;
  private String cor;

}
