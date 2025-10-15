package view;

import java.awt.Color;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import util.CotacaoManager;

public class FrmPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JDesktopPane desktopPane;
    
    private FrmOperacoesConsolidadas frmOperacoesConsolidadas;
    private FrmRelatorio frmRelatorio;
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
        JMenuItem itemREgMovimento = new JMenuItem("Registrar Movimento");
        menuOperacoes.add(itemREgMovimento);
        itemREgMovimento.addActionListener(e -> callOnlyOnce(FrmOperacoesConsolidadas.class.getName()));

        JMenu menuVisualizar = new JMenu("Visualizar");
        
        JMenuItem itemRelatorio = new JMenuItem("Relatorio");
        menuVisualizar.add(itemRelatorio);
        itemRelatorio.addActionListener(e -> callOnlyOnce(FrmRelatorio.class.getName()));
        
        menuBar.add(menuOperacoes);
        
        

        menuBar.add(menuVisualizar);
        
        JMenuItem itemDesempenhoMensal = new JMenuItem("Desempenho Mensal");
        menuVisualizar.add(itemDesempenhoMensal);
        itemDesempenhoMensal.addActionListener(e -> callOnlyOnce(FrmDesempenho.class.getName()));
        
        setJMenuBar(menuBar);
        
        JMenu mnNewMenu = new JMenu("Ferramentas");
        menuBar.add(mnNewMenu);
        
        JMenuItem itemCotacoes = new JMenuItem("Atualizar Cotações");
        itemCotacoes.addActionListener(e -> atualizarCotacoes());
        mnNewMenu.add(itemCotacoes);
    }
    
    private void callOnlyOnce(String className) {
        JInternalFrame frameToOpen = null;
        
        if (className.equals(FrmOperacoesConsolidadas.class.getName())) {
            if (frmOperacoesConsolidadas == null || frmOperacoesConsolidadas.isClosed()) {
            	frmOperacoesConsolidadas = new FrmOperacoesConsolidadas();
            	frmOperacoesConsolidadas.setLocation(50, 50);
            }
            frameToOpen = frmOperacoesConsolidadas;

	    } else if (className.equals(FrmRelatorio.class.getName())) {
	        if (frmRelatorio == null || frmRelatorio.isClosed()) {
	        	frmRelatorio = new FrmRelatorio();
	        }
	        frameToOpen = frmRelatorio;
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
    			"Sucesso", 
    			JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmPrincipal().setVisible(true);
        });
    }
}