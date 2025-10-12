package view.paneles; // Crie um subpacote 'paneles' para organização

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import dao.AcaoDAO;
import model.Acao; // Importe seus modelos e DAOs
import util.Utils;

public class PainelComprarAcao extends JPanel {

    private final AcaoDAO acaoDAO = new AcaoDAO();
    private JTextField txtDataCompraAcao;
    private JTextField txtAcaoCompra;
    private JTextField txtQuantidadeCompraAcao;
    private JTextField txtPrecoCompraAcao;
    private JComboBox<String> cmbTipoOperacao;
    private String tipoOperacao = "DIV"; // Tipo de operação padrão

    public PainelComprarAcao() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(6, 1, 0, 0)); // Aumentei para 6 para incluir o Tipo Estratégia
        
        // 1. Data e Tipo Estratégia
        JPanel panelDataTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDataTipo.add(new JLabel("Data"));
        txtDataCompraAcao = new JTextField(10);
        panelDataTipo.add(txtDataCompraAcao);
        
        panelDataTipo.add(new JLabel("Tipo Estratégia"));
        cmbTipoOperacao = new JComboBox<>();
        cmbTipoOperacao.setModel(new DefaultComboBoxModel<>(new String[] {"DIV", "GNH", "D3X"}));
        panelDataTipo.add(cmbTipoOperacao);
        this.add(panelDataTipo);
        
        // 2. Ação
        JPanel panelAcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcao.add(new JLabel("Ação"));
        txtAcaoCompra = new JTextField(10);
        panelAcao.add(txtAcaoCompra);
        this.add(panelAcao);
        
        // 3. Quantidade
        JPanel panelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQuantidade.add(new JLabel("Quantidade"));
        txtQuantidadeCompraAcao = new JTextField(10);
        panelQuantidade.add(txtQuantidadeCompraAcao);
        this.add(panelQuantidade);
        
        // 4. Preço Compra
        JPanel panelPreco = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPreco.add(new JLabel("Preço Compra"));
        txtPrecoCompraAcao = new JTextField(10);
        panelPreco.add(txtPrecoCompraAcao);
        this.add(panelPreco);
        
        // 5. Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSair.addActionListener(e -> { 
            // Em um painel especializado, você precisaria de um listener
            // ou método de callback para fechar a JInternalFrame pai.
            // Para simplificar, vou remover o botão Sair ou fazer uma ação temporária.
            // Aqui, ele permanecerá no painel, mas a ação será gerenciada pela principal
            // ou através de um listener injetado. Para o teste, manterei
            // e farei a lógica de Salvar.
        });
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar"); 
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        panelBotoes.add(btnSalvar);
        this.add(panelBotoes);
        
        // Configurações iniciais
        limparPainel();
    }
    
    // Método público para limpar (chamado externamente)
    public void limparPainel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDataCompraAcao.setText(dateFormat.format(new Date()));
        txtAcaoCompra.setText("");
        txtPrecoCompraAcao.setText("");
        txtQuantidadeCompraAcao.setText("100");
        cmbTipoOperacao.setSelectedIndex(0);
    }
    
    private boolean validarData() {
        // [Implementação da validação de data, como no seu FrmComprarAcao original]
        // ... (Omitido por brevidade, mas deve ser migrado)
        return true; 
    }
    
    private void cmdSalvar_Click() {
        // Lógica de Salvar Comprar Ação, copiada de FrmComprarAcao.java
        
        if (!validarData()) return;

        String acaoText = txtAcaoCompra.getText().trim();
        String quantidadeText = txtQuantidadeCompraAcao.getText().trim();
        String precoCompraText = txtPrecoCompraAcao.getText().trim();
        
        // Acesso ao item selecionado do ComboBox
        tipoOperacao = (String) cmbTipoOperacao.getSelectedItem();

        if (acaoText.isEmpty() || !Utils.isNumeric(quantidadeText) || !Utils.isNumeric(precoCompraText)) {
            JOptionPane.showMessageDialog(this, 
                "Preencha todos os campos corretamente.", "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            return; 
        }

        acaoDAO.comprarAcao(acaoText, txtDataCompraAcao.getText().trim(), 
                            quantidadeText, precoCompraText, 
                            tipoOperacao); // Usa o tipoOperacao do ComboBox
        
        limparPainel();
        JOptionPane.showMessageDialog(this, "Ação inserida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        // NOTA: Os chamados para limpar as outras telas (limparPainelVenderAcao, etc.)
        // devem ser feitos a partir do FrmOperacoesConsolidadas,
        // usando um "callback" ou método público para notificar o container pai.
    }
}