package model;

public class Opcao {
    private int id;
    private String dataVenda;
    private String opcao;
    private double quantidade;
    private double precoVenda;
    private double strike;
    private int idAcao;
    private String dataCompra;
    private double precoCompra;
    // ... Construtor e Getters/Setters

    // ADICIONADO: Para exibir corretamente na JComboBox
    @Override
    public String toString() {
        return opcao;
    }

    // Seus getters e setters existentes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOpcao() { return opcao; }
    public void setOpcao(String opcao) { this.opcao = opcao; }
    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }
    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
    public String getDataVenda() { return dataVenda; }
    public void setDataVenda(String dataVenda) { this.dataVenda = dataVenda; }
    public double getStrike() { return strike; }
    public void setStrike(double strike) { this.strike = strike; }
    public int getIdAcao() { return idAcao; }
    public void setIdAcao(int idAcao) { this.idAcao = idAcao; }
    public String getDataCompra() { return dataCompra; }
    public void setDataCompra(String dataCompra) { this.dataCompra = dataCompra; }
    public double getPrecoCompra() { return precoCompra; }
    public void setPrecoCompra(double precoCompra) { this.precoCompra = precoCompra; }
 
}