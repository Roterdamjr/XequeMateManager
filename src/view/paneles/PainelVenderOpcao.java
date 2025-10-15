package view.paneles;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dao.AcaoDAO;
import dao.OpcaoDAO;
import model.Acao;
import util.Utils;
import view.FrmOperacoesConsolidadas;
import view.OperacoesListener;

public class PainelVenderOpcao extends JPanel {

	private static final long serialVersionUID = 1L;
	private final AcaoDAO acaoDAO = new AcaoDAO();
    private final OpcaoDAO opcaoDAO = new OpcaoDAO();
    
    private JTextField txtDataVendaOpcao;
    private JComboBox<Acao> cmbAcaoOpcaoVenda; 
    private Acao acaoSelecionadaOpcaoVenda = null;
    private JTextField txtPrecoVendaOpcao;
    private JTextField txtStrikeVendaOpcao;
    private JTextField txtOpcaoVenda; 
    private JTextField txtQuantidade;
    private OperacoesListener listener;
    private FrmOperacoesConsolidadas frmOperacoes;
    
    public PainelVenderOpcao(OperacoesListener listener) {
    	this.listener = listener;
    	this.frmOperacoes = (FrmOperacoesConsolidadas) listener; 
    	
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(7, 1, 0, 0));
        
        // 1. Data
        JPanel panelData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelData.add(new JLabel("Data Venda"));
        txtDataVendaOpcao = new JTextField(7);
        panelData.add(txtDataVendaOpcao);
        this.add(panelData);
        
        // 2. Ação (Para seleção da Ação base da Opção)
        JPanel panelAcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcao.add(new JLabel("Ação)"));
        cmbAcaoOpcaoVenda = new JComboBox<>();
        cmbAcaoOpcaoVenda.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelAcao.add(cmbAcaoOpcaoVenda);
        this.add(panelAcao);
        
        // 3. Opção (Código)
        JPanel panelOpcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpcao.add(new JLabel("Opção         "));
        txtOpcaoVenda = new JTextField(10);
        panelOpcao.add(txtOpcaoVenda);
        this.add(panelOpcao);
        
        // 4. Quantidade (Label da Ação)
        JPanel panelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQuantidade.add(new JLabel("Quantidade "));
        txtQuantidade = new JTextField(4);
        panelQuantidade.add(txtQuantidade);
        this.add(panelQuantidade);

        // 5. Strike
        JPanel panelStrike = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStrike.add(new JLabel("Strike           "));
        txtStrikeVendaOpcao = new JTextField(5);
        panelStrike.add(txtStrikeVendaOpcao);
        this.add(panelStrike);
        
        // 6. Preço Venda
        JPanel panelPrecoVenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrecoVenda.add(new JLabel("Preço Venda "));
        txtPrecoVendaOpcao = new JTextField(5);
        panelPrecoVenda.add(txtPrecoVendaOpcao);
        this.add(panelPrecoVenda);
        
        // 7. Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        panelBotoes.add(btnSalvar);
        this.add(panelBotoes);
        
        // Listener para o ComboBox (Ação)
        cmbAcaoOpcaoVenda.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    acaoSelecionadaOpcaoVenda = (Acao) cmbAcaoOpcaoVenda.getSelectedItem();
                    atualizarLabels();
                }
            }
        });
        
        limparPainel();
    }
 
    public void limparPainel() {
        // Migrado de FrmVenderOpcao.java -> limparJanela()
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDataVendaOpcao.setText(dateFormat.format(new Date()));
        txtOpcaoVenda.setText("");
        txtStrikeVendaOpcao.setText("");
        txtPrecoVendaOpcao.setText("");
        
        carregarAcoesVenda(); 
        atualizarLabels();
    }

    public void carregarAcoesVenda() {

        try {
            List<Acao> acoes = acaoDAO.obterAcoesAbertas(frmOperacoes.getTipoOperacao()); 
            cmbAcaoOpcaoVenda.removeAllItems();
            
            for (Acao acao : acoes) {
                cmbAcaoOpcaoVenda.addItem(acao);
            }
            
            if (!acoes.isEmpty()) {
                acaoSelecionadaOpcaoVenda = acoes.get(0);
                cmbAcaoOpcaoVenda.setSelectedIndex(0);
            } else {
                acaoSelecionadaOpcaoVenda = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
					            		"Erro ao carregar a lista de ações.", 
					            		"Erro de BD", 
					            		JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarLabels() {
        // Migrado de FrmVenderOpcao.java -> atualizarLabels()
        if (acaoSelecionadaOpcaoVenda != null) {
            txtQuantidade.setText(String.valueOf(acaoSelecionadaOpcaoVenda.getQuantidade()));
        } else {
        	txtQuantidade.setText("0.0");
        }
    }
    
    private boolean validarData() {
        String dataText = txtDataVendaOpcao.getText().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false); 
        
        try {
            dateFormat.parse(dataText);
            return true;
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira uma data válida no formato dd/mm/yyyy.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtDataVendaOpcao.requestFocusInWindow();
            return false; 
        }
    }

    private void cmdSalvar_Click() {
        
        if (acaoSelecionadaOpcaoVenda == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhuma Ação selecionada para lançamento da Opção.", 
                "Erro de Seleção", 
                JOptionPane.INFORMATION_MESSAGE);
            return; 
        }
        
        if (!validarData()) return;

        String opcaoText = txtOpcaoVenda.getText().trim();
        String strikeText = txtStrikeVendaOpcao.getText().trim();
        String precoVendaText = txtPrecoVendaOpcao.getText().trim();
        
        if (opcaoText.isEmpty() || !Utils.isNumeric(strikeText) || !Utils.isNumeric(precoVendaText)) {
            JOptionPane.showMessageDialog(this, 
                "Preencha o Código da Opção, Strike e Preço Venda corretamente com valores numéricos.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            return; 
        }
        
        try {
            double precoVendaDouble = Double.parseDouble(precoVendaText.replace(",", "."));
            int idAcaoBase = acaoSelecionadaOpcaoVenda.getId();

            opcaoDAO.venderOpcao(idAcaoBase, 
			            		txtDataVendaOpcao.getText().trim(),
			            		txtOpcaoVenda.getText(),
			            		txtQuantidade.getText(),     		
			            		strikeText, 
			            		precoVendaDouble
			            		);
            
            JOptionPane.showMessageDialog(this, 
				            		"Opção lançada com sucesso!", 
				            		"Sucesso", 
				            		JOptionPane.INFORMATION_MESSAGE);
            limparPainel();
            
            if (listener != null) {
                listener.onOperacaoSalvaSucesso();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao converter valores. Use formato numérico.", 
                "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        // Usa a Event-Dispatch Thread (EDT) para garantir a segurança no Swing
        SwingUtilities.invokeLater(() -> {
            // 1. Cria a janela principal
            JFrame frame = new JFrame("Teste Visual PainelVenderOpcao");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            OperacoesListener mockListener = () -> {
                // System.out.println("Listener mock chamado com sucesso.");
            };
  
            PainelVenderOpcao painel = new PainelVenderOpcao(mockListener);

            // 4. Adiciona o painel ao frame
            frame.getContentPane().add(painel);
            
            // 5. Ajusta o tamanho e torna visível
            frame.pack();
            frame.setLocationRelativeTo(null); // Centraliza
            frame.setVisible(true);
        });
    }
}