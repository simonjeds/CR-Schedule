package com.clique.retire.schedule.model.drogatel;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.clique.retire.schedule.enums.FasePedidoEnum;
import com.clique.retire.schedule.enums.converter.FasePedidoEnumConverter;
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
@Table(name = "PEDIDO")
public class Pedido extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PEDI_NR_PEDIDO")
	private Integer numeroPedido;

	@Convert(converter = FasePedidoEnumConverter.class)
	@Column(name = "pedi_fl_fase")
	private FasePedidoEnum fasePedido;

	public Pedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

}
