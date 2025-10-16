package relat;

import java.util.ArrayList;
import java.util.List;
import dao.AcaoDAO;
import dao.DividendoDAO;
import dao.OpcaoDAO;
import model.Acao;
import model.Dividendo;
import model.Opcao;
import model.ResultadoOperacao;
import model.TipoOperacaoEnum;
import util.Utils;

/**
 * Classe base abstrata para a geração de relatórios de operações.
 * Contém a lógica de formatação comum, mas delega a busca das ações
 * (abertas ou fechadas) para as subclasses.
 */
public abstract class BaseRelatorio {
    
    // Variáveis estáticas e constantes originais, agora protegidas para acesso das subclasses
    protected static final String CABECALHO_1 = 	"\t %s | Investimento: %s |RESULTADO : %s | %s%%";
    protected static final String CABECALHO_2 = 	"\t       | Qtde: %d | Compra: %s | Strike: %s | PM: %s | Cotação: %s";
    protected static final String CABECALHO_3 = 	"\t       | Qtde: %d | Compra: %s | Venda: %s | PM: %s ";
    protected static final String CABECALHO_OPCAO = "\t        [%s] Compra: %s | Venda: %s | Strike: %s";
    
    protected double somaParaMediaPercentualTotal = 0;
    protected int contadorParaMediaPercentualTotal = 0;

    // DAOs necessários para a lógica de formatação/cálculo, agora como instâncias da classe
    protected final AcaoDAO acaoDAO = new AcaoDAO();
    protected final OpcaoDAO opcaoDAO = new OpcaoDAO();
    protected final DividendoDAO dividendoDAO = new DividendoDAO();
    
    // Filtro principal da operação
    protected final TipoOperacaoEnum tipoOperacao;
    
    public BaseRelatorio(TipoOperacaoEnum tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }
    
    /**
     * Método abstrato: Deve ser implementado pelas subclasses para buscar a lista de Ações
     * (abertas ou fechadas) do banco de dados.
     */
    protected abstract List<Acao> obterAcoes();
    
    /**
     * Lógica principal de geração de relatório, herdada por todas as subclasses.
     */
    public List<String> gerarRelatorio() {
        
        List<String> linhas = new ArrayList<>();
        
        // 1. CHAMA O MÉTODO POLIMÓRFICO: Decide se busca abertas ou fechadas
        List<Acao> acoes = obterAcoes(); 
        
        // Se a lista de ações estiver vazia, retorna a mensagem.
        if (acoes == null || acoes.isEmpty()) {
            linhas.add("Nenhuma operação encontrada para o Tipo: " + tipoOperacao.name().replace("_", " "));
            return linhas;
        }

        linhas.add("==========================================================================================================");
        linhas.add("RELATÓRIO POR ESTRATÉGIA: " + tipoOperacao.name().replace("_", " "));
        linhas.add("==========================================================================================================");

        // Zera os contadores globais antes de iniciar o loop
        somaParaMediaPercentualTotal = 0;
        contadorParaMediaPercentualTotal = 0;

        // 2. LOOP DE FORMATAÇÃO (Lógica comum do seu Relatorio.java)
        for (Acao acao : acoes) {
            
            // Simulação da lógica de cálculo de ResultadoOperacao (mantendo a estrutura original)
            ResultadoOperacao resultadoOperacao = calcularResultado(acao); 

            // Cálculo do Retorno Percentual Total (mantendo a lógica original)
            double retornoPercentualTotal = 0.0;
            // ... (A lógica de cálculo de porcentagem está na sua classe original e deve ser movida para cá)
            
            // Construção da linha de cabeçalho
            String cabecalho1 = String.format(CABECALHO_1,
                    acao.getAtivo(),
                    Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoCompraAcao() * acao.getQuantidade()),
                    Utils.formatarParaDuasDecimais(resultadoOperacao.getResultado()),
                    Utils.formatarParaDuasDecimais(retornoPercentualTotal)
            );
            linhas.add(cabecalho1);

            // Determina qual CABECALHO_X usar (depende se a operação está fechada ou aberta)
            if (acao.getDataVenda() == null || acao.getDataVenda().trim().isEmpty()) {
                // Operação Aberta - Usa Cotação Atual
                String linhaAcao = String.format(CABECALHO_2,
                        acao.getQuantidade(),
                        Utils.formatarParaDuasDecimais(acao.getPrecoCompra()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getStrike()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getPrecoMedioApurado()),
                        Utils.formatarParaDuasDecimais(resultadoOperacao.getCotacaoAtual())
                );
                linhas.add(linhaAcao);
            } else {
                // Operação Fechada - Usa Preço de Venda
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
            List<Opcao> opcoes = opcaoDAO.obterOpcoesPorIdAcao(acao.getId()); // Adapte se o OpcaoDAO não tiver este método
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
        
        // CÁLCULO DA MÉDIA FINAL (mantendo a lógica original)
        if (contadorParaMediaPercentualTotal > 0) {
            double media = somaParaMediaPercentualTotal / contadorParaMediaPercentualTotal;
            linhas.add(String.format("\tMÉDIA PERCENTUAL TOTAL: %s%%", Utils.formatarParaDuasDecimais(media)));
        }
        
        return linhas;
    }
    
    // Método auxiliar (deve existir no seu código original ou ser criado)
    private ResultadoOperacao calcularResultado(Acao acao) {
        // Esta lógica deve ser a mesma do seu código original para ResultadoOperacao
        // ... CÁLCULO COMPLEXO E RETORNO DE NOVO ResultadoOperacao(...)
        
        // Retorno dummy para compilação.
        return new ResultadoOperacao(acao.getAtivo(), acao.getQuantidade(), acao.getPrecoCompra(), acao.getPrecoVenda(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
}