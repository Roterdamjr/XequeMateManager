package model;
public class Dividendo {
    private int id;
    private int idAcao;  // FK para a ação
    private double valor;


    public Dividendo() {}

    public Dividendo( int idAcao, double valor) {
        this.idAcao = idAcao;
        this.valor = valor;
    }


    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAcao() { return idAcao; }
    public void setIdAcao(int idAcao) { this.idAcao = idAcao; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }


    @Override
    public String toString() {
        return "Dividendo [id=" + id + ", idAcao=" + idAcao +
                ", valor=" + valor  +"]";
    }
}
