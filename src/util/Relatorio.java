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

public class Relatorio {
	
	public static List<String> gerarRelatorioDividendos3X(boolean operacaoAberta) {
		
		List<Acao> acoes;
		if (operacaoAberta) {
			acoes = new AcaoDAO().obterAcoesAbertas();
		}else {
			acoes = new AcaoDAO().obterAcoesFechadas();
		}
		
        List<String> relatorioLinhas = new ArrayList<>();
        
        relatorioLinhas.add("=========================================================================================");
        relatorioLinhas.add("= RESUMO DE OPERAÇÕES EM ABERTO                                                         =");
        relatorioLinhas.add("=========================================================================================");

        if (acoes.isEmpty()) {
             relatorioLinhas.add("Nenhuma operação encontrada.");
             return relatorioLinhas;
        }

        for (Acao acao : acoes) {      
    		Acao ac = new AcaoDAO().obterAcaoPorId(acao.getId());
    	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
    	    Operacao op = new OperacaoDividendo(ac,opcoes) ;
    	    
            relatorioLinhas.addAll(obterResumoDaOperacao(op, operacaoAberta));
            relatorioLinhas.add("-----------------------------------------------------------------------------------------"); // Separador
        }
        return relatorioLinhas;
    }
	
    private static List<String> obterResumoDaOperacao(Operacao operacao, boolean operacaoAberta) {
        
		ResultadoOperacao resultadoOperacao= OperacaoAnalytics.sumarizaReeultado(operacao,  operacaoAberta);
		Acao acao = operacao.getAcao();

        Double valorInvestido =  acao.getQuantidade() * acao.getPrecoCompra();
        Double retornoPercentualTotal = 
        		100 *(resultadoOperacao.getResultado() /( acao.getQuantidade() * acao.getPrecoCompra()));
        
        List<String> linhas = new ArrayList<>();
        String linhaAcao;
        // LINHA PRINCIPAL DA AÇÃO
        if(operacaoAberta) {
	    	linhaAcao = String.format("%s | Investimento: %s |RESULTADO : %s | %s%%",
	    		acao.getAtivo(),
	    	    Utils.formatarParaDuasDecimais(valorInvestido),
	    	    Utils.formatarParaDuasDecimais(resultadoOperacao.getResultado()),
	    	    Utils.formatarParaDuasDecimais(retornoPercentualTotal)
	    	);  
	    	linhas.add(linhaAcao);
	        linhaAcao = String.format("      | Qtde: %d | Compra: %s | Strike: %s | PM: %s | Cotação: %s",
	        	    acao.getQuantidade(),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoCompraAcao()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getStrike()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getCotacaoAtual())
	        	);
	        
	        linhas.add(linhaAcao);
        }else {
	    	linhaAcao = String.format(" %s | Investimento: %s |RESULTADO : %s | %s%%",
	    		acao.getAtivo(),
	    	    Utils.formatarParaDuasDecimais(valorInvestido),
	    	    Utils.formatarParaDuasDecimais(resultadoOperacao.getResultado()),
	    	    Utils.formatarParaDuasDecimais(retornoPercentualTotal)
	    	);        
	        linhas.add(linhaAcao);  
	        
	        linhaAcao = String.format("       | Qtde: %d | Compra: %s | Strike: %s | PM: %s",
	        		acao.getQuantidade(),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoCompraAcao()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getStrike()),
	        	    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado())
	        	);
	        linhas.add(linhaAcao);
        }
        
        // INFORMAÇÕES ADICIONAIS DE OPÇÕES
        List<Opcao> opcoes = operacao.getOpcoes();
        if (opcoes != null && !opcoes.isEmpty()) {
            linhas.add("    -> Opções:");
            for (Opcao opcao : opcoes) {
                String linhaOpcao = String.format("       [%s] Compra: %s | Venda: %s | Strike: %s",
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
