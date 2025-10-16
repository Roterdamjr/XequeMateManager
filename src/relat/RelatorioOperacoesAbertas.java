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
        // Assume que 'acaoDAO' foi inicializado em BaseRelatorio
        // Assume que o método em AcaoDAO aceita o valor do ENUM para filtrar
        return acaoDAO.obterAcoesAbertas(this.tipoOperacao.getDbValue());
    }
}