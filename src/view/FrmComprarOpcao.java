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

import model.Opcao;
import dao.OpcaoDAO;
import util.ValidatorUtils;

public class FrmComprarOpcao extends JInternalFrame {

	private JPanel contentPane;
	private JTextField txtData;
	private JComboBox<Opcao> cmbOpcao; 
    private OpcaoDAO opcaoDAO = new OpcaoDAO();
    private Opcao opcaoSelecionada = null;
    private JTextField txtPrecoCompra;
    private JLabel lblQuantidade, lblPrecoVenda; // Detalhes da Opção
    

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmComprarOpcao frame = new FrmComprarOpcao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FrmComprarOpcao() {
		setTitle("Opção - Compra");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 1, 0, 0));
		
		// --- 1. Data Panel ---
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		panel.add(new JLabel("Data da Compra"));
		txtData = new JTextField();
		txtData.setColumns(10);
		panel.add(txtData);
		
		// --- 2. Opção Panel ---
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1);
		
		panel_1.add(new JLabel("Opção"));
		cmbOpcao = new JComboBox<>();
        cmbOpcao.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(cmbOpcao);
		
		// --- 3. Detalhes (Quantidade e Preço Venda da Opção - LABELS) ---
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_2);
		
		panel_2.add(new JLabel("Quantidade:"));
		lblQuantidade = new JLabel("N/D"); // Inicializado como "N/D"
		panel_2.add(lblQuantidade);
		
		// --- 4. Preço Compra Panel ---
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_3);

		panel_3.add(new JLabel("Preço Compra"));
		
		txtPrecoCompra = new JTextField();
		txtPrecoCompra.setColumns(10);
		panel_3.add(txtPrecoCompra);
		
		JLabel lblPreoVenda = new JLabel(" Preço Venda");
		panel_3.add(lblPreoVenda);
		lblPrecoVenda = new JLabel("N/D");
		panel_3.add(lblPrecoVenda);
		
		// --- 5. Buttons Panel ---
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_4.add(btnSair);
		
		JButton btnSalvar = new JButton("Salvar"); 
		btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_4.add(btnSalvar);
		
		limparJanela();
        
        // Listener para atualizar os detalhes na seleção
        cmbOpcao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    opcaoSelecionada = (Opcao) cmbOpcao.getSelectedItem();
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
	
    private void carregarOpcoesNaoCompradas() {
        cmbOpcao.removeAllItems();
        List<Opcao> opcoes = opcaoDAO.obterOpcoesNaoCompradas();
        
        if (opcoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
				"Não há opções disponíveis para compra.", 
				"Aviso", 
				JOptionPane.WARNING_MESSAGE);
            opcaoSelecionada = null;
        } else {
            for (Opcao opcao : opcoes) {
                cmbOpcao.addItem(opcao);
            }
            // Seleciona o primeiro item e atualiza as labels
            opcaoSelecionada = opcoes.get(0);
        }
        atualizarLabels();
    }
    
    private void atualizarLabels() {
        if (opcaoSelecionada != null) {
            lblQuantidade.setText(String.format("%.2f", opcaoSelecionada.getQuantidade()));
            // O VBA usa "dadosLidos(2)" que é o precoVenda, que para uma opção 'vendida' por nós, é o Strike
            lblPrecoVenda.setText(String.format("R$ %.2f", opcaoSelecionada.getPrecoVenda())); 
        } else {
            lblQuantidade.setText("N/D");
            lblPrecoVenda.setText("N/D");
        }
    }
    
	public void limparJanela() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		txtData.setText(dateFormat.format(new Date()));
		txtPrecoCompra.setText("");
        carregarOpcoesNaoCompradas(); // Recarrega a combo e atualiza labels
	}

	private void cmdSalvar_Click() {
		
        if (opcaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, 
				"Selecione um ativo para comprar.", 
				"Erro de Seleção", 
				JOptionPane.INFORMATION_MESSAGE);
			return; 
        }
        
		String dataText = txtData.getText().trim();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false); 
		
		try {
			dateFormat.parse(dataText);
		} catch (ParseException ex) {
			JOptionPane.showMessageDialog(this, 
				"Por favor, insira uma data válida no formato dd/mm/yyyy.", 
				"Erro de Validação", 
				JOptionPane.INFORMATION_MESSAGE);
			txtData.requestFocusInWindow();
			return; 
		}

		String precoCompraText = txtPrecoCompra.getText().trim();

		// [cite: 6]
		if (precoCompraText.isEmpty() || !ValidatorUtils.isNumeric(precoCompraText)) {
			JOptionPane.showMessageDialog(this, 
				"O campo Preço Compra deve ser preenchido corretamente com um valor numérico.", 
				"Erro de Validação", 
				JOptionPane.INFORMATION_MESSAGE);
			return; 
		}

		salvar(opcaoSelecionada, dataText, precoCompraText);
	}

	private void salvar(Opcao opcao, String dataCompra, String precoCompra) {
        
        double precoCompraDouble = Double.parseDouble(precoCompra.replace(",", "."));
        
        // Chama o método do DAO para persistir a compra
		opcaoDAO.comprarOpcao(opcao.getId(), dataCompra, precoCompraDouble);

		limparJanela();
		
		JOptionPane.showMessageDialog(this, 
			"Opção comprada com sucesso!", // [cite: 7]
			"Sucesso", 
			JOptionPane.INFORMATION_MESSAGE);
	}
}