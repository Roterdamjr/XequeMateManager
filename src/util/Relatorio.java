package util;

import java.util.ArrayList;
import java.util.List;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;
import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.Operacao;
import model.OperacaoDividendo;

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
        
        Acao acao = operacao.getAcao();
        List<Opcao> opcoes = operacao.getOpcoes();
        List<Dividendo> dividendos = new DividendoDAO().buscarPorAcao(acao.getId());
        
        List<String> linhas = new ArrayList<>();
        
        Double resultadoDasOpcoes = 0.0;
        if (opcoes != null && !opcoes.isEmpty()) {
            for (Opcao opcao : opcoes) {
                resultadoDasOpcoes +=  opcao.getPrecoVenda() - opcao.getPrecoCompra();
            }
        }
        
        Double resultadoDosDividendos = 0.0;
        if (dividendos != null && !dividendos.isEmpty()) {
            for (Dividendo dividendo : dividendos) {
            	resultadoDosDividendos +=  dividendo.getValor();
            }
        }
        
        // CÁLCULOS PRINCIPAIS
        double cotacao = new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());
        
        int quantidade = acao.getQuantidade();
        double strike = OpcaoDAO.obterStrikeUltimaOpcaoVendida(acao.getId());
        
        double precoMedio = acao.getPrecoCompra()  - resultadoDasOpcoes - resultadoDosDividendos;
        
        double precoVendaCalculo = 0.0;
      
        if (operacaoAberta){
        	precoVendaCalculo = (strike < cotacao) ? strike : cotacao;
        }else {
        	precoVendaCalculo = strike;
        }
        
        if(acao.getId()==8 || acao.getId()==10) {
        	int a=0;
        }
      
        
        Double resultado = quantidade * (precoVendaCalculo - precoMedio);
        Double patrimonio = quantidade * cotacao;

        // LINHA PRINCIPAL DA AÇÃO
        String linhaAcao = String.format("ATIVO: %s | Qtde: %d | Compra: %s | Strike: %s "
        		+ "| PM: %s | Cotação: %s | Resultado: %s | Patrimônio: %s",
                acao.getAtivo(),
                quantidade,
                ValidatorUtils.formatarParaDuasDecimais(acao.getPrecoCompra()),
                ValidatorUtils.formatarParaDuasDecimais(strike),
                ValidatorUtils.formatarParaDuasDecimais(precoMedio),
                ValidatorUtils.formatarParaDuasDecimais(cotacao),
                ValidatorUtils.formatarParaDuasDecimais(resultado),
                ValidatorUtils.formatarParaDuasDecimais(patrimonio)
        );
        linhas.add(linhaAcao);
        
        // INFORMAÇÕES ADICIONAIS DE OPÇÕES
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
        if (dividendos != null && !dividendos.isEmpty()) {
            double totalDividendo = dividendos.stream().mapToDouble(Dividendo::getValor).sum();
            linhas.add("    -> DIVIDENDOS RECEBIDOS: " + ValidatorUtils.formatarParaDuasDecimais(totalDividendo));
        }

        return linhas;
    }


	

}
