package com.clique.retire.schedule.repository;

import com.clique.retire.schedule.model.drogatel.Motivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoRepository extends JpaRepository<Motivo, Long> {

  Motivo findByDescricao(String descricao);

}
