package com.clique.retire.schedule.model.drogatel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.clique.retire.schedule.enums.SimNaoEnum;
import com.clique.retire.schedule.enums.TipoPendenciaPedidoEnum;
import com.clique.retire.schedule.util.Constantes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PENDENCIA_PEDIDO_DROGATEL")
public class PendenciaPedido extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PEPD_CD_PENDENCIA_PEDI_DROGATEL")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "PEDI_NR_PEDIDO")
	private Pedido pedido;
	
	@OneToOne
	@JoinColumn(name = "MTDR_CD_MOTIVO_DROGATEL")
	private Motivo motivo;
	
	@Column(name = "PEPD_DS_PENDENCIA")
	private String descricaoPendencia;
	
	@Column(name = "PEPD_FL_PENDENCIA_RESOLVIDA")
	@Enumerated(EnumType.STRING)
	private SimNaoEnum resolvido;

	@Column(name = "PEPD_DH_DATA_RESOLUCAO")
	private Date dataResolucao;

	@Column(name = "PEPD_DS_SOLUCAO")
	private String descricaoSolucao;

	@Column(name = "USUA_CD_RESP_RESOLUCAO")
	private Integer idUsuarioResolucao;
	
	@Column(name="PEPD_DH_DATA_CRIACAO")
	private Date dataCriacao;
	
	@OneToOne
	@JoinColumn(name = "USUA_CD_RESP_PENDENCIA")
	private Usuario responsavelPelaPendencia;
	
	@Column(name = "PEPD_FL_TIPO_PENDENCIA")
	@Enumerated(EnumType.STRING)
	private TipoPendenciaPedidoEnum tipo;
	
	public PendenciaPedido() {
		super(Constantes.USUARIO_ADMINISTRADOR.toString());
	}
	
	public static PendenciaPedido gerarPendenciaCancelamentoPedidoComControlado(Integer numeroPedido,Motivo motivo, Usuario usuario) {
		PendenciaPedido pendenciaPedido = new PendenciaPedido();
		pendenciaPedido.setPedido(new Pedido(numeroPedido));
		pendenciaPedido.setMotivo(motivo);
		pendenciaPedido.setDescricaoPendencia(Constantes.PENDENCIA_DESCRICAO);
		pendenciaPedido.setResolvido(SimNaoEnum.N);
		pendenciaPedido.setDataCriacao(new Date());
		pendenciaPedido.setResponsavelPelaPendencia(usuario);
		pendenciaPedido.setTipo(TipoPendenciaPedidoEnum.D);
		
		return pendenciaPedido;
	}

}
