package view;


import util.Relatorio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmResumo extends JInternalFrame {

    private JTextArea textAreaRelatorio;

    public FrmResumo() {
        super("Relatório de Resumo das Operações Abertas", true, true, true, true);
        
        setSize(1000, 700);
        
        setupUI();
        loadRelatorioData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Área de texto para exibir o relatório
        textAreaRelatorio = new JTextArea();
        textAreaRelatorio.setEditable(false);
        // Usa uma fonte monoespaçada para garantir que a formatação do relatório fique alinhada
        textAreaRelatorio.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textAreaRelatorio);
        add(scrollPane, BorderLayout.CENTER);
        
        // Botão Atualizar
        JButton btnAtualizar = new JButton("Atualizar Relatório");
        btnAtualizar.addActionListener(e -> loadRelatorioData());

        JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorth.add(btnAtualizar);

        add(panelNorth, BorderLayout.NORTH);
    }

    public void loadRelatorioData() {
        textAreaRelatorio.setText("Carregando relatório...");
        
        try {
            // Chama o novo método que retorna a lista de strings
            List<String> linhas = Relatorio.gerarResumoOperacoesNaoVendidas();
            
            // Converte a lista de strings em uma única string com quebras de linha
            textAreaRelatorio.setText(String.join("\n", linhas));

        } catch (Exception e) {
            textAreaRelatorio.setText("Erro ao carregar o relatório:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // =========================================================
    // BLOCO DE CÓDIGO PARA TESTE (RUN AS JAVA APPLICATION)
    // =========================================================
    public static void main(String[] args) {
        // Garante que a GUI seja construída no Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Cria o Frame principal (o wrapper)
            JFrame frame = new JFrame("Teste FrmResumoOperacoes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            
            // 2. Adiciona um JDesktopPane para simular o ambiente FrmPrincipal
            JDesktopPane desktopPane = new JDesktopPane();
            desktopPane.setBackground(Color.LIGHT_GRAY);
            frame.setContentPane(desktopPane);
            
            // 3. Cria e adiciona o InternalFrame que queremos testar
            FrmResumo internalFrame = new FrmResumo();
            desktopPane.add(internalFrame);
            
            // 4. Exibe e centraliza o InternalFrame
            internalFrame.setVisible(true);
            try {
                // Tenta maximizar o InternalFrame para melhor visualização
                internalFrame.setMaximum(true); 
            } catch (java.beans.PropertyVetoException ignored) {
                // Caso não consiga maximizar, apenas ignora
            }
            
            // 5. Exibe o Frame principal
            frame.setVisible(true);
        });
    }
}
