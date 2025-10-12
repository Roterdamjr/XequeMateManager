package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

import model.Acao;
import model.Opcao;
import dao.AcaoDAO;
import dao.OpcaoDAO;
import util.Utils;

public class FrmVenderOpcao extends JInternalFrame {

	private JPanel contentPane;
	private JTextField txtDataVenda;
	private JComboBox<Acao> cmbAcao; 
	private AcaoDAO acaoDAO = new AcaoDAO();
    private OpcaoDAO opcaoDAO = new OpcaoDAO();
    private Acao acaoSelecionada = null;
    private JTextField txtPrecoVenda;
    private JTextField txtStrike;
    private JTextField txtOpcao;
    JLabel lblQuantidade;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmVenderOpcao frame = new FrmVenderOpcao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FrmVenderOpcao() {
		setTitle("Opção - Venda");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300); // Ajustando o tamanho
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(7, 1, 0, 0)); // 6 linhas para Data, Opção, Detalhes Compra, Detalhes Opção, Venda, Botões
		
		// --- 1. Data Panel ---
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		panel.add(new JLabel("Data Venda"));
		txtDataVenda = new JTextField();
		txtDataVenda.setColumns(10);
		panel.add(txtDataVenda);
		
		// --- 2. Opção Panel ---
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1);
		
		panel_1.add(new JLabel("Ação"));
		cmbAcao = new JComboBox<>();
        cmbAcao.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(cmbAcao);
		
		// --- 3. Detalhes de Compra (Quantidade e Preço Compra) ---
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_2);
		
		JLabel lblNewLabel = new JLabel("Opção");
		panel_2.add(lblNewLabel);
		
		txtOpcao = new JTextField();
		panel_2.add(txtOpcao);
		txtOpcao.setColumns(10);
		
		// --- 4. Detalhes da Opção (Strike) ---
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_3);
		
		JLabel label_2 = new JLabel("Quantidade:");
		panel_3.add(label_2);
		
		lblQuantidade = new JLabel("New label");
		panel_3.add(lblQuantidade);

		// --- 5. Preço Venda Panel ---
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_4.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_4);
		
		JLabel label_1 = new JLabel("Strike:");
		panel_4.add(label_1);
		
		txtStrike = new JTextField();
		txtStrike.setColumns(10);
		panel_4.add(txtStrike);
		
		// --- 6. Buttons Panel ---
		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_5.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_5);
		
		JLabel label = new JLabel("Preço Venda");
		panel_5.add(label);
		
		txtPrecoVenda = new JTextField();
		txtPrecoVenda.setText("");
		txtPrecoVenda.setColumns(10);
		panel_5.add(txtPrecoVenda);
		
		JPanel panel_6 = new JPanel();
		contentPane.add(panel_6);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_6.add(btnSair);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_6.add(btnSalvar);
		
		limparJanela();
        
        cmbAcao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    acaoSelecionada = (Acao) cmbAcao.getSelectedItem();
                    atualizarLabels();
                }
            }
        });
        
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
				"Não há ações para serem vendidas.", 
				"Aviso", 
				JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Acao acao : acoes) {
            cmbAcao.addItem(acao);
        }
        
        acaoSelecionada = acoes.get(0);
    }
    
    private void atualizarLabels() {
        if (acaoSelecionada != null) {
        	lblQuantidade.setText(String.format("%.0f", acaoSelecionada.getQuantidade()));
        } else {
            lblQuantidade.setText("0.0");
        }
    }
    
	public void limparJanela() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		txtDataVenda.setText(dateFormat.format(new Date()));
		txtOpcao.setText("");
		txtStrike.setText("");
		txtPrecoVenda.setText("");
		
        carregarAcoesNaoVendidas(); 
        atualizarLabels();
	}

	private void cmdSalvar_Click() {
			
		String dataText = txtDataVenda.getText().trim();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false); 
		
		try {
			dateFormat.parse(dataText);
		} catch (ParseException ex) {
			JOptionPane.showMessageDialog(this, 
				"Por favor, insira uma data válida no formato dd/mm/yyyy.", 
				"Erro de Validação", 
				JOptionPane.INFORMATION_MESSAGE);
			txtDataVenda.requestFocusInWindow();
			return; 
		}
		
		String opcaoText = txtOpcao.getText().trim();
		String quantidadeText = lblQuantidade.getText().trim();
		String strikeText = txtStrike.getText().trim();
		String precoVendaText = txtPrecoVenda.getText().trim();
		
		if (precoVendaText.isEmpty() || !Utils.isNumeric(precoVendaText)) {
			JOptionPane.showMessageDialog(this, 
				"O campo Preço Venda deve ser preenchido corretamente com um valor numérico.", 
				"Erro de Validação", 
				JOptionPane.INFORMATION_MESSAGE);
			return; 
		}
		
        double precoVendaDouble = Double.parseDouble(precoVendaText.replace(",", "."));

		opcaoDAO.venderOpcao(acaoSelecionada.getId(), 
							dataText,
							opcaoText, 
							quantidadeText, 
							strikeText,
							precoVendaDouble);

		limparJanela();
		
		JOptionPane.showMessageDialog(this, 
			"Opção vendida com sucesso!", 
			"Sucesso", 
			JOptionPane.INFORMATION_MESSAGE);
	}


}