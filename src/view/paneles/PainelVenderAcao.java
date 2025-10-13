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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.AcaoDAO;
import model.Acao;
import util.Utils;
import view.OperacoesListener;
import javax.swing.border.BevelBorder;

public class PainelVenderAcao extends JPanel {

    private final AcaoDAO acaoDAO = new AcaoDAO();
    private JTextField txtDataVendaAcao;
    private JComboBox<Acao> cmbAcaoVenda; 
    private Acao acaoSelecionadaVenda = null;
    private JTextField txtPrecoVendaAcao;
    private JLabel lblQuantidadeVenda;
    private JLabel lblPrecoCompraVenda;
    private OperacoesListener listener;
    private String tipoOperacao = "DIV"; 
    
    public PainelVenderAcao() {
        this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        this.setLayout(new GridLayout(5, 1, 0, 0)); 
        
        // 1. Data
        JPanel panelData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelData.add(new JLabel("Data         "));
        txtDataVendaAcao = new JTextField(10);
        panelData.add(txtDataVendaAcao);
        this.add(panelData);
        
        // 2. Ação
        JPanel panelAcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcao.add(new JLabel("Ação          "));
        cmbAcaoVenda = new JComboBox<>();
        cmbAcaoVenda.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelAcao.add(cmbAcaoVenda);
        this.add(panelAcao);
        
        // 3. Detalhes (Quantidade e Preço Compra - LABELS)
        JPanel panelDetalhes = new JPanel(new GridLayout(1, 2));
        
        JPanel panelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQuantidade.add(new JLabel("Quantidade:"));
        lblQuantidadeVenda = new JLabel("0.0");
        panelQuantidade.add(lblQuantidadeVenda);
        panelDetalhes.add(panelQuantidade);
        
        JPanel panelPrecoCompra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrecoCompra.add(new JLabel("Preço Compra:"));
        lblPrecoCompraVenda = new JLabel("R$ 0.00");
        panelPrecoCompra.add(lblPrecoCompraVenda);
        panelDetalhes.add(panelPrecoCompra);
        this.add(panelDetalhes);

        // 4. Preço Venda
        JPanel panelPrecoVenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrecoVenda.add(new JLabel("Preço           "));
        txtPrecoVendaAcao = new JTextField(10);
        panelPrecoVenda.add(txtPrecoVendaAcao);
        this.add(panelPrecoVenda);
        
        // 5. Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        // Ação de Sair deve ser tratada pelo pai, ou injetar um listener
        // btnSair.addActionListener(e -> dispose()); // REMOVIDO: JPanel não pode chamar dispose()
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar"); 
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        panelBotoes.add(btnSalvar);
        this.add(panelBotoes);

        // Listener para o ComboBox (ItemListener migrado de FrmVenderAcao.java)
        cmbAcaoVenda.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    acaoSelecionadaVenda = (Acao) cmbAcaoVenda.getSelectedItem();
                    atualizarLabels();
                }
            }
        });
        
        limparPainel();
    }


    public void limparPainel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDataVendaAcao.setText(dateFormat.format(new Date()));
        txtPrecoVendaAcao.setText("");
        carregarAcoesVenda();
        atualizarLabels();
    }
    
    public void carregarAcoesVenda() {
        try {
            List<Acao> acoes = acaoDAO.obterAcoesAbertas(); 
            cmbAcaoVenda.removeAllItems();
            
            for (Acao acao : acoes) {
                cmbAcaoVenda.addItem(acao);
            }
            
            if (!acoes.isEmpty()) {
                acaoSelecionadaVenda = acoes.get(0);
                cmbAcaoVenda.setSelectedIndex(0);
            } else {
                acaoSelecionadaVenda = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
            		"Erro ao carregar a lista de ações: " +
			        e.getMessage(), "Erro de BD", 
			        JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarLabels() {
        if (acaoSelecionadaVenda != null) {
            lblQuantidadeVenda.setText(String.valueOf(acaoSelecionadaVenda.getQuantidade()));
            lblPrecoCompraVenda.setText(String.format("R$ %.2f", acaoSelecionadaVenda.getPrecoCompra()));
        } else {
            lblQuantidadeVenda.setText("0.0");
            lblPrecoCompraVenda.setText("R$ 0.00");
        }
    }

    private boolean validarData() {
		String dataText = txtDataVendaAcao.getText().trim();
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
			txtDataVendaAcao.requestFocusInWindow();
			return false; 
		}
    }
    
    private void cmdSalvar_Click() {
        
        if (acaoSelecionadaVenda == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhuma ação selecionada para venda.", 
                "Erro de Seleção", 
                JOptionPane.INFORMATION_MESSAGE);
            return; 
        }
        
        if (!validarData()) return;

        String precoVendaText = txtPrecoVendaAcao.getText().trim();

        if (!Utils.isNumeric(precoVendaText)) {
            JOptionPane.showMessageDialog(this, 
                "O campo Preço Venda deve ser preenchido corretamente com um valor numérico.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtPrecoVendaAcao.requestFocusInWindow();
            return; 
        }
        
        salvar(acaoSelecionadaVenda);

    }
    
	private void salvar(Acao acao) {
		String dataVenda = txtDataVendaAcao.getText().trim();
		
		String precoVendaText = txtPrecoVendaAcao.getText().trim();
        double precoVendaDouble = Double.parseDouble(precoVendaText.replace(",", "."));
        
		acaoDAO.venderAcao(acao.getId(), precoVendaDouble, dataVenda);

		limparPainel();
		
		carregarAcoesVenda(); 
		
		JOptionPane.showMessageDialog(this, 
			"Ação vendida com sucesso!", 
			"Sucesso", 
			JOptionPane.INFORMATION_MESSAGE);
		
        if (listener != null) {
            listener.onOperacaoSalvaSucesso();
        }
	}
	
}