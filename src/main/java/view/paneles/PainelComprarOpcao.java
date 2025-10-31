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

import dao.AcaoDAO;
import dao.OpcaoDAO;
import model.Acao;
import model.Opcao;
import util.Utils;
import view.FrmrRegistroOperacoes;
import view.OperacoesListener;

public class PainelComprarOpcao extends JPanel {

	private static final long serialVersionUID = 1L;
	private final OpcaoDAO opcaoDAO = new OpcaoDAO();
	private final AcaoDAO acaoDAO = new AcaoDAO(); 
    private JTextField txtDataCompraOpcao;
    private JComboBox<Acao> cmbAcaoOpcaoCompra; 
    private Acao acaoSelecionadaOpcaoCompra = null;
    private JComboBox<Opcao> cmbOpcaoCompra; 
    private JTextField txtOpcaoCompraManual; 
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
    private FrmrRegistroOperacoes frmOperacoes;
    
    private JPanel panelAcao; // Variável de instância adicionada
    
    // ******************** CORREÇÃO: Variável de instância para o Painel de Preço Venda/Compra ********************
    private JPanel panelPrecoCompra; 
    // ******************** FIM CORREÇÃO ********************
    
    public PainelComprarOpcao(OperacoesListener listener) {
    	this.listener = listener;
    	this.frmOperacoes = (FrmrRegistroOperacoes) listener; 
    	
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(9, 1, 0, 0)); 
        
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
        
        // ******************** 2. Ação (NOVO CAMPO) ********************
        // Modificado para usar a variável de instância panelAcao
        panelAcao = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        panelAcao.add(new JLabel("Ação                      "));
        cmbAcaoOpcaoCompra = new JComboBox<>();
        cmbAcaoOpcaoCompra.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelAcao.add(cmbAcaoOpcaoCompra);
        this.add(panelAcao);
        // ******************** FIM NOVO CAMPO ********************
        
        // 3. Opção (Antiga Posição 2)
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
        
        // 4. Detalhes (Quantidade - LABELS/TEXTFIELD) (Antiga Posição 3)
        JPanel panelQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQuantidade.add(new JLabel("Quantidade:         "));
        
        // Componentes Alternativos para Quantidade
        lblQuantidadeCompraOpcao = new JLabel("N/D"); // Modo A
        txtQuantidadeCompraManual = new JTextField(7); // Modo B
        
        panelQuantidade.add(lblQuantidadeCompraOpcao);
        panelQuantidade.add(txtQuantidadeCompraManual);
        this.add(panelQuantidade);
        
        // 5. Preço Compra (Antiga Posição 4) - Rótulo "Preço Compra"
        JPanel panelStrike = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(panelStrike);
        JLabel label_1 = new JLabel("Preço Compra     ");
        panelStrike.add(label_1);
        txtPrecoCompra = new JTextField(7);
        panelStrike.add(txtPrecoCompra);
        
        // ******************** 6. Preço Venda (Antiga Posição 5) ********************
        // CORREÇÃO: Atribuição à variável de instância
        panelPrecoCompra = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        this.add(panelPrecoCompra);
        JLabel label_2 = new JLabel(" Preço Venda:      ");
        panelPrecoCompra.add(label_2);
        
        // Componentes Alternativos para Preço Venda
        lblPrecoVenda = new JLabel("N/D"); // Modo A
        panelPrecoCompra.add(lblPrecoVenda);
        txtPrecoVenda = new JTextField(10);
        panelPrecoCompra.add(txtPrecoVenda);
        // ******************** FIM CORREÇÃO ********************
        
        // 7. Preço Venda (LABELS/TEXTFIELD) (Antiga Posição 6) - Rótulo "Strike"
        JPanel panelPrecoVenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(panelPrecoVenda);
        JLabel lblStrike_1 = new JLabel("Strike                    ");
        panelPrecoVenda.add(lblStrike_1);
        
        lblStrike = new JLabel("N/D"); // Modo A
        panelPrecoVenda.add(lblStrike);
        txtStrike = new JTextField(7);
        panelPrecoVenda.add(txtStrike);
        
        // 8. Botões (Antiga Posição 7)
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
        
