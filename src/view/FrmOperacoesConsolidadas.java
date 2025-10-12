package view;


import view.paneles.PainelComprarAcao;
import view.paneles.PainelVenderAcao;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import view.paneles.PainelComprarOpcao;
import view.paneles.PainelVenderOpcao;

public class FrmOperacoesConsolidadas extends JInternalFrame {

	private String tipoOperacao="DIV";
	
	private PainelComprarAcao painelComprarAcao; 
	private PainelVenderAcao painelVenderAcao;
	private PainelComprarOpcao painelComprarOpcao;
    private PainelVenderOpcao painelVenderOpcao;	
    private JTabbedPane tabbedPane;


    public FrmOperacoesConsolidadas() {
        setTitle("Operações - Compra/Venda Unificada");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane);

        // Adiciona as 4 telas como abas
        tabbedPane.addTab("Comprar Ação", criarPainelComprarAcao());
        tabbedPane.addTab("Vender Ação", criarPainelVenderAcao());
        tabbedPane.addTab("Comprar Opção", criarPainelComprarOpcao());
        tabbedPane.addTab("Vender Opção", criarPainelVenderOpcao());
        
        // Inicializa as janelas
        limparPainelComprarAcao();
        limparPainelVenderAcao();
        limparPainelComprarOpcao();
        limparPainelVenderOpcao();
    }
    
    private JPanel criarPainelComprarAcao() {
        painelComprarAcao = new PainelComprarAcao();
        return painelComprarAcao;
    }
    
    private JPanel criarPainelVenderAcao() {
        painelVenderAcao = new PainelVenderAcao();
        return painelVenderAcao;
    }

    private void limparPainelComprarAcao() {
        if (painelComprarAcao != null) {
            painelComprarAcao.limparPainel();
        }
    }
    
    private void limparPainelVenderAcao() {
        if (painelVenderAcao != null) {
            painelVenderAcao.limparPainel();
        }
    }

    private JPanel criarPainelVenderOpcao() {
        painelVenderOpcao = new PainelVenderOpcao();
        return painelVenderOpcao;
    }

    private JPanel criarPainelComprarOpcao() {
        painelComprarOpcao = new PainelComprarOpcao();
        return painelComprarOpcao;
    }

    private void limparPainelComprarOpcao() {
        if (painelComprarOpcao != null) {
            painelComprarOpcao.limparPainel();
        }
    }
    private void limparPainelVenderOpcao() {
        if (painelVenderOpcao != null) {
            painelVenderOpcao.limparPainel();
        }
    }

   
}