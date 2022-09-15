package com.clique.retire.schedule.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clique.retire.schedule.dto.PedidoDTO;
import com.clique.retire.schedule.enums.FasePedidoEnum;
import com.clique.retire.schedule.model.drogatel.Pedido;
import com.clique.retire.schedule.repository.FilialCosmosRepositoryCustom;
import com.clique.retire.schedule.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoService {

  private final PedidoRepository repository;
  private final PainelCliqueRetireService painelCliqueRetireService;
  private final FilialCosmosRepositoryCustom filialCosmosRepository;

  @Transactional
  public void imprimirEtiquetasDeControladosPendentes() {
    List<PedidoDTO> pedidos = repository.buscarPedidosParaImpressaoDeEtiquetas();
    if (pedidos.isEmpty()) return;

    String numerosPedidos = pedidos.stream()
      .map(PedidoDTO::getNumeroPedido)
      .map(String::valueOf)
      .collect(Collectors.joining(", "));

    log.info("Iniciando impressão de etiquetas para os pedidos: [{}]", numerosPedidos);

    List<Integer> codigosFiliais = pedidos.stream().map(PedidoDTO::getCodigoFilial).collect(Collectors.toList());
    Map<Integer, String> ipsPorFilial = filialCosmosRepository.buscarUmIPParaCadaFilial(codigosFiliais);

    pedidos.forEach(pedido -> pedido.setIp(ipsPorFilial.get(pedido.getCodigoFilial())));

    Map<PedidoDTO, Boolean> resultadoImpressao = painelCliqueRetireService.emitirEtiqueta(pedidos);

    List<Integer> pedidosComImpressaoBemSucedida = resultadoImpressao.entrySet().stream()
      .filter(Map.Entry::getValue)
      .map(Map.Entry::getKey)
      .map(PedidoDTO::getNumeroPedido)
      .collect(Collectors.toList());

    if (pedidosComImpressaoBemSucedida.isEmpty()) return;

    repository.atualizarFlagEmissaoEtiquetaNasReceitas(pedidosComImpressaoBemSucedida);
    log.info("Etiquetas impressas com sucesso para os pedidos: {}", pedidosComImpressaoBemSucedida);
  }

  @Transactional
  public boolean registrarNovaFasePedido(Pedido pedido, FasePedidoEnum novaFase) {
    try {
      if (pedido.getFasePedido().equals(novaFase)) {
        return false;
      }

      pedido.setFasePedido(novaFase);
      this.repository.save(pedido);
      this.repository.salvarHistoricoPedido(pedido, novaFase);
      this.repository.atualizarFasePedidoServico(pedido.getNumeroPedido(), novaFase.getChave());
      return true;
    } catch (Exception ex) {
      log.error("Erro ao executar a alteração de fase do pedido: " + pedido.getNumeroPedido(), ex);
      return false;
    }
  }

  public void confirmarEntregaDePedidosDeControlados() {
    int numeroAleatorio = new Random().nextInt();
    log.info("[PedidoTemporizador-{}] Inicio confirmarEntregaDePedidosDeControlados()", numeroAleatorio);
    List<Integer> pedidos = repository.buscarPedidosDeControladoAptosParaFaseEntregue();

    if (!pedidos.isEmpty()) {
      String token = painelCliqueRetireService.validarMatricula();

      pedidos.forEach(numeroPedido -> {
        try {
          painelCliqueRetireService.confirmarEntregaPedido(numeroPedido, token);
          log.info("Fase do pedido de controlado [{}] alterada para ENTREGUE.", numeroPedido);
        } catch (Exception ex) {
          log.error("Erro ao confirmar entrega do pedido {}. Exception: {}", numeroPedido, ex.getMessage());
        }
      });
    }
    log.info("[PedidoTemporizador-{}] Fim confirmarEntregaDePedidosDeControlados()", numeroAleatorio);
  }
  
  	public void criarExpedicaoPedidoServico() {
  		List<PedidoDTO> listaPS = repository.buscarPedidosDeServicoParaExpedicao();
  		log.info("[PedidoTemporizador] criarExpedicaoPedidoServico gerou {} pedido(s) de serviço", listaPS.size());
  		if (listaPS.isEmpty()) return;
  		
  		String token = painelCliqueRetireService.validarMatricula();
  		painelCliqueRetireService.gerarExpedicaoPedidoServico(listaPS, token);
  	}

  	public void definirExpedicaoPedidoServicoComoNaoEntregue() {
  		List<PedidoDTO> lista = repository.buscarPedidosDeServicoComOcorrencia();
  		log.info("[PedidoTemporizador] definirExpedicaoPedidoServicoComoNaoEntregue encontrou {} ocorrência(s)", lista.size());
  		
	    lista.forEach(pedido -> 
  			painelCliqueRetireService.registrarNovaFase(pedido.getNumeroPedido().longValue(), FasePedidoEnum.NAO_ENTREGUE)
  		);
  	}
}