        // Listener para a nova ComboBox Ação
        cmbAcaoOpcaoCompra.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && !isModoRecompra) {
                    acaoSelecionadaOpcaoCompra = (Acao) cmbAcaoOpcaoCompra.getSelectedItem();
                    // Nenhuma label para atualizar com a Ação
                }
            }
        });

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
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose(); // Fecha a janela
            }
        });
        
        
        // Inicialização   
       
        setModo(rbRecompra.isSelected()); 
        limparPainel();
        
        SwingUtilities.invokeLater(() -> {
            // O getRootPane retorna null se o componente não estiver anexado a uma hierarquia de topo.
            javax.swing.JRootPane rootPane = SwingUtilities.getRootPane(this);
            if (rootPane != null) {
                rootPane.setDefaultButton(btnSalvar);
            }
        });
    }
    
    /**
     * Alterna entre o Modo A (Combo/Labels - Recompra) e o Modo B (Textos Manuais - Compra).
     * @param isRecompra Se true, define o Modo Recompra. Se false, define o Modo Compra.
     */
    private void setModo(boolean isRecompra) {
        this.isModoRecompra = isRecompra;
        
        // Ação (Visível apenas no Modo Compra Manual)
        // Alterado para controlar o JPanel inteiro (rótulo + combobox)
        panelAcao.setVisible(!isRecompra);
        
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

        // ******************** CORREÇÃO: Oculta o painel inteiro de Preço Venda no Modo Compra ********************
        panelPrecoCompra.setVisible(isRecompra); 
        // ******************** FIM CORREÇÃO ********************
        
        if (isRecompra) {
            carregarOpcoesCompra();
            atualizarLabels();
            acaoSelecionadaOpcaoCompra = null; // Limpa a seleção de Ação no modo Recompra
        } else {
            carregarAcoesCompra(); // Carrega ações para seleção no Modo Compra
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
        txtQuantidadeCompraManual.setText("100");
        txtStrike.setText(""); // Limpa Strike
        txtPrecoVenda.setText("");
        
        if (isModoRecompra) {
            carregarOpcoesCompra();
            acaoSelecionadaOpcaoCompra = null;
        } else {
            carregarAcoesCompra();
            opcaoSelecionadaCompra = null;
        }
        atualizarLabels();
    }
    
    /**
     * Carrega as ações ativas para o JComboBox no Modo Compra.
     */
    public void carregarAcoesCompra() {
        if (isModoRecompra) return; // Carrega apenas no Modo B

        try {
            List<Acao> acoes = acaoDAO.obterAcoesAbertas(frmOperacoes.getTipoOperacao()); 
            
            cmbAcaoOpcaoCompra.removeAllItems();
            
            for (Acao acao : acoes) {
                cmbAcaoOpcaoCompra.addItem(acao);
            }
            
            if (!acoes.isEmpty()) {
                acaoSelecionadaOpcaoCompra = acoes.get(0);
                cmbAcaoOpcaoCompra.setSelectedIndex(0);
            } else {
                acaoSelecionadaOpcaoCompra = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
					            		"Erro ao carregar a lista de ações.", 
					            		"Erro de BD", 
					            		JOptionPane.ERROR_MESSAGE);
        }
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
            lblStrike.setText("N/D"); 
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
        
        // ******************** Não é necessário validar Preço Venda no Modo Compra (já que o painel será ocultado) ********************
        // A Lógica para Preço Venda manual no Modo Compra (se você decidir reexibi-lo):
        /*
        if (precoVendaManualText.isEmpty() || !Utils.isNumeric(precoVendaManualText.replace(",", "."))) {
             JOptionPane.showMessageDialog(this, 
                "O campo Preço Venda deve ser preenchido corretamente com um valor numérico.", 
                "Erro de Validação", 
                JOptionPane.INFORMATION_MESSAGE);
             txtPrecoVenda.requestFocusInWindow();
             
             return false;
        }
        */
        // ******************** FIM ********************
        
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
            	
            	if (acaoSelecionadaOpcaoCompra == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Nenhuma Ação selecionada para a compra da Opção.", 
                        "Erro de Seleção", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return; 
                }

                String opcaoManualText = txtOpcaoCompraManual.getText().trim();
                String quantidadeManualText = txtQuantidadeCompraManual.getText().trim();
                String strikeText = txtStrike.getText().trim(); // Novo campo
                String precoVendaManualText = txtPrecoVenda.getText().trim(); // Preço Venda no Modo Compra

	            if (!validarCamposDeCompra(opcaoManualText, quantidadeManualText, strikeText, precoVendaManualText))
                	return;

	            int idAcaoBase = acaoSelecionadaOpcaoCompra.getId(); // Captura o ID da Ação selecionada
	            
                opcaoDAO.comprarOpcao( idAcaoBase, // Usa o ID da Ação selecionada
                		txtDataCompraOpcao.getText().trim(), 
                		opcaoManualText, 
                		quantidadeManualText,
                		strikeText, 
                		precoCompraDouble
	            		);

                JOptionPane.showMessageDialog(this, 
	            		"Opção lançada com sucesso!", 
	            		"Sucesso", 
	            		JOptionPane.INFORMATION_MESSAGE);
                
                limparPainel();
                
                if (listener != null) {
                    listener.onOperacaoSalvaSucesso();
                }
                
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
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teste Visual PainelComprarOpcao - Modos A/B");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            OperacoesListener mockListener = new OperacoesListener() {
                // Implementação mínima para evitar NullPointerException
                @Override
                public void onOperacaoSalvaSucesso() {
                    System.out.println("Mock Listener: Operação salva com sucesso (simulado).");
                }
            };
            
            // 3. Cria o painel principal
            PainelComprarOpcao painel = new PainelComprarOpcao(mockListener);

            // 4. Adiciona o painel ao frame
            frame.getContentPane().add(painel);
            
            // 5. Ajusta o tamanho e torna visível
            frame.pack();
            frame.setLocationRelativeTo(null); // Centraliza
            frame.setVisible(true);
        });
    }
    

}