package com.clique.retire.schedule.client;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.clique.retire.schedule.dto.PedidoDTO;
import com.google.gson.JsonObject;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("api_key: psw#frameworkSwagger2017;")
public interface PainelCliqueRetireClient {
	
	@RequestLine("GET login/atualizar-metricas")
	public Object atualizarMetricas();
	
	@RequestLine("GET login/calcular-metricas")
	public Object calcularMetricas();
	
	@RequestLine("GET login/validar-matricula?matricula=1")
	JsonObject validarMatricula();

	@RequestLine("GET login/atualizar-sinalizador-filial?filial={numeroFilial}")
	public Object atualizarSinalizadorPorFilial(@Param("numeroFilial") Integer numeroFilial);

	@Headers({ "Authorization: Bearer {token}" })
	@RequestLine("GET entrega/super-vendedor/confirmar-entrega-pedido?numeroPedido={numeroPedido}")
	void confirmarEntregaPedido(@Param("numeroPedido") Integer numeroPedido, @Param("token") String token);

	@Headers({ "Authorization: Bearer {token}", "ip: {ip}" })
	@RequestLine("GET emitir-etiqueta?numeroPedido={numeroPedido}")
	void emitirEtiqueta(
		@Param("numeroPedido") Integer numeroPedido, @Param("token") String token, @Param("ip") String ip
	);
	
	@Headers({ "Authorization: Bearer {token}",
			   "Content-Type: application/json" })
	@RequestLine("POST entrega/gerar-expedicao-pedido-servico")
	void gerarExpedicaoPedidoServico(@RequestBody List<PedidoDTO> listaPedidoDTO, @Param("token") String token);
	
	@RequestLine("GET pedido/registrar-fase?numeroPedido={numeroPedido}&fase={fase}")
	void registrarFase(@Param("numeroPedido") Long numeroPedido, @Param("fase") String fase);

}
