package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.AcaoDAO;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
// Importa a nova classe de utilidade
import util.ValidatorUtils;

public class FrmComprarAcao extends JInternalFrame  {

	private JPanel contentPane;
	private JTextField txtData;
	private JTextField txtAcao; 
	private JTextField txtQuantidade; 
	private JTextField txtPrecoCompra;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmComprarAcao frame = new FrmComprarAcao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FrmComprarAcao() {
		setClosable(true);
		setTitle("Comprar Ação");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 378, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 1, 0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel("Data");
		panel.add(lblNewLabel);
		
		txtData = new JTextField();
		txtData.setColumns(10);
		panel.add(txtData);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("Ação");
		panel_1.add(lblNewLabel_1);
		
		txtAcao = new JTextField();
		txtAcao.setColumns(10);
		panel_1.add(txtAcao);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_2);
		
		JLabel lblNewLabel_2 = new JLabel("Quantidade");
		panel_2.add(lblNewLabel_2);
		
		txtQuantidade = new JTextField(); 
		txtQuantidade.setColumns(10);
		panel_2.add(txtQuantidade);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_3);
		
		JLabel lblNewLabel_2_2 = new JLabel("Preço Compra");
		panel_3.add(lblNewLabel_2_2);
		
		txtPrecoCompra = new JTextField();
		txtPrecoCompra.setColumns(10);
		panel_3.add(txtPrecoCompra);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_4.add(btnSair);
		
		JButton btnSalvar = new JButton("Salvar"); 
		btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_4.add(btnSalvar);
		
		limparJanela();

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
	
	public void limparJanela() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		txtData.setText(dateFormat.format(new Date()));
		txtAcao.setText("");
		txtPrecoCompra.setText("");
		txtQuantidade.setText("100");
		
	}

	private void cmdSalvar_Click() {
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

		String acaoText = txtAcao.getText().trim();
		String quantidadeText = txtQuantidade.getText().trim();
		String precoCompraText = txtPrecoCompra.getText().trim();

		// Usa o método isNumeric da classe ValidatorUtils
		if (acaoText.isEmpty() || !ValidatorUtils.isNumeric(quantidadeText) || !ValidatorUtils.isNumeric(precoCompraText)) {
			JOptionPane.showMessageDialog(this, 
				"Preencha todos os campos corretamente (Ação não pode ser vazia; Quantidade e Preço devem ser numéricos).", 
				"Erro de Validação", 
				JOptionPane.INFORMATION_MESSAGE);
			return; 
		}

		new AcaoDAO().comprarAcao(acaoText, dataText, quantidadeText, precoCompraText);
		limparJanela();
		
		JOptionPane.showMessageDialog(this, 
			"Ação inserida com sucesso!", 
			"Sucesso", 
			JOptionPane.INFORMATION_MESSAGE);
	}


}