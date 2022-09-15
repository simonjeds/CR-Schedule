package com.clique.retire.schedule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.clique.retire.schedule.client.PainelCliqueRetireClient;
import com.clique.retire.schedule.dto.PedidoDTO;
import com.clique.retire.schedule.enums.FasePedidoEnum;
import com.clique.retire.schedule.util.FeignUtil;
import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class PainelCliqueRetireService {

  private final Environment env;
  private PainelCliqueRetireClient painelCliqueRetireClient;

  @PostConstruct
  private void init() {
    String url = env.getProperty("url.base.painel.clique.retire");
    this.painelCliqueRetireClient = FeignUtil.getPainelCliqueRetireClient(url);
  }

  public String validarMatricula() {
    JsonObject response = painelCliqueRetireClient.validarMatricula();
    return response.getAsJsonObject("data").get("token").getAsString();
  }

  public void confirmarEntregaPedido(Integer numeroPedido, String token) {
    painelCliqueRetireClient.confirmarEntregaPedido(numeroPedido, token);
  }

  public Map<PedidoDTO, Boolean> emitirEtiqueta(List<PedidoDTO> pedidos) {
    String token = this.validarMatricula();
    Map<PedidoDTO, Boolean> resultadoImpressao = new HashMap<>();

    pedidos.forEach(pedido -> {
      try {
        painelCliqueRetireClient.emitirEtiqueta(pedido.getNumeroPedido(), token, pedido.getIp());
        resultadoImpressao.put(pedido, true);
      } catch (Exception ex) {
        log.error("Erro na impressão da etiqueta para pedido [{}]: {}", pedido.getNumeroPedido(), ex.getMessage());
        resultadoImpressao.put(pedido, false);
      }
    });

    return resultadoImpressao;
  }
  
  public void gerarExpedicaoPedidoServico(List<PedidoDTO> pedidoDTO, String token) {
	  painelCliqueRetireClient.gerarExpedicaoPedidoServico(pedidoDTO, token);
  }
  
  public void registrarNovaFase(Long numeroPdido, FasePedidoEnum fase) {
	  painelCliqueRetireClient.registrarFase(numeroPdido, fase.getChave());
  }

}
