package com.clique.retire.schedule.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class FilialCosmosRepositoryImpl implements FilialCosmosRepositoryCustom {

  @PersistenceContext(unitName = "cosmosEntityManager")
  private EntityManager em;

  @Override
  @SuppressWarnings("unchecked")
  public Map<Integer, String> buscarUmIPParaCadaFilial(List<Integer> codigosFiliais) {
    StringBuilder sql = new StringBuilder()
      .append("SELECT ci.fili_cd_filial AS codigo_filial, MIN(ci.cnit_nm_estacao) AS ip FROM controle_intranet ci ")
      .append("WHERE ci.fili_cd_filial IN :codigosFiliais ")
      .append("GROUP BY ci.fili_cd_filial");

    List<Tuple> result = (List<Tuple>) em.createNativeQuery(sql.toString(), Tuple.class)
      .setParameter("codigosFiliais", codigosFiliais)
      .getResultList();

    Function<Tuple, Integer> codigoFilialExtractor = tupla -> tupla.get("codigo_filial", Integer.class);
    Function<Tuple, String> ipExtractor = tupla -> tupla.get("ip", String.class);

    return result.stream().collect(Collectors.toMap(codigoFilialExtractor, ipExtractor));
  }

}
