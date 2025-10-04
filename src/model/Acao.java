package model;

public class Acao {
    private int id;
    private String ativo;
    private String dataCompra;
    private int quantidade;
    private double precoCompra;
    private String dataVenda;
    private double precoVenda;
    
    public Acao() {
    	
    }
    
	public Acao(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAtivo() {
		return ativo;
	}
	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	public String getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getPrecoCompra() {
		return precoCompra;
	}
	public void setPrecoCompra(double precoCompra) {
		this.precoCompra = precoCompra;
	}
	public String getDataVenda() {
		return dataVenda;
	}
	public void setDataVenda(String dataVenda) {
		this.dataVenda = dataVenda;
	}
	public double getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(double precoVenda) {
		this.precoVenda = precoVenda;
	}
	public String exibir() {
	    return "ID: " + id +
		           " | Ativo: " + ativo +
		           " | Data Compra: " + dataCompra +
		           " | Quantidade: " + quantidade +
		           " | Preço Compra: " + precoCompra +
		           " | Data Venda: " + dataVenda +
		           " | Preço Venda: " + precoVenda;
	}
	public String toString() {
		return ativo;
	}

}