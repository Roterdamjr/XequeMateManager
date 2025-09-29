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

    public FrmPrincipal() {
        setTitle("XequeMate Investimentos - Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(1000, 700); 
        setLocationRelativeTo(null); 
        
        setupMenuBar();
        setupDesktop();
        
        // Abre a tabela de ações não vendidas por padrão
        callOnlyOnce(FrmAcoesNaoVendidas.class.getName());
    }
    
    private void setupDesktop() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.LIGHT_GRAY);
        this.setContentPane(desktopPane);
    }
 
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuAcoes = new JMenu("Ações");
        JMenuItem itemComprarAcao = new JMenuItem("Comprar Ação");
        JMenuItem itemVenderAcao = new JMenuItem("Vender Ação");
        
        itemComprarAcao.addActionListener(e -> callOnlyOnce(FrmComprarAcao.class.getName()));
        itemVenderAcao.addActionListener(e -> callOnlyOnce(FrmVenderAcao.class.getName()));
        
        menuAcoes.add(itemComprarAcao);
        menuAcoes.add(itemVenderAcao);
        
        JMenu menuOpcoes = new JMenu("Opções");
        JMenuItem itemComprarOpcao = new JMenuItem("Comprar Opção");
        JMenuItem itemVenderOpcao = new JMenuItem("Vender Opção");

        itemComprarOpcao.addActionListener(e -> callOnlyOnce(FrmComprarOpcao.class.getName()));
        itemVenderOpcao.addActionListener(e -> callOnlyOnce(FrmVenderOpcao.class.getName()));

        menuOpcoes.add(itemComprarOpcao);
        menuOpcoes.add(itemVenderOpcao);

        JMenu menuVisualizar = new JMenu("Visualizar");
        JMenuItem itemMostrarTabela = new JMenuItem("Ações Não Vendidas");
        itemMostrarTabela.addActionListener(e -> callOnlyOnce(FrmAcoesNaoVendidas.class.getName()));

        JMenuItem itemAtualizar = new JMenuItem("Atualizar Tabela");
        itemAtualizar.addActionListener(e -> updateAcoesTableData());

        menuVisualizar.add(itemMostrarTabela);
        menuVisualizar.add(itemAtualizar);
        
        menuBar.add(menuAcoes);
        menuBar.add(menuOpcoes);
        menuBar.add(menuVisualizar);
        
        setJMenuBar(menuBar);
        
        JMenu mnNewMenu = new JMenu("Ferramentas");
        menuBar.add(mnNewMenu);
        
        JMenuItem itemCotacoes = new JMenuItem("Atualizar Cotações");
        itemCotacoes.addActionListener(e -> atualizarCotacoes());
        mnNewMenu.add(itemCotacoes);
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
    
    private void updateAcoesTableData() {
        if (frmAcoesNaoVendidas != null && !frmAcoesNaoVendidas.isClosed()) {
            frmAcoesNaoVendidas.loadAcoesData();
        } else {
            JOptionPane.showMessageDialog(this, "A tabela 'Ações Não Vendidas' não está aberta.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
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