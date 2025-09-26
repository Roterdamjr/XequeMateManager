package view;

import dao.AcaoDAO;
import model.Acao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmAcoesNaoVendidas extends JInternalFrame {

    private JTable acoesTable;
    private DefaultTableModel tableModel;
    private AcaoDAO acaoDAO;

    public FrmAcoesNaoVendidas() {
        super("Ações Não Vendidas", true, true, true, true);
        
        acaoDAO = new AcaoDAO();
        setSize(600, 400);
        
        setupAcoesTable();
        loadAcoesData();
    }

    private void setupAcoesTable() {
        String[] colunas = {"ID", "Ativo", "Quantidade", "Preço Compra (R$)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        acoesTable = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(acoesTable);
        
        JButton btnAtualizar = new JButton("Atualizar Dados");
        btnAtualizar.addActionListener(e -> loadAcoesData());

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.add(btnAtualizar, BorderLayout.WEST);

        setLayout(new BorderLayout());
        add(panelNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadAcoesData() {
        tableModel.setRowCount(0);

        List<Acao> acoes = acaoDAO.obterAcoesNaoVendidas();

        for (Acao acao : acoes) {
            tableModel.addRow(new Object[]{
                acao.getId(),
                acao.getAtivo(),
                acao.getQuantidade(),
                String.format("%.2f", acao.getPrecoCompra())
            });
        }
    }
}