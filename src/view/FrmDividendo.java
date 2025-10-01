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

import dao.DividendoDAO; // Presumindo que você terá um DAO para Dividendo
import model.Dividendo; // Presumindo que você terá um modelo Dividendo
import util.ValidatorUtils; // Importa a classe de utilidade

public class FrmDividendo extends JInternalFrame {

    private JPanel contentPane;
    private JComboBox<String> cmbAcao; // ComboBox para selecionar a ação
    private JTextField txtValor;

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
        setBounds(100, 100, 378, 250); // Altura ajustada para 3 campos
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
        cmbAcao.setPrototypeDisplayValue("XXXXX_XXXXX"); // Ajuda a definir o tamanho
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
        carregarAcoesMock(); // Carrega as opções do ComboBox (substituir por dados reais)
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
    
    /**
     * Simula o carregamento de ações do banco de dados para o ComboBox.
     * Na aplicação real, este método chamaria o AcaoDAO.
     */
    private void carregarAcoesMock() {
        // Simulação de ações que viriam do AcaoDAO.listarTodasAcoes()
        String[] acoes = {"Escolha...", "PETR4", "VALE3", "ITSA4", "BBAS3"};
        cmbAcao.setModel(new DefaultComboBoxModel<>(acoes));
    }

    public void limparJanela() {
        if (cmbAcao.getItemCount() > 0) {
            cmbAcao.setSelectedIndex(0);
        }
        txtValor.setText("");
    }

    private void cmdSalvar_Click() {
        String acaoSelecionada = (String) cmbAcao.getSelectedItem();
        String valorText = txtValor.getText().trim();

        // 1. Validação da Ação
        if (acaoSelecionada == null || acaoSelecionada.equals("Escolha...")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione uma Ação válida.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. Validação do Valor (usa o ValidatorUtils, esperando que ele lide com a formatação brasileira)
        // Se o ValidatorUtils só aceita ponto (.), é preciso adaptar.
        if (!ValidatorUtils.isNumeric(valorText)) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira um Valor numérico válido (use ponto ou vírgula, conforme o ValidatorUtils aceitar).", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtValor.requestFocusInWindow();
            return;
        }
        
        // Tenta converter e salvar
        try {
            // Se o seu ValidatorUtils for bem feito, ele pode converter a String para Double
            double valorDividendo = Double.parseDouble(valorText.replace(",", "."));
            
            // NOTE: Seria necessário obter o ID real da Ação a partir do código (AcaoDAO.buscarIdPorCodigo(acaoSelecionada))
            int idAcaoReal = 99; // <--- Substitua pela busca REAL no banco!

            // 3. Salvar no Banco
            // DividendoDAO dao = new DividendoDAO();
            // Dividendo novoDividendo = new Dividendo(idAcaoReal, valorDividendo);
            // dao.inserir(novoDividendo); 

            // Para o teste, apenas exibe a mensagem de sucesso:
            System.out.println("Dividendo registrado para " + acaoSelecionada + " no valor de " + valorDividendo);

            limparJanela();
            
            JOptionPane.showMessageDialog(this, 
                "Dividendo registrado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, 
                "O valor não está no formato numérico correto.", 
                "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar o dividendo: " + ex.getMessage(), 
                "Erro de Banco de Dados", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
