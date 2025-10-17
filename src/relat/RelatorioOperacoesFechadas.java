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

    @Override
    protected List<Acao> obterAcoes() {
        return acaoDAO.obterAcoesFechadasOrdenadasPorData(this.tipoOperacao.getDbValue());
    }
    
    @Override
    protected boolean isOperacaoAberta() {
        return false; 
    }
}