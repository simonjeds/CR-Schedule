package com.clique.retire.schedule.config;

import com.clique.retire.schedule.enums.ParametroEnum;
import com.clique.retire.schedule.repository.ParametroCliqueRetireCustom;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ConfiguracaoLED {

  public static final String COR_PRETA = "#000000";
  public static final String COR_VERMELHA = "#FF0000";
  private static final String COR_VERDE = "#00FF00";
  private static final String COR_AMARELA = "#FFA500";
  private static final String COR_ROXA = "#800080";

  private String COR_SEPARACAO_PENDENTE;
  private String COR_SEPARACAO_ATRASADA;
  private String COR_NOVO_PEDIDO;
  private String FASE_APOS_SEPARACAO;
  private Integer TEMPO_SEPARACAO_PENDENTE;
  private Integer TEMPO_SEPARACAO_ATRASADA;

  @Autowired
  private ParametroCliqueRetireCustom parametroRepository;

  @PostConstruct
  private void init() {
    COR_NOVO_PEDIDO = getStringParametrizadaOuPadrao(ParametroEnum.COR_SINALIZADOR_NOVO_PEDIDO, COR_VERDE);
    COR_SEPARACAO_PENDENTE = getStringParametrizadaOuPadrao(ParametroEnum.COR_SINALIZADOR_SEPARACAO_PENDENTE, COR_AMARELA);
    COR_SEPARACAO_ATRASADA = getStringParametrizadaOuPadrao(ParametroEnum.COR_SINALIZADOR_SEPARACAO_ATRASADA, COR_ROXA);
    FASE_APOS_SEPARACAO = getStringParametrizadaOuPadrao(ParametroEnum.FASE_APOS_SEPARACAO, "08");
    TEMPO_SEPARACAO_ATRASADA = getTempoParametrizadoOuPadrao(ParametroEnum.TEMPO_SEPARACAO_ATRASADA, 25);
    TEMPO_SEPARACAO_PENDENTE = getTempoParametrizadoOuPadrao(ParametroEnum.TEMPO_SEPARACAO_PENDENTE, 15);
  }

  private Integer getTempoParametrizadoOuPadrao(ParametroEnum parametro, Integer tempoPadrao) {
    String tempo = parametroRepository.findByNome(parametro.getDescricao());
    return StringUtils.isNotBlank(tempo) ? Integer.valueOf(tempo) : tempoPadrao;
  }

  private String getStringParametrizadaOuPadrao(ParametroEnum parametro, String stringPadrao) {
    String valor = parametroRepository.findByNome(parametro.getDescricao());
    return StringUtils.isNotBlank(valor) ? valor : stringPadrao;
  }

  public String obterCorPeloCodigoDaPrioridade(Integer codigoPrioridade) {
    if (codigoPrioridade == 1) { // VERDE
      return COR_NOVO_PEDIDO;
    }

    if (codigoPrioridade == 2) { // ROXA
      return COR_SEPARACAO_ATRASADA;
    }

    if (codigoPrioridade == 3) { // AMARELA
      return COR_SEPARACAO_PENDENTE;
    }

    return COR_PRETA;
  }

  public Integer getTempoSeparacaoPendente() {
    return TEMPO_SEPARACAO_PENDENTE;
  }

  public Integer getTempoSeparacaoAtrasada() {
    return TEMPO_SEPARACAO_ATRASADA;
  }

  public String getFaseAposSeparacao() {
    return FASE_APOS_SEPARACAO;
  }

}
