package model;

/**
 * Classe para armazenar os dados resumidos de uma operação de ações/opções/dividendos.
 */
public class ResultadoOperacao {

    private String ativo;
    private int quantidade;
    private double precoCompraAcao;
    private double strike;
    private double precoMedioApurado;
    private double cotacaoAtual;
    private double resultado;
    private double patrimonioAtual;
    private double totalOpcoes;
    private double totalDividendos;

    public ResultadoOperacao(String ativo, int quantidade, double precoCompraAcao, double strike,
                             double precoMedioApurado, double cotacaoAtual, double resultado,
                             double patrimonioAtual, double totalOpcoes, double totalDividendos) {
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoCompraAcao = precoCompraAcao;
        this.strike = strike;
        this.precoMedioApurado = precoMedioApurado;
        this.cotacaoAtual = cotacaoAtual;
        this.resultado = resultado;
        this.patrimonioAtual = patrimonioAtual;
        this.totalOpcoes = totalOpcoes;
        this.totalDividendos = totalDividendos;
    }

    // --- GETTERS ---
    public String getAtivo() { return ativo; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoCompraAcao() { return precoCompraAcao; }
    public double getStrike() { return strike; }
    public double getPrecoMedioApurado() { return precoMedioApurado; }
    public double getCotacaoAtual() { return cotacaoAtual; }
    public double getResultado() { return resultado; }
    public double getPatrimonioAtual() { return patrimonioAtual; }
    public double getTotalOpcoes() { return totalOpcoes; }
    public double getTotalDividendos() { return totalDividendos; }
    
    // --- SETTERS --- (Se forem necessários, adicione-os aqui)
}