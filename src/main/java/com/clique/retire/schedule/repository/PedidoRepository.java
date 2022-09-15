package com.clique.retire.schedule.repository;

import com.clique.retire.schedule.model.drogatel.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>, PedidoRepositoryCustom {

}
