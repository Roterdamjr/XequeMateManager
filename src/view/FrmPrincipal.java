package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import util.CotacaoManager;

public class FrmPrincipal extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JDesktopPane desktopPane;
    
    private FrmOperacoesConsolidadas frmOperacoesConsolidadas;
    private FrmRelatorioOperacoesAbertas frmRelatorioAbertas; 
    private FrmRelatorioOperacoesFechadas frmRelatorioFechadas; 
    private FrmDesempenho frmDesempenho;
    
    public FrmPrincipal() {
        setTitle("XequeMate Investimentos - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(1000, 700); 
        setLocationRelativeTo(null); 
        
        setupMenuBar();
        setupDesktop();
        
    }
    
    private void setupDesktop() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.LIGHT_GRAY);
        this.setContentPane(desktopPane);
    }
 
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemRegistrarOperacao = new JMenuItem("Registrar");
        itemRegistrarOperacao.addActionListener(this);
        menuOperacoes.add(itemRegistrarOperacao);
        
        JMenu menuRelatorios = new JMenu("Relatórios");
        // NOVOS ITENS DE MENU
        JMenuItem itemRelatorioAbertas = new JMenuItem("Operações Abertas");
        itemRelatorioAbertas.addActionListener(this);
        JMenuItem itemRelatorioFechadas = new JMenuItem("Operações Fechadas");
        itemRelatorioFechadas.addActionListener(this);
        menuRelatorios.add(itemRelatorioAbertas);
        menuRelatorios.add(itemRelatorioFechadas);
        
        menuBar.add(menuOperacoes);
        menuBar.add(menuRelatorios);
        
        JMenuItem itemDesempenho = new JMenuItem("Desempenho");
        menuRelatorios.add(itemDesempenho);
        itemDesempenho.addActionListener(this);
        
        this.setJMenuBar(menuBar);
        
        JMenu mnuEerramentas = new JMenu("Eerramentas");
        menuBar.add(mnuEerramentas);
        
        JMenuItem itemAtualizarCotacoes = new JMenuItem("Atualizar Cotações");
        mnuEerramentas.add(itemAtualizarCotacoes);
        itemAtualizarCotacoes.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand().equals("Atualizar Cotações")) {
    		atualizarCotacoes();
    	} else {
    		String className = "";
    		if (e.getActionCommand().equals("Registrar")) {
    			className = FrmOperacoesConsolidadas.class.getName();
    		} else if (e.getActionCommand().equals("Operações Abertas")) { 
    			className = FrmRelatorioOperacoesAbertas.class.getName();
    		} else if (e.getActionCommand().equals("Operações Fechadas")) { 
    			className = FrmRelatorioOperacoesFechadas.class.getName();
    		} else if (e.getActionCommand().equals("Desempenho")) {
    			className = FrmDesempenho.class.getName();
    		}
    		
    		abrirFrame(className);
    	}
    }

    private void abrirFrame(String className) {
        JInternalFrame frameToOpen = null;

        if (className.equals(FrmOperacoesConsolidadas.class.getName())) {
            if (frmOperacoesConsolidadas == null || frmOperacoesConsolidadas.isClosed()) {
            	frmOperacoesConsolidadas = new FrmOperacoesConsolidadas();
            	frmOperacoesConsolidadas.setLocation(50, 50);
            }
            frameToOpen = frmOperacoesConsolidadas;

	    } else if (className.equals(FrmRelatorioOperacoesAbertas.class.getName())) { 
	        if (frmRelatorioAbertas == null || frmRelatorioAbertas.isClosed()) {
	        	frmRelatorioAbertas = new FrmRelatorioOperacoesAbertas();
	        }
	        frameToOpen = frmRelatorioAbertas;
	    } else if (className.equals(FrmRelatorioOperacoesFechadas.class.getName())) { 
	        if (frmRelatorioFechadas == null || frmRelatorioFechadas.isClosed()) {
	        	frmRelatorioFechadas = new FrmRelatorioOperacoesFechadas();
	        }
	        frameToOpen = frmRelatorioFechadas;
	    } else if (className.equals(FrmDesempenho.class.getName())) {
	        if (frmDesempenho == null || frmDesempenho.isClosed()) {
	        	frmDesempenho = new FrmDesempenho();
	        }
	        frameToOpen = frmDesempenho;        
	    }
        
        
        if (frameToOpen != null && frameToOpen.getParent() == null) {
            desktopPane.add(frameToOpen);
        }
        
        if (frameToOpen != null) {
            frameToOpen.setVisible(true);
            desktopPane.getDesktopManager().activateFrame(frameToOpen);
            try {
                frameToOpen.setSelected(true);
            } catch (java.beans.PropertyVetoException ignored) {}
        }
    }
    
    private void atualizarCotacoes() {
    	CotacaoManager.atualizarCotacoesNoDatabase();
    	JOptionPane.showMessageDialog(this, 
    			"Cotações atualizadas com sucesso!", 
    			"Atualização Concluída", 
    			JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmPrincipal().setVisible(true);
        });
    }
}