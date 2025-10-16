package view;

import model.TipoOperacaoEnum;
import relat.BaseRelatorio;
import relat.RelatorioOperacoesAbertas;

/**
 * Frame concreto para exibir Relatórios de Operações ABERTAS.
 */
public class FrmRelatorioOperacoesAbertas extends BaseFrmRelatorio {

    private static final long serialVersionUID = 1L;

    public FrmRelatorioOperacoesAbertas() {
        // Chama o novo construtor da BaseFrmRelatorio passando o título
        super("Relatório de Operações Abertas"); 
    }

    @Override
    protected BaseRelatorio getBaseRelatorio(TipoOperacaoEnum tipoOperacao) {
        return new RelatorioOperacoesAbertas(tipoOperacao);
    }
}