package com.clique.retire.schedule.repository;

import java.util.List;
import java.util.Map;

public interface FilialCosmosRepositoryCustom {

  Map<Integer, String> buscarUmIPParaCadaFilial(List<Integer> codigosFiliais);

}
