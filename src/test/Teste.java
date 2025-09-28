package test;


import java.util.List;

import dao.OperacaoDAO;
import model.Acao;
import model.Opcao;
import model.Operacao;


public class Teste {

	 public static void main(String[] args) {
		Operacao op =new Teste().buscaOperacao(1);
		Acao acao = op.getAcao();
		List<Opcao> opcoes = op.getOpcoes();
		
		System.out.println(op.getAcao());

	    // Imprime cada opção em uma linha
	    if (opcoes != null && !opcoes.isEmpty()) {
	        for (Opcao o : opcoes) {
	            System.out.println(o);
	        }
	    } else {
	        System.out.println("Nenhuma opção encontrada.");
	    }
	}
	 
    public Operacao buscaOperacao(int idAcao)  {
    	return new OperacaoDAO().buscaOperacao(idAcao);

    }
}
