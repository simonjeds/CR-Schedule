package com.clique.retire.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clique.retire.schedule.model.drogatel.PendenciaPedido;

public interface PendenciaPedidoRepository extends JpaRepository<PendenciaPedido, Long>, PendenciaPedidoRepositoryCustom {

}
