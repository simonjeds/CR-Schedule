package com.clique.retire.schedule.repository;

import java.util.List;

import com.clique.retire.schedule.dto.FilialCorSinalizadorDTO;
import com.clique.retire.schedule.dto.PedidoDTO;
import com.clique.retire.schedule.enums.FasePedidoEnum;
import com.clique.retire.schedule.model.drogatel.Pedido;

/**
 * @author Framework
 */
public interface PedidoRepositoryCustom {
	
	/**
	 * Método que obtém as filiais e as informações sobre atraso de pedido e
	 * separação.
	 * 
	 * @param filial codigo da filial
	 * @return List<HorarioPedidoFilialDTO>
	 */
	List<FilialCorSinalizadorDTO> buscarFiliaisEInformacoesDeAtraso(Integer filial);

	/**
	 * Ajusta os pedidos para entregue se os mesmos tiverem algum item com medicamento controlado.
	 * @return lista de ids dos pedidos que estão aptos para fase ENTREGUE
	 */
	List<Integer> buscarPedidosDeControladoAptosParaFaseEntregue();
	
	/**
	 * Busca os pedidos de controlados com NF emitida que ainda não emitiram etiqueta
	 * @return ids dos pedidos para impressão
	 */
	List<PedidoDTO> buscarPedidosParaImpressaoDeEtiquetas();

	/**
	 * Atualiza o campo RCPC_FL_ETIQUETAEMITIDA na tabela RECEITA_PRODUTO_CONTROLADO para os pedidos que tiveram emissão
	 * de etiquetas com sucesso
	 * @param pedidos lista com ids dos pedidos para atualização
	 */
	void atualizarFlagEmissaoEtiquetaNasReceitas(List<Integer> pedidos);
	
	/**
	 * Cria um registro no histórico do pedido com uma nova fase
	 */
	void salvarHistoricoPedido(Pedido pedido, FasePedidoEnum novaFase);
	
	/**
	 * Obtém uma LISTA de pedidos de serviço aptos para expedição
	 * @return LISTA
	 */
	List<PedidoDTO> buscarPedidosDeServicoParaExpedicao();
	
	/**
	 * Obtém uma LISTA de pedidos de serviço com a sinalização de alguma ocorrência
	 * @return LISTA
	 */
	List<PedidoDTO> buscarPedidosDeServicoComOcorrencia();
	
	/**
	 * Atualiza a fase do pedido de serviço
	 * @param numeroPedido
	 * @param fase
	 */
	void atualizarFasePedidoServico(Integer numeroPedido, String fase);

}
