package com.clique.retire.schedule.service;

import com.clique.retire.schedule.config.ConfiguracaoLED;
import com.clique.retire.schedule.dto.FilialCorSinalizadorDTO;
import com.clique.retire.schedule.repository.CancelamentoPedidoRepositoryCustom;
import com.clique.retire.schedule.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SinalizadorService {

  private final PedidoRepository pedidoRepository;
  private final SimpMessageSendingOperations messagingTemplate;
  private final CancelamentoPedidoRepositoryCustom cancelamentoPedidoRepository;

  public void verificarLuzesCliqueRetire(Integer codigoFilial) {
    List<FilialCorSinalizadorDTO> filiaisDTO = pedidoRepository.buscarFiliaisEInformacoesDeAtraso(codigoFilial);

    Predicate<FilialCorSinalizadorDTO> isCorPreta = filialCorDTO -> ConfiguracaoLED.COR_PRETA.equals(filialCorDTO.getCor());
    Consumer<FilialCorSinalizadorDTO> atualizarLed = filialCorDTO -> atualizarCorPorFilial(filialCorDTO.getFilial(), filialCorDTO.getCor());

    filiaisDTO.stream()
      .filter(isCorPreta.negate())
      .forEach(atualizarLed);

    List<Integer> filiaisComLedDesligado = filiaisDTO.stream()
      .filter(isCorPreta)
      .map(FilialCorSinalizadorDTO::getFilial)
      .collect(Collectors.toList());

    if (filiaisComLedDesligado.isEmpty())
    	return;
    
    List<FilialCorSinalizadorDTO> filiaisComPedidosCanceladosOuLedDesligadoDTO =
      cancelamentoPedidoRepository.buscarPedidosCanceladosPorFiliaisAptosParaReposicao(filiaisComLedDesligado);

    filiaisComPedidosCanceladosOuLedDesligadoDTO.forEach(atualizarLed);
  }

  public void atualizarCorPorFilial(Integer filial, String corHexadecimal) {
    String topico = String.format("/filial/%s", filial);
    messagingTemplate.convertAndSend(topico, corHexadecimal);
  }

}