package com.clique.retire.schedule.enums;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FasePedidoEnum {

	DESISTENCIA("01", "Desistência", "Desistência", "Desistência"),
	ATENDIDO("03", "Atendido", "Aguardando separação", "Ag. Separação"),
	EM_ANALISE_RISCO("04", "Em Análise de Risco", "Em Análise de Risco", "Em Análise de Risco"),
	AGUARDANDO_ATENDIMENTO("05", "Aguardando atendimento", "Aguardando atendimento", "Ag. atendimento"),
	EM_SEPARACAO("06", "Em Separação", "Em Separação", ""),
	EM_NEGOCIACAO("07", "Em Negociação", "Aguardando negociação", "Ag. Drogatel"),
	EM_REGISTRO("08", "Em Registro", "Em registro", "Ag. Emitir Cupom"),
	NAO_ENTREGUE("09", "Não Entregue", "Não Entregue", "Não Entregue"),
	EXPEDIDO("10", "Expedido", "Expedido", "Expedido"),
	ENTREGUE("11", "Entregue", "Entregue", "Já retirado"),
	CANCELADO("12", "Cancelado", "Cancelado", "Cancelado"),
	DEVOLUCAO_TOTAL("13", "Devolução Total", "Devolvido", "Cancelado"),
	AGUARDANDO_NEGOCIACAO("15", "Ag. Negociação", "Aguardando negociação", "Ag. Drogatel"),
	AGUARDANDO_NEGOCIACAO_REINCIDENTE("16", "Ag. Negociação Reincidente", "Aguardando negociação reincidente", "Ag. Drogatel"),
	AGUARDANDO_CONFIRMACAO_PAGAMENTO("27", "Ag. Conf. Pagamento", "Aguardando confirmação de pagamento", "Ag. Conf. Pagamento"),
	AGUARDANDO_REGISTRO("18", "Ag. Registro", "Aguardando registro", ""),
	AGUARDANDO_EXPEDICAO("19", "Ag. Expedição", "Aguardando retirada", "Pronto para retirada"),
	AGUARDANDO_MERCADORIA("20", "Ag. Mercadoria", "Aguardando mercadoria", "Ag. Mercadoria"),
	EMITIDO("21", "Emitido", "Emitido", ""),
	AGUARDANDO_EXPEDICAO_ROTEIRIZADA("22", "Aguardando Expedicao Roteirizada", "Aguardando Expedicao Roteirizada", "Ag. Expedicao Roteirizada"),
	ENVIADO("23", "Enviado", "Enviado", ""),
	RECEBIDO("24", "Recebido", "Recebido", ""),
	RECEBIDO_COM_PENDENCIA("25", "Recebido com pendencia", "Recebido com pendencia", ""),
	ENTREGUE_COM_PENDENCIA("26", "Entregue com Pendência", "Entregue c/ Pendência", ""),
	AGUARDANDO_APROVACAO_RECEITA("28", "Ag. aprovação de Receita", "Aguardando receitas e lotes", ""),
	AGUARDANDO_RECEITA("29", "Ag. aprovação de Receita", "Aguardando receita", "Aguardando receita"),
	AGUARDANDO_PAGAMENTO_GNRE("30", "Aguardando Pagamento GNRE", "Aguardando Pagamento GNRE", "Ag. Pagamento GNRE"),
	AGUARDANDO_PRAZO_INICIO_SEPARACAO("31", "Aguardando prazo para inicio de separação", "Aguardando prazo para inicio de separação", "Ag. prazo para inicio de separação"),
	SOLICITANDO_CAPTURA("33", "Solicitando captura", "Solicitando captura", "Solicitando captura"),
	AGUARDANDO_RECEITAs("34", "Em Edição", "Em Edição", "Em Edição");
	
	private String chave;
	private String valor;
	private String descricaoPainel;
	private String descricaoCombo;

	public static FasePedidoEnum buscarPorChave(String chave) {
		return Stream.of(values())
			.filter(fase -> fase.getChave().equals(chave))
			.findFirst()
			.orElse(null);
	}
	
}