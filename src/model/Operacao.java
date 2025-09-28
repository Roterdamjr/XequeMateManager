package model;
import java.util.List;

public class Operacao {
    private Acao acao;
    private List<Opcao> opcoes;

    public Operacao(Acao acao, List<Opcao> opcoes) {
        this.acao = acao;
        this.opcoes = opcoes;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
    }



}
