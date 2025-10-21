package relat;

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
import model.TipoOperacaoEnum;
import util.OperacaoAnalyticsDividendos3X;
import util.OperacaoAnalyticsGanhaGanha;
import util.Utils;


public abstract class BaseRelatorio {

    protected static final String CABECALHO_1 = 	"\t %s | Investimento: %s |RESULTADO : %s | %s%%";
    protected static final String CABECALHO_2 = 	"\t       | Qtde: %d | Compra: %s | Strike: %s | PM: %s | Cotação: %s";
    protected static final String CABECALHO_3 = 	"\t       | Qtde: %d | Compra: %s | Venda: %s | PM: %s ";
    protected static final String CABECALHO_OPCAO = "\t        [%s] Compra: %s | Venda: %s | Strike: %s";
    
    protected double somaParaMediaPercentualTotal = 0;
    protected int contadorParaMediaPercentualTotal = 0;

    protected final AcaoDAO acaoDAO = new AcaoDAO();
    protected final OpcaoDAO opcaoDAO = new OpcaoDAO();
    protected final DividendoDAO dividendoDAO = new DividendoDAO();
    protected abstract boolean isOperacaoAberta();

    protected final TipoOperacaoEnum tipoOperacao;
    
    public BaseRelatorio(TipoOperacaoEnum tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    protected abstract List<Acao> obterAcoes();
    
    /**
     * Lógica principal de geração de relatório, herdada por todas as subclasses.
     */
    public List<String> gerarRelatorio() {
        
        List<String> linhas = new ArrayList<>();
        

        List<Acao> acoes = obterAcoes(); 
        
        // Se a lista de ações estiver vazia, retorna a mensagem.
        if (acoes == null || acoes.isEmpty()) {
            linhas.add("Nenhuma operação encontrada para o Tipo: " + tipoOperacao.name().replace("_", " "));
            return linhas;
        }

        linhas.add("==========================================================================================================");
        linhas.add("RELATÓRIO POR ESTRATÉGIA: " + tipoOperacao.name().replace("_", " "));
        linhas.add("==========================================================================================================");

        somaParaMediaPercentualTotal = 0;
        contadorParaMediaPercentualTotal = 0;

        for (Acao acao : acoes) {
            ResultadoOperacao resultadoOperacao = calcularResultado(acao); 

            double investimento = resultadoOperacao.getPrecoCompraAcao() * acao.getQuantidade();
            double resultado = resultadoOperacao.getResultado();
            double retornoPercentualTotal = resultado / investimento * 100;
            
            String cabecalho1 = String.format(CABECALHO_1,
                    acao.getAtivo(),
                    Utils.formatarParaDuasDecimais(investimento),
                    Utils.formatarParaDuasDecimais(resultado),
                    Utils.formatarParaDuasDecimais(retornoPercentualTotal)
            );
            linhas.add(cabecalho1);

            if (acao.getDataVenda() == null || acao.getDataVenda().trim().isEmpty()) {
                String linhaAcao = String.format(CABECALHO_2,
                        acao.getQuantidade(),
                        Utils.formatarParaDuasDecimais(acao.getPrecoCompra()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getStrike()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getCotacaoAtual())
                );
                linhas.add(linhaAcao);
            } else {
                String linhaAcao = String.format(CABECALHO_3,
                        acao.getQuantidade(),
                        Utils.formatarParaDuasDecimais(acao.getPrecoCompra()),
                        Utils.formatarParaDuasDecimais(acao.getPrecoVenda()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado())
                );
                linhas.add(linhaAcao);
            }
            
            somaParaMediaPercentualTotal += retornoPercentualTotal;
            contadorParaMediaPercentualTotal++;
            
            // INFORMAÇÕES ADICIONAIS DE OPÇÕES
            List<Opcao> opcoes = opcaoDAO.obterOpcoesPorIdAcao(acao.getId()); 
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
            
            // INFORMAÇÕES ADICIONAIS DE DIVIDENDOS
            List<Dividendo> dividendos = dividendoDAO.buscarPorAcao(acao.getId());
            if (dividendos != null && !dividendos.isEmpty()) {
                double totalDividendo = dividendos.stream().mapToDouble(Dividendo::getValor).sum();
                linhas.add(String.format("    -> Total Dividendos Recebidos: %s", 
                                        Utils.formatarParaDuasDecimais(totalDividendo)));
            }

            linhas.add("----------------------------------------------------------------------------------------------------------");
        }

        if (contadorParaMediaPercentualTotal > 0) {
            double media = somaParaMediaPercentualTotal / contadorParaMediaPercentualTotal;
            linhas.add(String.format("\tMÉDIA PERCENTUAL TOTAL: %s%%", Utils.formatarParaDuasDecimais(media)));
        }
        
        return linhas;
    }
    
    private ResultadoOperacao calcularResultado(Acao ac) {
    	Acao acao = new AcaoDAO().obterAcaoPorId(ac.getId());
	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(acao.getId());
    	Operacao operacao = new Operacao(acao,opcoes) ;
        
    	try {
    	    switch (tipoOperacao) {
    	        case DIVIDENDO3X:
    	            return new OperacaoAnalyticsDividendos3X().sumarizaResultado(operacao, isOperacaoAberta());
    	        case GANHA_GANHA:
    	            return new OperacaoAnalyticsGanhaGanha().sumarizaResultado(operacao, isOperacaoAberta());	            
    	        default:
    	            return null; 
    	    }
    	} catch (Exception e) {
    	    System.out.println("Erro em obterResumoDaOperacao. Ativo: " + operacao.getAcao().getAtivo());
    	    return null;
    	}
    }


}