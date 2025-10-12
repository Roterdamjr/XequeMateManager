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
import dao.AcaoDAO;
import util.Utils;

public class FrmVenderAcao extends JInternalFrame {

	private JPanel contentPane;
	private JTextField txtData;
	private JComboBox<Acao> cmbAcao; 
    private AcaoDAO acaoDAO = new AcaoDAO();
    private Acao acaoSelecionada = null;
    private JTextField txtPrecoVenda;
    JLabel lblQuantidade;
    JLabel lblPrecoCompra;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmVenderAcao frame = new FrmVenderAcao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FrmVenderAcao() {
		setTitle("Vender Ação");
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
		
		panel.add(new JLabel("Data"));
		txtData = new JTextField();
		txtData.setColumns(10);
		panel.add(txtData);
		
		// --- 2. Ação Panel ---
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1);
		
		panel_1.add(new JLabel("Ação"));
		
		cmbAcao = new JComboBox<>();
        cmbAcao.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(cmbAcao);
		
		// --- 3. Detalhes (Quantidade e Preço Compra - LABELS) ---
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 2, 0, 0));
		
		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_5.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_5);
		
		JLabel label = new JLabel("Quantidade:");
		panel_5.add(label);
		
		lblQuantidade = new JLabel("0.0");
		panel_5.add(lblQuantidade);
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_6.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_6);
		
		JLabel lbl = new JLabel("Preço Compra:");
		panel_6.add(lbl);
		
		lblPrecoCompra = new JLabel();
		lblPrecoCompra.setText("preco");
		panel_6.add(lblPrecoCompra);
		
		// --- 4. Preço Venda Panel ---
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_3);

		panel_3.add(new JLabel("Preço Venda"));
		
		txtPrecoVenda = new JTextField();
		txtPrecoVenda.setColumns(10);
		panel_3.add(txtPrecoVenda);
		
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
        carregarAcoesNaoVendidas();

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
        atualizarLabels();
    }
    
    private void atualizarLabels() {
        if (acaoSelecionada != null) {
        	lblQuantidade.setText(String.format("%d", acaoSelecionada.getQuantidade()));
        	lblPrecoCompra.setText(String.format("R$ %.2f", acaoSelecionada.getPrecoCompra()));
        } else {
            lblQuantidade.setText("0.0");
            lblPrecoCompra.setText("R$ 0.00");
        }
    }
    
	public void limparJanela() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		txtData.setText(dateFormat.format(new Date()));
        txtPrecoVenda.setText("");
        atualizarLabels();
	}

	private void cmdSalvar_Click() {
		
        if (acaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, 
				"Nenhuma ação selecionada para venda.", 
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

		String precoVendaText = txtPrecoVenda.getText().trim();

		if (!Utils.isNumeric(precoVendaText)) {
			JOptionPane.showMessageDialog(this, 
				"O campo Preço Venda deve ser preenchido corretamente com um valor numérico.", 
				"Erro de Validação", 
				JOptionPane.INFORMATION_MESSAGE);
			return; 
		}

		salvar(acaoSelecionada, dataText, precoVendaText);
	}

	private void salvar(Acao acao, String dataVenda, String precoVenda) {
        
        double precoVendaDouble = Double.parseDouble(precoVenda.replace(",", "."));
        
		acaoDAO.venderAcao(acao.getId(), precoVendaDouble, dataVenda);

		limparJanela();
		
        carregarAcoesNaoVendidas(); 
		
		JOptionPane.showMessageDialog(this, 
			"Ação vendida com sucesso!", 
			"Sucesso", 
			JOptionPane.INFORMATION_MESSAGE);
	}
}