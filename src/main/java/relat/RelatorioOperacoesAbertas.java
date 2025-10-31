package relat;

import java.util.List;

import model.Acao;
import model.TipoOperacaoEnum;

/**
 * Implementa a lógica de busca de dados para Operações Abertas.
 */
public class RelatorioOperacoesAbertas extends BaseRelatorio {

    public RelatorioOperacoesAbertas(TipoOperacaoEnum tipoOperacao) {
        super(tipoOperacao);
    }
    
    @Override
    protected List<Acao> obterAcoes() {
        return acaoDAO.obterAcoesAbertasOrdenadasPorData(this.tipoOperacao.getDbValue());
    }
    
    @Override
    protected boolean isOperacaoAberta() {
        return true; 
    }
}