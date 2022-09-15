package com.clique.retire.schedule.service;

import static com.clique.retire.schedule.util.Constantes.PENDENCIA_PEDIDO_MOTIVO;
import static com.clique.retire.schedule.util.Constantes.USUARIO_ADMINISTRADOR;

import java.util.List;
import java.util.stream.Collectors;

import com.clique.retire.schedule.repository.MotivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clique.retire.schedule.model.drogatel.Motivo;
import com.clique.retire.schedule.model.drogatel.PendenciaPedido;
import com.clique.retire.schedule.model.drogatel.Usuario;
import com.clique.retire.schedule.repository.PendenciaPedidoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PendenciaPedidoService {

  private final PendenciaPedidoRepository repository;
  private final MotivoRepository motivoRepository;

  @Transactional
  public void solicitarCancelamentoPedidoViaSAC() {
    List<Integer> pedidos = repository.obterPedidosComControladoAptosParaOCancelamento();
    if (pedidos.isEmpty()) return;

    Motivo motivo = motivoRepository.findByDescricao(PENDENCIA_PEDIDO_MOTIVO);
    Usuario usuario = new Usuario(USUARIO_ADMINISTRADOR);

    pedidos.forEach(
      numeroPedido -> repository.save(
        PendenciaPedido.gerarPendenciaCancelamentoPedidoComControlado(numeroPedido, motivo, usuario)
      )
    );

    log.info("Pedido(s) a cancelar : {} ", pedidos.stream().map(String::valueOf).collect(Collectors.joining(", ")));
  }
}