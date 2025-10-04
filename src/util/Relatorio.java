package util;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<String> gerarResumoOperacoes(boolean operacaoAberta) {
		
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
    	    
            relatorioLinhas.addAll(gerarResumoDaOperacao(op, operacaoAberta));
            relatorioLinhas.add("-----------------------------------------------------------------------------------------"); // Separador
        }
        return relatorioLinhas;
    }
	
	
	public static List<String> gerarResumoDaOperacao(Operacao operacao, boolean operacaoAberta) {
        
		ResultadoOperacao resultadoOperacao= OperaçãoAnalytics.sumarizaReeultado(operacao,  operacaoAberta);
		Acao acao = operacao.getAcao();
		
		
        // LINHA PRINCIPAL DA AÇÃO
        String linhaAcao = String.format("ATIVO: %s | Qtde: %d | Compra: %s | Strike: %s "
        		+ "| PM: %s | Cotação: %s | Resultado: %s | Patrimônio: %s",
        		acao.getAtivo(),
        		acao.getQuantidade(),
                ValidatorUtils.formatarParaDuasDecimais(resultadoOperacao.getPrecoCompraAcao()),
                ValidatorUtils.formatarParaDuasDecimais(resultadoOperacao.getStrike()),
                ValidatorUtils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado()),
                ValidatorUtils.formatarParaDuasDecimais(resultadoOperacao.getCotacaoAtual()),
                ValidatorUtils.formatarParaDuasDecimais(resultadoOperacao.getResultado()),
                ValidatorUtils.formatarParaDuasDecimais(resultadoOperacao.getPatrimonioAtual())
        );
        
        List<String> linhas = new ArrayList<>();
        
        linhas.add(linhaAcao);
        
        // INFORMAÇÕES ADICIONAIS DE OPÇÕES
        List<Opcao> opcoes = operacao.getOpcoes();
        if (opcoes != null && !opcoes.isEmpty()) {
            linhas.add("    -> OPÇÕES VENDIDAS/COMPRADAS:");
            for (Opcao opcao : opcoes) {
                String linhaOpcao = String.format("       [%s] Compra: %s | Venda: %s | Strike: %s",
                        opcao.getOpcao(),
                        ValidatorUtils.formatarParaDuasDecimais(opcao.getPrecoCompra()),
                        ValidatorUtils.formatarParaDuasDecimais(opcao.getPrecoVenda()),
                        ValidatorUtils.formatarParaDuasDecimais(opcao.getStrike())
                );
                linhas.add(linhaOpcao);
            }
        }
        
        // INFORMAÇÕES ADICIONAIS DE DIVIDENDOS (Apenas total)
        List<Dividendo> dividendos = new DividendoDAO().buscarPorAcao(acao.getId());
        if (dividendos != null && !dividendos.isEmpty()) {
            double totalDividendo = dividendos.stream().mapToDouble(Dividendo::getValor).sum();
            linhas.add("    -> DIVIDENDOS RECEBIDOS: " + ValidatorUtils.formatarParaDuasDecimais(totalDividendo));
        }

        return linhas;
    }
	

}
