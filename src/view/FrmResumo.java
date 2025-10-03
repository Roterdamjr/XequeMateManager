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
        executarCarregamento(() -> {
            // Chamamos gerarResumoOperacoes passando 'true' como argumento
            return Relatorio.gerarResumoOperacoes(true);
        });
    }

    private void setupUI() {
        getContentPane().setLayout(new BorderLayout());

        textAreaRelatorio = new JTextArea();
        textAreaRelatorio.setEditable(false);
        textAreaRelatorio.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textAreaRelatorio);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Painel para a parte superior
        JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        getContentPane().add(panelNorth, BorderLayout.NORTH);
        
        JRadioButton rbAbertas = new JRadioButton("Operações Abertas");
        JRadioButton rbFechadas = new JRadioButton("Operações Fechadas");
        
        ButtonGroup grupoOperacoes = new ButtonGroup();
        grupoOperacoes.add(rbAbertas);
        grupoOperacoes.add(rbFechadas);
        
        rbAbertas.setSelected(true);
        
        panelNorth.add(rbAbertas);
        panelNorth.add(rbFechadas);
        
        rbAbertas.addActionListener(e -> {
            // A lambda (e -> { ... }) chama o executarCarregamento, que espera
            // uma outra lambda (ou interface funcional) sem argumentos.
        	executarCarregamento(() -> {
                // Chamamos gerarResumoOperacoes passando 'true' como argumento
                return Relatorio.gerarResumoOperacoes(true);
            });
        });
        
        rbFechadas.addActionListener(e -> {
            // A lambda (e -> { ... }) chama o executarCarregamento, que espera
            // uma outra lambda (ou interface funcional) sem argumentos.
        	executarCarregamento(() -> {
                // Chamamos gerarResumoOperacoes passando 'true' como argumento
                return Relatorio.gerarResumoOperacoes(false);
            });
        });
    }

    private void executarCarregamento(java.util.function.Supplier<List<String>> reportGenerator) {
        textAreaRelatorio.setText("Carregando relatório...");

        try {
            List<String> linhas = reportGenerator.get();
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
