package com.clique.retire.schedule.repository;

import com.clique.retire.schedule.dto.FilialCorSinalizadorDTO;

import java.util.List;

public interface CancelamentoPedidoRepositoryCustom {

  List<FilialCorSinalizadorDTO> buscarPedidosCanceladosPorFiliaisAptosParaReposicao(List<Integer> filiais);

}
