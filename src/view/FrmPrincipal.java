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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmPrincipal extends JFrame {

    private JDesktopPane desktopPane;
    
    private FrmAcoesNaoVendidas frmAcoesNaoVendidas;
    private FrmComprarAcao frmComprarAcao;
    private FrmComprarOpcao frmComprarOpcao;
    private FrmVenderAcao frmVenderAcao;
    private FrmVenderOpcao frmVenderOpcao;
    private FrmDividendo frmDividendo;
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
        JMenuItem itemComprarAcao = new JMenuItem("Comprar Ação");
        JMenuItem itemVenderAcao = new JMenuItem("Vender Ação");
        
        itemComprarAcao.addActionListener(e -> callOnlyOnce(FrmComprarAcao.class.getName()));
        itemVenderAcao.addActionListener(e -> callOnlyOnce(FrmVenderAcao.class.getName()));
        
        menuOperacoes.add(itemComprarAcao);
        menuOperacoes.add(itemVenderAcao);

        JMenu menuVisualizar = new JMenu("Visualizar");
        JMenuItem itemMostrarTabela = new JMenuItem("Ações Não Vendidas");
        itemMostrarTabela.addActionListener(e -> callOnlyOnce(FrmAcoesNaoVendidas.class.getName()));

        menuVisualizar.add(itemMostrarTabela);
        
        menuBar.add(menuOperacoes);
        JMenuItem itemComprarOpcao = new JMenuItem("Comprar Opção");
        menuOperacoes.add(itemComprarOpcao);
        
        JMenuItem itemVenderOpcao = new JMenuItem("Vender Opção");
        menuOperacoes.add(itemVenderOpcao);
        
        JMenuItem itemDividendo = new JMenuItem("Dividendos");
        itemDividendo.addActionListener(e -> callOnlyOnce(FrmDividendo.class.getName()));
        menuOperacoes.add(itemDividendo);
        
        
        itemVenderOpcao.addActionListener(e -> callOnlyOnce(FrmVenderOpcao.class.getName()));
        
        itemComprarOpcao.addActionListener(e -> callOnlyOnce(FrmComprarOpcao.class.getName()));
        menuBar.add(menuVisualizar);
        
        setJMenuBar(menuBar);
        
        JMenu mnNewMenu = new JMenu("Ferramentas");
        menuBar.add(mnNewMenu);
        
        JMenuItem itemCotacoes = new JMenuItem("Atualizar Cotações");
        itemCotacoes.addActionListener(e -> atualizarCotacoes());
        mnNewMenu.add(itemCotacoes);
        
        JMenuItem itemRelatorio = new JMenuItem("Relatorio");
        itemRelatorio.addActionListener(e -> callOnlyOnce(FrmRelatorio.class.getName()));
        mnNewMenu.add(itemRelatorio);
        
        JMenuItem itemDesempenhoMensal = new JMenuItem("Desempenho Mensal");
        itemDesempenhoMensal.addActionListener(e -> callOnlyOnce(FrmDesempenho.class.getName()));
        mnNewMenu.add(itemDesempenhoMensal);
    }
    
    private void callOnlyOnce(String className) {
        JInternalFrame frameToOpen = null;
        
        if (className.equals(FrmAcoesNaoVendidas.class.getName())) {
            if (frmAcoesNaoVendidas == null || frmAcoesNaoVendidas.isClosed()) {
                frmAcoesNaoVendidas = new FrmAcoesNaoVendidas();
                frmAcoesNaoVendidas.setLocation(50, 50);
            }
            frameToOpen = frmAcoesNaoVendidas;
        } else if (className.equals(FrmComprarAcao.class.getName())) {
            if (frmComprarAcao == null || frmComprarAcao.isClosed()) {
                frmComprarAcao = new FrmComprarAcao();
            }
            frameToOpen = frmComprarAcao;
        } else if (className.equals(FrmVenderAcao.class.getName())) {
            if (frmVenderAcao == null || frmVenderAcao.isClosed()) {
                frmVenderAcao = new FrmVenderAcao();
            }
            frameToOpen = frmVenderAcao;
        } else if (className.equals(FrmComprarOpcao.class.getName())) {
            if (frmComprarOpcao == null || frmComprarOpcao.isClosed()) {
                frmComprarOpcao = new FrmComprarOpcao();
            }
            frameToOpen = frmComprarOpcao;
        } else if (className.equals(FrmVenderOpcao.class.getName())) {
            if (frmVenderOpcao == null || frmVenderOpcao.isClosed()) {
                frmVenderOpcao = new FrmVenderOpcao();
            }
            frameToOpen = frmVenderOpcao;

	    } else if (className.equals(FrmDividendo.class.getName())) {
	        if (frmDividendo == null || frmDividendo.isClosed()) {
	        	frmDividendo = new FrmDividendo();
	        }
	        frameToOpen = frmDividendo;
	        
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