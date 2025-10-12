package view;


import view.paneles.PainelComprarAcao;
import view.paneles.PainelVenderAcao;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import view.paneles.PainelComprarOpcao;
import view.paneles.PainelDividendo;
import view.paneles.PainelVenderOpcao;

public class FrmOperacoesConsolidadas extends JInternalFrame implements OperacoesListener{

	private static final long serialVersionUID = 1L;

	private String tipoOperacao="DIV";
	
	private PainelComprarAcao painelComprarAcao; 
	private PainelVenderAcao painelVenderAcao;
	private PainelComprarOpcao painelComprarOpcao;
    private PainelVenderOpcao painelVenderOpcao;	
    private JTabbedPane tabbedPane;
    private PainelDividendo painelDividendo;


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

        tabbedPane.addTab("Comprar Ação", criarPainelComprarAcao());
        tabbedPane.addTab("Vender Opção", criarPainelVenderOpcao());
        tabbedPane.addTab("Vender Ação", criarPainelVenderAcao());
        tabbedPane.addTab("Comprar Opção", criarPainelComprarOpcao());
        tabbedPane.addTab("Dividendo", criarPainelDividendo());
        
        limparTodasAsAbas();
    }
    
    private JPanel criarPainelComprarAcao() {
        painelComprarAcao = new PainelComprarAcao(this);
        return painelComprarAcao;
    }
    
    private JPanel criarPainelVenderAcao() {
        painelVenderAcao = new PainelVenderAcao();
        return painelVenderAcao;
    }

    private JPanel criarPainelVenderOpcao() {
        painelVenderOpcao = new PainelVenderOpcao(this);
        return painelVenderOpcao;
    }

    private JPanel criarPainelComprarOpcao() {
        painelComprarOpcao = new PainelComprarOpcao();
        return painelComprarOpcao;
    }
    
    private JPanel criarPainelDividendo() {
        painelDividendo = new PainelDividendo();
        return painelDividendo;
    }
    
    public void limparTodasAsAbas() {
        if (painelComprarAcao != null) {
            painelComprarAcao.limparPainel();
            painelVenderOpcao.carregarAcoesVenda();
        }
        if (painelVenderOpcao != null) {
            painelVenderOpcao.limparPainel();
            painelComprarOpcao.carregarOpcoesCompra();
        }
        if (painelVenderAcao != null) {
            painelVenderAcao.limparPainel();
            painelVenderAcao.carregarAcoesVenda();
        }
        if (painelComprarOpcao != null) {
            painelComprarOpcao.limparPainel();
        }
        if (painelDividendo != null) {
            painelDividendo.limparPainel();
            painelDividendo.carregarAcoes();
        }
    }

    @Override
    public void onOperacaoSalvaSucesso() {
        limparTodasAsAbas();
    }
   
}