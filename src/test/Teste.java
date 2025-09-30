package test;

import java.util.List;

import dao.AcaoDAO;
import dao.CotacaoDAO;
import dao.OpcaoDAO;
import dao.OperacaoDAO;
import model.Acao;
import model.Opcao;
import model.Operacao;
import util.Relatorio;
import util.ValidatorUtils;

public class Teste {

	 public static void main(String[] args) {
		 Relatorio.exibirResumoOperacoesNaoVendidas();
/*
			List<Acao> acoesNaVendidas = new AcaoDAO().obterAcoesNaoVendidas();
			for (Acao acao : acoesNaVendidas) {
				Operacao op = new OperacaoDAO().buscaOperacao(acao.getId());
				System.out.println(acao);
			}
			*/
	}
	

    
    
}
