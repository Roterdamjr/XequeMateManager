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

import dao.OpcaoDAO;
import model.Opcao;
import util.Utils;

public class PainelComprarOpcao extends JPanel {

    private final OpcaoDAO opcaoDAO = new OpcaoDAO();
    private JTextField txtDataCompraOpcao;
    private JComboBox<Opcao> cmbOpcaoCompra; 
    private Opcao opcaoSelecionadaCompra = null;
    private JTextField txtPrecoCompraOpcao;
    private JLabel lblQuantidadeCompraOpcao;
    private JLabel lblPrecoVendaCompraOpcao; // No FrmComprarOpcao original é lblPrecoVenda

    public PainelComprarOpcao() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(5, 1, 0, 0));
        
        // 1. Data
        JPanel panelData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelData.add(new JLabel("Data da Compra"));
        txtDataCompraOpcao = new JTextField(10);
        panelData.add(txtDataCompraOpcao);
        this.add(panelData);
        
        // 2. Opção
        JPanel panelOpcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpcao.add(new JLabel("Opção"));
        cmbOpcaoCompra = new JComboBox<>();
        cmbOpcaoCompra.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelOpcao.add(cmbOpcaoCompra);
        this.add(panelOpcao);
        
        // 3. Detalhes (Quantidade e Preço Venda da Opção - LABELS)
        JPanel panelDetalhes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDetalhes.add(new JLabel("Quantidade:"));
        lblQuantidadeCompraOpcao = new JLabel("N/D");
        panelDetalhes.add(lblQuantidadeCompraOpcao);
        
        panelDetalhes.add(new JLabel(" Preço Venda (Strike):"));
        lblPrecoVendaCompraOpcao = new JLabel("N/D");
        panelDetalhes.add(lblPrecoVendaCompraOpcao);
        this.add(panelDetalhes);

        // 4. Preço Compra
        JPanel panelPrecoCompra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrecoCompra.add(new JLabel("Preço Compra"));
        txtPrecoCompraOpcao = new JTextField(10);
        panelPrecoCompra.add(txtPrecoCompraOpcao);
        this.add(panelPrecoCompra);
        
        // 5. Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar"); 
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        panelBotoes.add(btnSalvar);
        this.add(panelBotoes);

        // Listener para o ComboBox (Migrado de FrmComprarOpcao.java)
        cmbOpcaoCompra.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    opcaoSelecionadaCompra = (Opcao) cmbOpcaoCompra.getSelectedItem();
                    atualizarLabels();
                }
            }
        });
        
        limparPainel();
    }
    
    // =====================================================================
    // Lógica de Limpeza e Persistência
    // =====================================================================

    public void limparPainel() {
        // Migrado de FrmComprarOpcao.java -> limparJanela()
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDataCompraOpcao.setText(dateFormat.format(new Date()));
        txtPrecoCompraOpcao.setText("");
        carregarOpcoesCompra();
        atualizarLabels();
    }

    private void carregarOpcoesCompra() {
        // Migrado de FrmComprarOpcao.java -> carregarOpcoes()
        try {
            List<Opcao> opcoes = opcaoDAO.listarOpcoesDisponiveis(); // Método de exemplo
            cmbOpcaoCompra.removeAllItems();
            for (Opcao opcao : opcoes) {
                cmbOpcaoCompra.addItem(opcao);
            }
            if (!opcoes.isEmpty()) {
                opcaoSelecionadaCompra = opcoes.get(0);
            } else {
                opcaoSelecionadaCompra = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a lista de opções: " + e.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarLabels() {
        // Migrado de FrmComprarOpcao.java -> atualizarLabels()
        if (opcaoSelecionadaCompra != null) {
            lblQuantidadeCompraOpcao.setText(String.valueOf(opcaoSelecionadaCompra.getQuantidade()));
            lblPrecoVendaCompraOpcao.setText(String.format("R$ %.2f", opcaoSelecionadaCompra.getStrike())); // Preço Venda é o Strike
        } else {
            lblQuantidadeCompraOpcao.setText("N/D");
            lblPrecoVendaCompraOpcao.setText("N/D");
        }
    }
    
    private boolean validarData() {
		String dataText = txtDataCompraOpcao.getText().trim();
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
			txtDataCompraOpcao.requestFocusInWindow();
			return false; 
		}
    }

    private void cmdSalvar_Click() {
        // Lógica de Salvar Compra Opção, migrada de FrmComprarOpcao.java

        if (opcaoSelecionadaCompra == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhuma Opção selecionada para compra.", 
                "Erro de Seleção", 
                JOptionPane.INFORMATION_MESSAGE);
            return; 
        }
        
        if (!validarData()) return;

        String precoCompraText = txtPrecoCompraOpcao.getText().trim();

        if (precoCompraText.isEmpty() || !Utils.isNumeric(precoCompraText)) {
            JOptionPane.showMessageDialog(this, 
                "O campo Preço Compra deve ser preenchido corretamente com um valor numérico.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtPrecoCompraOpcao.requestFocusInWindow();
            return; 
        }

        try {
            double precoCompraDouble = Double.parseDouble(precoCompraText.replace(",", "."));
            
            // Supondo um método salvar no OpcaoDAO que receba os dados
            opcaoDAO.comprarOpcao(opcaoSelecionadaCompra, txtDataCompraOpcao.getText().trim(), precoCompraDouble);
            
            JOptionPane.showMessageDialog(this, "Opção inserida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparPainel();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao converter o preço. Use formato numérico.", 
                "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}