package relat;

import java.util.List;

import model.Acao;
import model.TipoOperacaoEnum;

/**
 * Implementa a lógica de busca de dados para Operações Fechadas.
 */
public class RelatorioOperacoesFechadas extends BaseRelatorio {

    public RelatorioOperacoesFechadas(TipoOperacaoEnum tipoOperacao) {
        super(tipoOperacao);
    }

    /**
     * Implementa o método abstrato para buscar apenas ações fechadas.
     * Requer que AcaoDAO tenha um método obterAcoesFechadas(String tipoOperacaoDb).
     */
    @Override
    protected List<Acao> obterAcoes() {
        // Assume que 'acaoDAO' foi inicializado em BaseRelatorio
        return acaoDAO.obterAcoesFechadas(this.tipoOperacao.getDbValue());
    }
}