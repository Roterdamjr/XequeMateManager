package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.AcaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;
import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import model.OperacaoDividendo;
import model.ResultadoOperacao;
import model.TipoOperacaoEnum;

public class Relatorio {
	
	static double  somaParaMediaPercentualTotal = 0;
	static int  contadorParaMediaPercentualTotal = 0;
	static String CABECALHO_1 = 	" %s | Investimento: %s |RESULTADO : %s | %s%%";
	static String CABECALHO_2 = 	"       | Qtde: %d | Compra: %s | Strike: %s | PM: %s | Cotação: %s";
	static String CABECALHO_3 = 	"       | Qtde: %d | Compra: %s | Venda: %s | PM: %s ";
	static String CABECALHO_OPCAO = "        [%s] Compra: %s | Venda: %s | Strike: %s";
	
	public static List<String> gerarRelatorio(TipoOperacaoEnum tipoOperacao, boolean isOperacaoAberta) {
		
		;
		List<Acao> acoes;
		if (isOperacaoAberta) {
			acoes = new AcaoDAO().obterAcoesAbertas(tipoOperacao.getDbValue());
		}else {
			acoes = new AcaoDAO().obterAcoesFechadasOrdenadasPorData(tipoOperacao.getDbValue());
		}
		
        List<String> relatorioLinhas = new ArrayList<>();
        
        relatorioLinhas.add(" =========================================================================================");
        relatorioLinhas.add(" = RESUMO DE OPERAÇÕES      		                                                      =");
        relatorioLinhas.add(" =========================================================================================");

        if (acoes.isEmpty()) {
             relatorioLinhas.add("Nenhuma operação encontrada.");
             return relatorioLinhas;
        }

        for (Acao acao : acoes) {      
    		Acao ac = new AcaoDAO().obterAcaoPorId(acao.getId());
    	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
    	    Operacao op = new OperacaoDividendo(ac,opcoes) ;
    	    
            relatorioLinhas.addAll(obterResumoDaOperacao(op, isOperacaoAberta));
            relatorioLinhas.add(" -----------------------------------------------------------------------------------------"); 
        }
      
        /*
         * se estiver exibindo operações fechadas imprime Média
         */
        if (!isOperacaoAberta) {
	        relatorioLinhas.add(" =========================================================================================");
	        relatorioLinhas.add("   QTDE DE OPERAÇÕES: "  + 
	        		Utils.formatarParaDuasDecimais(contadorParaMediaPercentualTotal)+
	        		"   MÉDIA DE RESULTADO: "  + 
	        		Utils.formatarParaDuasDecimais(somaParaMediaPercentualTotal/contadorParaMediaPercentualTotal)+ "%");
	        relatorioLinhas.add(" =========================================================================================");
        }
        return relatorioLinhas;
        
    }
	
    private static List<String> obterResumoDaOperacao(Operacao operacao, boolean isOperacaoAberta) {
    	ResultadoOperacao resultadoOperacao = null;
    	
        try {
		resultadoOperacao = 
				new OperacaoAnalyticsDividendos3X().sumarizaResultado(operacao,  isOperacaoAberta);
        }catch (Exception e) {
			System.out.println("Erro em obterResumoDaOperacao. Ativo: " + operacao.getAcao().getAtivo());
		}
		
		Acao acao = operacao.getAcao();

        Double valorInvestido =  acao.getQuantidade() * acao.getPrecoCompra();
        Double retornoPercentualTotal = 
        		100 *(resultadoOperacao.getResultado() /( acao.getQuantidade() * acao.getPrecoCompra()));
        
        List<String> linhas = new ArrayList<>();
        String linhaAcao;
        
        if(isOperacaoAberta) {
	    	linhaAcao = String.format(CABECALHO_1,
	    		acao.getAtivo(),
	    	    Utils.formatarParaDuasDecimais(valorInvestido),
	    	    Utils.formatarParaDuasDecimais(resultadoOperacao.getResultado()),
	    	    Utils.formatarParaDuasDecimais(retornoPercentualTotal)
	    	);  
	    	linhas.add(linhaAcao);
	        linhaAcao = String.format(CABECALHO_2,
	        	    acao.getQuantidade(),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoCompraAcao()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoVendaAcao()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getCotacaoAtual())
	        	);
	        
	        linhas.add(linhaAcao);
        }else {
	    	linhaAcao = String.format(CABECALHO_1,
	    		acao.getAtivo(),
	    	    Utils.formatarParaDuasDecimais(valorInvestido),
	    	    Utils.formatarParaDuasDecimais(resultadoOperacao.getResultado()),
	    	    Utils.formatarParaDuasDecimais(retornoPercentualTotal)
	    	);        
	        linhas.add(linhaAcao);  
	        
	        linhaAcao = String.format(CABECALHO_3,
	        		acao.getQuantidade(),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoCompraAcao()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoVendaAcao()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado())
	        	);
	        linhas.add(linhaAcao);
	        somaParaMediaPercentualTotal += retornoPercentualTotal;
	        contadorParaMediaPercentualTotal++;
        }
        
        // INFORMAÇÕES ADICIONAIS DE OPÇÕES
        List<Opcao> opcoes = operacao.getOpcoes();
        if (opcoes != null && !opcoes.isEmpty()) {
            linhas.add("    -> Opções:");
            for (Opcao opcao : opcoes) {
                String linhaOpcao = String.format(CABECALHO_OPCAO,
                        opcao.getOpcao(),
                        Utils.formatarParaDuasDecimais(opcao.getPrecoCompra()),
                        Utils.formatarParaDuasDecimais(opcao.getPrecoVenda()),
                        Utils.formatarParaDuasDecimais(opcao.getStrike())
                );
                linhas.add(linhaOpcao);
            }
        }
        
        // INFORMAÇÕES ADICIONAIS DE DIVIDENDOS (Apenas total)
        List<Dividendo> dividendos = new DividendoDAO().buscarPorAcao(acao.getId());
        if (dividendos != null && !dividendos.isEmpty()) {
            double totalDividendo = dividendos.stream().mapToDouble(Dividendo::getValor).sum();
            linhas.add("    -> DIVIDENDOS RECEBIDOS: " + Utils.formatarParaDuasDecimais(totalDividendo));
        }

        return linhas;
    }
	

}
