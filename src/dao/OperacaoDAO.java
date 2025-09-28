package dao;

import java.util.List;

import model.Acao;
import model.Opcao;
import model.Operacao;

public class OperacaoDAO {
	
	public static Operacao buscaOperacao(int idAcao)  {
		
		Acao acao = new AcaoDAO().obterAcaoPorId(idAcao);
		
	    List<Opcao> opcoes =  new OpcaoDAO().obterOpcoesPorIdAcao(idAcao);
	    
	    return new Operacao(acao, opcoes);
	}
}
