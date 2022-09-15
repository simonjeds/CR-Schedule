package com.clique.retire.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PedidoDTO {

  private Integer numeroPedido;
  private Integer codigoFilial;
  private String ip;

}
