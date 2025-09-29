package test;

import java.util.List;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.OperacaoDAO;
import model.Acao;
import model.Opcao;
import model.Operacao;

public class Teste {

	 public static void main(String[] args) {
		Operacao op =new Teste().buscaOperacao(1);
		//new Teste().imprime(op);
		new Teste().efetuaCalculos(op);
	}
	
	public void efetuaCalculos(Operacao operacao) {
		List<Opcao> opcoes= operacao.getOpcoes();
		Acao acao = operacao.getAcao();
		
		
	    if (opcoes != null && !opcoes.isEmpty()) {
	    	Double acum = 0.0;
	        for (Opcao opcao : opcoes) {
	        	acum += opcao.getPrecoCompra() - opcao.getPrecoVenda();

	        }
	        Double precoMedio =  acao.getPrecoCompra() + acum ;
	        Double cotacao = new CotacaoDAO().buscarCotacaoPorAtivo(acao.getAtivo());
	        int quantidade = acao.getQuantidade();
	        System.out.println("precoMedio:" + precoMedio +", Cotação "+cotacao + ", Qtde "+ quantidade);

	    } else {
	        System.out.println("Nenhuma opção encontrada.");
	    }
	}
	
    public Operacao buscaOperacao(int idAcao)  {
    	return new OperacaoDAO().buscaOperacao(idAcao);
    }
    
	public void imprime(Operacao op) {List<Opcao> opcoes = op.getOpcoes();
		
		System.out.println(op.getAcao());

	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao o : opcoes) {
	            System.out.println(o);
	        }
	    } else {
	        System.out.println("Nenhuma opção encontrada.");
	    }
	 }
}
