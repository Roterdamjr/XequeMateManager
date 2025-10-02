package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox; // Para o campo Ação
import javax.swing.DefaultComboBoxModel; // Para o modelo do ComboBox
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Arrays; // Usado para o mock de dados

import dao.AcaoDAO;
import dao.DividendoDAO; // Presumindo que você terá um DAO para Dividendo
import model.Acao;
import model.Dividendo; // Presumindo que você terá um modelo Dividendo
import util.ValidatorUtils; // Importa a classe de utilidade

public class FrmDividendo extends JInternalFrame {

    private JPanel contentPane;
    private JComboBox<Acao> cmbAcao; 
    private JTextField txtValor;
    private AcaoDAO acaoDAO = new AcaoDAO();
    private Acao acaoSelecionada = null;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FrmDividendo frame = new FrmDividendo();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FrmDividendo() {
        setClosable(true);
        setTitle("Registrar Dividendo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 378, 250); 
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(3, 1, 0, 0)); // 3 linhas: Ação, Valor, Botões
        
        // --- Painel Ação (ComboBox) ---
        JPanel panelAcao = new JPanel();
        FlowLayout flowLayout_Acao = (FlowLayout) panelAcao.getLayout();
        flowLayout_Acao.setAlignment(FlowLayout.LEFT);
        contentPane.add(panelAcao);
        
        JLabel lblAcao = new JLabel("Ação");
        panelAcao.add(lblAcao);
        
        cmbAcao = new JComboBox<>();
        cmbAcao.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelAcao.add(cmbAcao);
        
        // --- Painel Valor ---
        JPanel panelValor = new JPanel();
        FlowLayout flowLayout_Valor = (FlowLayout) panelValor.getLayout();
        flowLayout_Valor.setAlignment(FlowLayout.LEFT);
        contentPane.add(panelValor);
        
        JLabel lblValor = new JLabel("Valor (R$)");
        panelValor.add(lblValor);
        
        txtValor = new JTextField();
        txtValor.setColumns(10);
        panelValor.add(txtValor);
        
        // --- Painel Botões ---
        JPanel panelBotoes = new JPanel();
        contentPane.add(panelBotoes);
        
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar"); 
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSalvar);
        
        // Configurações e Listeners
        carregarAcoesNaoVendidas(); 
        limparJanela();

        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cmdSalvar_Click();
            }
        });
    }
    
    private void carregarAcoesNaoVendidas() {
        cmbAcao.removeAllItems();
        List<Acao> acoes = acaoDAO.obterAcoesAbertas();
        
        if (acoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
				"Não há ações para serem vendidas.", "Aviso", 
				JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Acao acao : acoes) {
            cmbAcao.addItem(acao);
        }
        
        acaoSelecionada = acoes.get(0);
    }
    

    public void limparJanela() {
        if (cmbAcao.getItemCount() > 0) {
            cmbAcao.setSelectedIndex(0);
        }
        txtValor.setText("");
    }

    private void cmdSalvar_Click() {

        String valorText = txtValor.getText().trim();

        if (!ValidatorUtils.isNumeric(valorText)) {
            JOptionPane.showMessageDialog(this, 
                "Insira um Valor numérico válido ",   "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtValor.requestFocusInWindow();
            return;
        }
        
        try {
        	acaoSelecionada = (Acao) cmbAcao.getSelectedItem();
            double valorDividendo = Double.parseDouble(valorText.replace(",", "."));
            
            Dividendo novoDividendo = new Dividendo(acaoSelecionada.getId(), valorDividendo);
            new DividendoDAO().inserir(novoDividendo); 

            limparJanela();
            
            JOptionPane.showMessageDialog(this, 
                "Dividendo registrado com sucesso!",  "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, 
                "O valor não está no formato numérico correto.",  "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar o dividendo: " + ex.getMessage(),  "Erro de Banco de Dados", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
