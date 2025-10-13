package view.paneles; // Crie um subpacote 'paneles' para organização

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import dao.AcaoDAO;
import model.Acao; // Importe seus modelos e DAOs
import util.Utils;
import view.OperacoesListener;

public class PainelComprarAcao extends JPanel {

    private final AcaoDAO acaoDAO = new AcaoDAO();
    private JTextField txtDataCompraAcao;
    private JTextField txtAcaoCompra;
    private JTextField txtQuantidadeCompraAcao;
    private JTextField txtPrecoCompraAcao;
    private OperacoesListener listener;
    private String tipoOperacao = "DIV"; // Tipo de operação padrão

    public PainelComprarAcao(OperacoesListener listener) {
    	this.listener = listener;
    	
    	
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(6, 1, 0, 0)); // Aumentei para 6 para incluir o Tipo Estratégia
        
        // 1. Data e Tipo Estratégia
        JPanel panelDataTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDataTipo.add(new JLabel("Data               "));
        txtDataCompraAcao = new JTextField(10);
        panelDataTipo.add(txtDataCompraAcao);
        this.add(panelDataTipo);
        
        // 2. Ação
        JPanel panelAcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcao.add(new JLabel("Ação              "));
        txtAcaoCompra = new JTextField(10);
        panelAcao.add(txtAcaoCompra);
        this.add(panelAcao);
        
        // 3. Quantidade
        JPanel panelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQuantidade.add(new JLabel("Quantidade   "));
        txtQuantidadeCompraAcao = new JTextField(10);
        panelQuantidade.add(txtQuantidadeCompraAcao);
        this.add(panelQuantidade);
        
        // 4. Preço Compra
        JPanel panelPreco = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPreco.add(new JLabel("Preço             "));
        txtPrecoCompraAcao = new JTextField(10);
        panelPreco.add(txtPrecoCompraAcao);
        this.add(panelPreco);
        
        // 5. Botões
        JPanel panelBotoes = new JPanel();
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSair.addActionListener(e -> { 
        });
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar"); 
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        panelBotoes.add(btnSalvar);
        this.add(panelBotoes);

        limparPainel();
    }

    public void limparPainel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDataCompraAcao.setText(dateFormat.format(new Date()));
        txtAcaoCompra.setText("");
        txtPrecoCompraAcao.setText("");
        txtQuantidadeCompraAcao.setText("100");
    }
    
    private boolean validarData() {
		String dataText = txtDataCompraAcao.getText().trim();
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
			txtDataCompraAcao.requestFocusInWindow();
			return false;
		}
    }
    
    private void cmdSalvar_Click() {

        if (!validarData()) return;

        String acaoText = txtAcaoCompra.getText().trim();
        String quantidadeText = txtQuantidadeCompraAcao.getText().trim();
        String precoCompraText = txtPrecoCompraAcao.getText().trim();

        if (acaoText.isEmpty() || !Utils.isNumeric(quantidadeText) || !Utils.isNumeric(precoCompraText)) {
            JOptionPane.showMessageDialog(this, 
                "Preencha todos os campos corretamente.", "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            return; 
        }

        acaoDAO.comprarAcao(acaoText, 
        					txtDataCompraAcao.getText().trim(), 
                            quantidadeText, precoCompraText, 
                            tipoOperacao); 
        
        limparPainel();
        
        JOptionPane.showMessageDialog(this, 
        		"Ação inserida com sucesso!", 
        		"Sucesso", 
        		JOptionPane.INFORMATION_MESSAGE);
        
        // Chama o callback para que o FrmOperacoesConsolidadas limpe as outras abas
        if (listener != null) {
            listener.onOperacaoSalvaSucesso();
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
  
            PainelComprarAcao painel = new PainelComprarAcao(mockListener);

            // 4. Adiciona o painel ao frame
            frame.getContentPane().add(painel);
            
            // 5. Ajusta o tamanho e torna visível
            frame.pack();
            frame.setLocationRelativeTo(null); // Centraliza
            frame.setVisible(true);
        });
    }
}