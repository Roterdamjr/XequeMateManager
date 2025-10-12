package view.paneles;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.AcaoDAO;
import dao.DividendoDAO; // Presumindo que você terá um DAO para Dividendo
import model.Acao;
import model.Dividendo; // Presumindo que você terá um modelo Dividendo
import util.Utils;

public class PainelDividendo extends JPanel {

    private final AcaoDAO acaoDAO = new AcaoDAO();
    private final DividendoDAO dividendoDAO = new DividendoDAO();
    
    private JComboBox<Acao> cmbAcao; 
    private JTextField txtValor;
    private Acao acaoSelecionada = null;

    public PainelDividendo() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(3, 1, 0, 0));
        
        // 1. Ação
        JPanel panelAcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcao.add(new JLabel("Ação"));
        cmbAcao = new JComboBox<>();
        cmbAcao.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelAcao.add(cmbAcao);
        this.add(panelAcao);
        
        // 2. Valor
        JPanel panelValor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelValor.add(new JLabel("Valor Recebido"));
        txtValor = new JTextField(10);
        panelValor.add(txtValor);
        this.add(panelValor);
        
        // 3. Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar"); 
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        panelBotoes.add(btnSalvar);
        this.add(panelBotoes);
        
        limparPainel();
    }
    

    public void limparPainel() {
        carregarAcoes();
        txtValor.setText("");
    }
    
    public void carregarAcoes() {
        try {
            List<Acao> acoes = acaoDAO.obterAcoesAbertas(); // Método de exemplo
            cmbAcao.removeAllItems();
            
            for (Acao acao : acoes) {
                cmbAcao.addItem(acao);
            }
            
            if (!acoes.isEmpty()) {
                cmbAcao.setSelectedIndex(0);
                acaoSelecionada = acoes.get(0);
            } else {
                acaoSelecionada = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a lista de ações: " + e.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cmdSalvar_Click() {
        // Lógica de Salvar Dividendo, migrada de FrmDividendo.java
        
        String valorText = txtValor.getText().trim();

        if (!Utils.isNumeric(valorText)) {
            JOptionPane.showMessageDialog(this, 
                "Insira um Valor numérico válido",   "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtValor.requestFocusInWindow();
            return;
        }
        
        try {
            acaoSelecionada = (Acao) cmbAcao.getSelectedItem();
            if (acaoSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Nenhuma ação selecionada.", "Erro de Seleção", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            double valorDividendo = Double.parseDouble(valorText.replace(",", "."));
            
            Dividendo novoDividendo = new Dividendo(acaoSelecionada.getId(), valorDividendo);
            dividendoDAO.inserir(novoDividendo); 

            limparPainel();
            
            JOptionPane.showMessageDialog(this, 
                "Dividendo registrado com sucesso!",  "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar o dividendo: " + ex.getMessage(),  "Erro de Banco de Dados", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}