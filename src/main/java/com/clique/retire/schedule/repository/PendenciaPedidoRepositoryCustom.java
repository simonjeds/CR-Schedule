package com.clique.retire.schedule.repository;

import com.clique.retire.schedule.model.drogatel.PendenciaPedido;

import java.util.List;

public interface PendenciaPedidoRepositoryCustom {
	
	/**
	 * Obter todos os pedidos que contém controlado que não houve emissão de nota após um periodo pre-definido.
	 * @return
	 */
	List<Integer> obterPedidosComControladoAptosParaOCancelamento();

	/**
	 * Obter todos os pedidos que passaram pelo retorno do motociclista com não entrega e já estão aptos para voltarem
	 * como aguardando expedição (Fase 19)
	 * @return lista de pedidos
	 */
  List<PendenciaPedido> buscarPedidosNaoEntreguesAptosParaRetirada();

}
