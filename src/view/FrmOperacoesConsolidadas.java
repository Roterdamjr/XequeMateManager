package view;

import view.paneles.PainelComprarAcao;
import view.paneles.PainelVenderAcao;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.TipoOperacaoEnum;
import view.paneles.PainelComprarOpcao;
import view.paneles.PainelDividendo;
import view.paneles.PainelVenderOpcao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton; 
import javax.swing.ButtonGroup; 
import java.awt.Color;

public class FrmOperacoesConsolidadas extends JInternalFrame implements OperacoesListener,ActionListener{

	private static final long serialVersionUID = 1L;

	private String tipoOperacao;
	private PainelComprarAcao painelComprarAcao; 
	private PainelVenderAcao painelVenderAcao;
	private PainelComprarOpcao painelComprarOpcao;
    private PainelVenderOpcao painelVenderOpcao;	
    private JTabbedPane tabbedPane;
    private PainelDividendo painelDividendo;
    private JPanel panelBotoes;

    private JRadioButton rbDividendo3X;
    private JRadioButton rbGanhaGanha;
    private JRadioButton rb3x1;
    private JRadioButton rbEstrategica;
    private ButtonGroup grupoEstrategias; 
    
	public String getTipoOperacao() {return tipoOperacao;}
	public void setTipoOperacao(String tipoOperacao) {	this.tipoOperacao = tipoOperacao;}
	
    public FrmOperacoesConsolidadas() {
        setTitle("Operações - Compra/Venda Unificada");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        
        setBounds(100, 100, 600, 400);
        {
        	panelBotoes = new JPanel();
            panelBotoes.setLayout(new FlowLayout(FlowLayout.LEFT));
            panelBotoes.setBackground(Color.LIGHT_GRAY); 
            
        	getContentPane().add(panelBotoes, BorderLayout.NORTH);

        	criarRadioButtons(); 
        }

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("Comprar Ação", criarPainelComprarAcao());
        tabbedPane.addTab("Vender Opção", criarPainelVenderOpcao());
        tabbedPane.addTab("Vender Ação", criarPainelVenderAcao());
        tabbedPane.addTab("Comprar Opção", criarPainelComprarOpcao());
        tabbedPane.addTab("Dividendo", criarPainelDividendo());
        
        setTipoOperacao(TipoOperacaoEnum.DIVIDENDO3X.getDbValue());
        recarregarTodasAsAbas();
        
    }
    
    private void criarRadioButtons() {
        grupoEstrategias = new ButtonGroup();

        rbDividendo3X = new JRadioButton("Dividendo3X");
        rbGanhaGanha = new JRadioButton("GanhaGanha");
        rb3x1 = new JRadioButton("3x1");
        rbEstrategica = new JRadioButton("Estrategica");
        
        Color corPadrao = new Color(245, 245, 245); // Um cinza bem clarinho como padrão
        panelBotoes.setBackground(corPadrao);
        
        rbDividendo3X.addActionListener(this);
        rbGanhaGanha.addActionListener(this);
        rb3x1.addActionListener(this);
        rbEstrategica.addActionListener(this);
        
        grupoEstrategias.add(rbDividendo3X);
        grupoEstrategias.add(rbGanhaGanha);
        grupoEstrategias.add(rb3x1);
        grupoEstrategias.add(rbEstrategica);
         
        rbDividendo3X.setSelected(true);
        
        mudarCorPainel(Color.LIGHT_GRAY);

        panelBotoes.add(rbDividendo3X);
        panelBotoes.add(rbGanhaGanha);
        panelBotoes.add(rb3x1);
        panelBotoes.add(rbEstrategica);
    }

    private JPanel criarPainelComprarAcao() {
        painelComprarAcao = new PainelComprarAcao(this);
        return painelComprarAcao;
    }
    
    private JPanel criarPainelVenderAcao() {
        painelVenderAcao = new PainelVenderAcao(this);
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
        painelDividendo = new PainelDividendo(this);
        return painelDividendo;
    }
    
    public void recarregarTodasAsAbas() {
        // 1. Painel Comprar Ação
        if (painelComprarAcao != null) {
            painelComprarAcao.limparPainel();
            // Nenhuma chamada de carregamento, pois esta é a aba de compra
        }
        
        // 2. Painel Vender Opção (Corrigido: Lógica de recarga contida aqui)
        if (painelVenderOpcao != null) {
            painelVenderOpcao.limparPainel();
            painelVenderOpcao.carregarAcoesVenda(); 
        }
        
        // 3. Painel Vender Ação
        if (painelVenderAcao != null) {
            painelVenderAcao.limparPainel();
            painelVenderAcao.carregarAcoesVenda();
        }
        
        // 4. Painel Comprar Opção
        if (painelComprarOpcao != null) {
            painelComprarOpcao.limparPainel();
            painelComprarOpcao.carregarOpcoesCompra();
        }
        
        // 5. Painel Dividendo
        if (painelDividendo != null) {
            painelDividendo.limparPainel();
            painelDividendo.carregarAcoes();
        }
    }

    @Override
    public void onOperacaoSalvaSucesso() {
        recarregarTodasAsAbas();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        Color corSelecionada = new Color(245, 245, 245); // Cor padrão se for o mesmo que a cor inicial

        if (e.getSource() == rbDividendo3X) {
            corSelecionada = Color.LIGHT_GRAY; // Lavanda Claro (Exemplo) 
            setTipoOperacao(TipoOperacaoEnum.DIVIDENDO3X.getDbValue());
        } else if (e.getSource() == rbGanhaGanha) {
            corSelecionada = Color.decode("#CCFFCC"); // Verde Menta (Exemplo)
            setTipoOperacao(TipoOperacaoEnum.GANHA_GANHA.getDbValue() );
        } else if (e.getSource() == rb3x1) {
            corSelecionada = Color.decode("#FFCCCC"); // Rosa Claro (Exemplo)
            setTipoOperacao(TipoOperacaoEnum.TRES_PRA_UM.getDbValue());
        } else if (e.getSource() == rbEstrategica) {
            corSelecionada = Color.decode("#FFFFCC"); // Amarelo Pastel (Exemplo)
            setTipoOperacao(TipoOperacaoEnum.ESTRATEGICA.getDbValue());
        }
        
        recarregarTodasAsAbas();
        mudarCorPainel(corSelecionada);	
	}
	
    private void mudarCorPainel(Color novaCor) {
        panelBotoes.setBackground(novaCor);

        rbDividendo3X.setBackground(novaCor);
        rbGanhaGanha.setBackground(novaCor);
        rb3x1.setBackground(novaCor);
        rbEstrategica.setBackground(novaCor);

        panelBotoes.revalidate();
        panelBotoes.repaint();
    }
}