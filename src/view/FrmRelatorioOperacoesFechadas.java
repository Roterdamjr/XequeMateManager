package view;

import model.TipoOperacaoEnum;
import relat.BaseRelatorio;
import relat.RelatorioOperacoesFechadas;

/**
 * Frame concreto para exibir Relatórios de Operações FECHADAS.
 */
public class FrmRelatorioOperacoesFechadas extends BaseFrmRelatorio {

    private static final long serialVersionUID = 1L;

    public FrmRelatorioOperacoesFechadas() {
        // Chama o novo construtor da BaseFrmRelatorio passando o título
        super("Relatório de Operações Fechadas"); 
    }

    @Override
    protected BaseRelatorio getBaseRelatorio(TipoOperacaoEnum tipoOperacao) {
        return new RelatorioOperacoesFechadas(tipoOperacao);
    }
}