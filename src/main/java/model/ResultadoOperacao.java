package model;

/**
 * Classe para armazenar os dados resumidos de uma operação de ações/opções/dividendos.
 */
public class ResultadoOperacao {

    private String ativo;
    private int quantidade;
    private double precoCompraAcao;
    private double precoVendaAcao;
    private double precoMedioApurado;
    private double strike;
    private double cotacaoAtual;
    private double resultado;
    private double totalOpcoes;
    private double totalDividendos;

    public ResultadoOperacao(String ativo, int quantidade, double precoCompraAcao, double precoVendaAcao,
                             double precoMedioApurado,double strike, double cotacaoAtual, double resultado,
                             double totalOpcoes, double totalDividendos) {
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoCompraAcao = precoCompraAcao;
        this.precoVendaAcao = precoVendaAcao;
        this.precoMedioApurado = precoMedioApurado;
        this.strike = strike;
        this.cotacaoAtual = cotacaoAtual;
        this.resultado = resultado;
        this.totalOpcoes = totalOpcoes;
        this.totalDividendos = totalDividendos;
    }

    // --- GETTERS ---
    public String getAtivo() { return ativo; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoCompraAcao() { return precoCompraAcao; }
    public double getPrecoVendaAcao() { return precoVendaAcao; }
    public double getPrecoMedioApurado() { return precoMedioApurado; }
    public double getCotacaoAtual() { return cotacaoAtual; }
    public double getResultado() { return resultado; }
    public double getTotalOpcoes() { return totalOpcoes; }
    public double getTotalDividendos() { return totalDividendos; }

	public double getStrike() {
		return strike;
	}
    
    // --- SETTERS --- (Se forem necessários, adicione-os aqui)
}