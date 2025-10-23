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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dao.OpcaoDAO;
import model.Opcao;
import util.Utils;
import view.OperacoesListener;
import javax.swing.border.BevelBorder;

public class PainelComprarOpcao extends JPanel {

	private static final long serialVersionUID = 1L;
	private final OpcaoDAO opcaoDAO = new OpcaoDAO();
    private JTextField txtDataCompraOpcao;
    private JComboBox<Opcao> cmbOpcaoCompra; 
    private JTextField txtOpcaoCompraManual; 
    
    // Novo campo Strike
    private JLabel lblStrike;
    private JTextField txtStrike;
    
    JLabel lblPrecoVenda; 
    private JTextField txtPrecoVenda; 
    private Opcao opcaoSelecionadaCompra = null;
    private JTextField txtPrecoCompra;
    private JLabel lblQuantidadeCompraOpcao; 
    private JTextField txtQuantidadeCompraManual; 
    private OperacoesListener listener;
    
    // Controles de Modo
    private JRadioButton rbRecompra;
    private JRadioButton rbCompra;
    private boolean isModoRecompra = true; // Flag para o modo atual
    
    public PainelComprarOpcao() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(8, 1, 0, 0)); // Aumentado para 8 linhas (por causa do Strike)
        
        // 0. Controles de Modo
        JPanel panelModo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelModo.add(new JLabel("Modo"));
        
        rbRecompra = new JRadioButton("Recompra");
        rbCompra = new JRadioButton("Compra");
        
        ButtonGroup grupoModo = new ButtonGroup();
        grupoModo.add(rbRecompra);
        grupoModo.add(rbCompra);
        rbRecompra.setSelected(true);
        
        panelModo.add(rbRecompra);
        panelModo.add(rbCompra);
        this.add(panelModo);
        
        // 1. Data
        JPanel panelData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelData.add(new JLabel("Data da Compra  "));
        txtDataCompraOpcao = new JTextField(10);
        panelData.add(txtDataCompraOpcao);
        this.add(panelData);
        
        // 2. Opção
        JPanel panelOpcao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpcao.add(new JLabel("Opção                  "));
        
        // Componentes Alternativos para Opção
        cmbOpcaoCompra = new JComboBox<>();
        cmbOpcaoCompra.setFont(new Font("Tahoma", Font.PLAIN, 12));
        cmbOpcaoCompra.setPreferredSize(new JTextField(10).getPreferredSize()); // Ajusta o tamanho
        
        txtOpcaoCompraManual = new JTextField(8); // Modo B
        
        panelOpcao.add(cmbOpcaoCompra);
        panelOpcao.add(txtOpcaoCompraManual);
        this.add(panelOpcao);
        
        // 3. Detalhes (Quantidade - LABELS/TEXTFIELD)
        JPanel panelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQuantidade.add(new JLabel("Quantidade:         "));
        
        // Componentes Alternativos para Quantidade
        lblQuantidadeCompraOpcao = new JLabel("N/D"); // Modo A
        txtQuantidadeCompraManual = new JTextField(7); // Modo B
        
        panelQuantidade.add(lblQuantidadeCompraOpcao);
        panelQuantidade.add(txtQuantidadeCompraManual);
        this.add(panelQuantidade);
        
        // 4. Strike (NOVO CAMPO)
        JPanel panelStrike = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(panelStrike);
        JLabel label_1 = new JLabel("Preço Compra     ");
        panelStrike.add(label_1);
        txtPrecoCompra = new JTextField(7);
        panelStrike.add(txtPrecoCompra);
        
        // 5. Preço Compra
        JPanel panelPrecoCompra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(panelPrecoCompra);
        JLabel label_2 = new JLabel(" Preço Venda:      ");
        panelPrecoCompra.add(label_2);
        
        // Componentes Alternativos para Preço Venda
        lblPrecoVenda = new JLabel("N/D"); // Modo A
        panelPrecoCompra.add(lblPrecoVenda);
        txtPrecoVenda = new JTextField(10);
        panelPrecoCompra.add(txtPrecoVenda);
        
        // 6. Preço Venda (LABELS/TEXTFIELD)
        JPanel panelPrecoVenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(panelPrecoVenda);
        JLabel lblStrike_1 = new JLabel("Strike                    ");
        panelPrecoVenda.add(lblStrike_1);
        
        lblStrike = new JLabel("N/D"); // Modo A
        panelPrecoVenda.add(lblStrike);
        txtStrike = new JTextField(7);
        panelPrecoVenda.add(txtStrike);
        
        // 7. Botões
        JPanel panelBotoes = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelBotoes.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT); // Alinha os botões
        add(panelBotoes);
        
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSair);
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelBotoes.add(btnSalvar);
        
        // ******************** Listeners ********************
        cmbOpcaoCompra.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && isModoRecompra) {
                    opcaoSelecionadaCompra = (Opcao) cmbOpcaoCompra.getSelectedItem();
                    atualizarLabels();
                }
            }
        });
        
        // Listener para os RadioButtons (Alternância de Modo)
        rbRecompra.addActionListener(e -> setModo(true));
        rbCompra.addActionListener(e -> setModo(false));
        
        btnSalvar.addActionListener(e -> cmdSalvar_Click());
        
        btnSair.addActionListener(e -> {
            // Obtém o Frame (Janela) que contém este painel
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose(); // Fecha a janela
            }
        });
        
        
        // Inicialização
        setModo(rbRecompra.isSelected()); // Configura o modo inicial (A)
        limparPainel();
    }
    
    /**
     * Alterna entre o Modo A (Combo/Labels - Recompra) e o Modo B (Textos Manuais - Compra).
     * @param isRecompra Se true, define o Modo Recompra. Se false, define o Modo Compra.
     */
    private void setModo(boolean isRecompra) {
        this.isModoRecompra = isRecompra;
        
        // Opção
        cmbOpcaoCompra.setVisible(isRecompra);
        txtOpcaoCompraManual.setVisible(!isRecompra);

        // Quantidade
        lblQuantidadeCompraOpcao.setVisible(isRecompra);
        txtQuantidadeCompraManual.setVisible(!isRecompra);
        
        // Strike (NOVO)
        lblStrike.setVisible(isRecompra);
        txtStrike.setVisible(!isRecompra);

        // Preço Venda
        lblPrecoVenda.setVisible(isRecompra);
        txtPrecoVenda.setVisible(!isRecompra);

        if (isRecompra) {
            carregarOpcoesCompra();
            atualizarLabels();
        } else {
            // Limpa campos manuais quando muda para o modo B
            txtOpcaoCompraManual.setText("");
            txtQuantidadeCompraManual.setText("");
            txtStrike.setText(""); // Limpa Strike manual
            txtPrecoVenda.setText("");
        }
        
        // Garante que o container se reorganize
        this.revalidate();
        this.repaint();
    }

    public void limparPainel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDataCompraOpcao.setText(dateFormat.format(new Date()));
        txtPrecoCompra.setText("");
        txtOpcaoCompraManual.setText("");
        txtQuantidadeCompraManual.setText("");
        txtStrike.setText(""); // Limpa Strike
        txtPrecoVenda.setText("");
        
        if (isModoRecompra) {
            carregarOpcoesCompra();
        } else {
            opcaoSelecionadaCompra = null;
        }
        atualizarLabels();
    }

    public void carregarOpcoesCompra() {
        if (!isModoRecompra) return; // Carrega apenas no Modo A

        try {
            List<Opcao> opcoes = opcaoDAO.obterOpcoesNaoCompradas(); 
            
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
            JOptionPane.showMessageDialog(this, 
					            		"Erro ao carregar a lista de opções: " + e.getMessage(), 
					            		"Erro de BD", 
					            		JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarLabels() {
        if (!isModoRecompra) {
            lblQuantidadeCompraOpcao.setText("N/D");
            lblPrecoVenda.setText("N/D");
            lblStrike.setText("N/D"); // Atualiza Strike
            return;
        }
        
        if (opcaoSelecionadaCompra != null) {
            lblQuantidadeCompraOpcao.setText(String.valueOf(opcaoSelecionadaCompra.getQuantidade()));
            lblPrecoVenda.setText(String.format("R$ %.2f", opcaoSelecionadaCompra.getPrecoVenda()));
            lblStrike.setText(String.format("R$ %.2f", opcaoSelecionadaCompra.getStrike())); // Exibe Strike
        } else {
            lblQuantidadeCompraOpcao.setText("N/D");
            lblPrecoVenda.setText("N/D");
            lblStrike.setText("N/D");
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

    private boolean validarCamposDeCompra(String opcaoManualText,
		    								String quantidadeManualText ,
		    								String strikeText,
		    								String precoVendaManualText) {
    	
        if (opcaoManualText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "O campo Opção deve ser preenchido no Modo Compra.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
            txtOpcaoCompraManual.requestFocusInWindow();
            return false;
        }
        
        if (quantidadeManualText.isEmpty() || !Utils.isNumeric(quantidadeManualText)) {
             JOptionPane.showMessageDialog(this, 
                "O campo Quantidade deve ser preenchido corretamente com um valor numérico.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
             txtQuantidadeCompraManual.requestFocusInWindow();
             return false ;
        }
        
        // Validação do Strike (NOVO)
        if (strikeText.isEmpty() || !Utils.isNumeric(strikeText.replace(",", "."))) {
             JOptionPane.showMessageDialog(this, 
                "O campo Strike deve ser preenchido corretamente com um valor numérico.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
             txtStrike.requestFocusInWindow();
             return false;
        }
        
        if (precoVendaManualText.isEmpty() || !Utils.isNumeric(precoVendaManualText.replace(",", "."))) {
             JOptionPane.showMessageDialog(this, 
                "O campo Preço Venda deve ser preenchido corretamente com um valor numérico.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
             txtPrecoVenda.requestFocusInWindow();
             
             return false;
        }
        return true;
    }
    
    private void cmdSalvar_Click() {
        
        if (!validarData()) return;

        String precoCompraText = txtPrecoCompra.getText().trim();
        
        if (precoCompraText.isEmpty() || !Utils.isNumeric(precoCompraText.replace(",", "."))) {
            JOptionPane.showMessageDialog(this, 
						                "O campo Preço Compra deve ser preenchido corretamente com um valor numérico.", 
						                "Erro de Validação", 
						                JOptionPane.INFORMATION_MESSAGE);
            
            txtPrecoCompra.requestFocusInWindow();
            return; 
        }
        
        try {
            double precoCompraDouble = Double.parseDouble(precoCompraText.replace(",", "."));

            if (isModoRecompra) {
               
                opcaoDAO.recomprarOpcao(opcaoSelecionadaCompra.getId(), 
				    					txtDataCompraOpcao.getText().trim(), 
				    					precoCompraDouble);
                
                JOptionPane.showMessageDialog(this, 
					            		"Opção inserida com sucesso!", 
					            		"Sucesso", 
					            		JOptionPane.INFORMATION_MESSAGE);
            
                limparPainel();
                
                if (listener != null) {
                    listener.onOperacaoSalvaSucesso();
                }
            
            }else{            
            	// --- Modo Compra---

                String opcaoManualText = txtOpcaoCompraManual.getText().trim();
                String quantidadeManualText = txtQuantidadeCompraManual.getText().trim();
                String strikeText = txtStrike.getText().trim(); // Novo campo
                String precoVendaManualText = txtPrecoVenda.getText().trim(); // Preço Venda no Modo Compra
                
	            if (!validarCamposDeCompra(opcaoManualText, quantidadeManualText, strikeText, precoVendaManualText))
                	return;

	           	double precoVendaManualDouble = Double.parseDouble(precoVendaManualText.replace(",", "."));
	           	double strikeDouble = Double.parseDouble(strikeText.replace(",", ".")); // Novo parse
	           	
	            int quantidadeManualInt = Integer.parseInt(quantidadeManualText);        
                

/*
                // Ação pendente no DAO:
                opcaoDAO.comprarOpcao( idAcao, 
                		txtDataCompraOpcao.getText().trim(), 
                		opcaoManualText, 
                		quantidadeManualInt,
						strikeDouble, // Usar o novo strike
						precoVendaManualDouble
	            		);
	            		*/
               
                 JOptionPane.showMessageDialog(this, 
                    "Implementação do Salvar no Modo B (Opção Manual) pendente no OpcaoDAO." +
                    "\nDados coletados: Opção=" + opcaoManualText + ", Qtd=" + quantidadeManualInt +
                    ", Strike=" + strikeDouble + ", Preço Compra=" + precoCompraDouble +
                    ", Preço Venda=" + precoVendaManualDouble,
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
                return;
                
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao converter um dos preços ou Strike. Use formato numérico.", 
                "Erro de Formato", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, 
                "Erro ao salvar a opção: " + e.getMessage(), 
                "Erro de BD", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // O método main e o OperacoesListener foram omitidos para brevidade, mas devem permanecer.
    public static void main(String[] args) {
        // Usa a Event-Dispatch Thread (EDT) para garantir a segurança no Swing
        SwingUtilities.invokeLater(() -> {
            // 1. Cria a janela principal
            JFrame frame = new JFrame("Teste Visual PainelComprarOpcao - Modos A/B");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PainelComprarOpcao painel = new PainelComprarOpcao();

            // 4. Adiciona o painel ao frame
            frame.getContentPane().add(painel);
            
            // 5. Ajusta o tamanho e torna visível
            frame.pack();
            frame.setLocationRelativeTo(null); // Centraliza
            frame.setVisible(true);
        });
    }
}