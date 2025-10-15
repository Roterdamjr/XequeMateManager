package view;


import util.Relatorio;

import javax.swing.*;

import model.TipoOperacaoEnum;

import java.awt.*;
import java.util.List;

public class FrmRelatorio extends JInternalFrame {


	private static final long serialVersionUID = 1L;
	private JTextArea textAreaRelatorio;
    TipoOperacaoEnum tipoOperacao = TipoOperacaoEnum.GANHA_GANHA;
    
    public FrmRelatorio() {
        super("Relatório de Resumo das Operações Abertas", true, true, true, true);
        
        setSize(700, 500);
        
        setupUI();
        executarCarregamento(() -> {
            return Relatorio.gerarRelatorio(tipoOperacao, true);
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
        	executarCarregamento(() -> {
                return Relatorio.gerarRelatorio(tipoOperacao,true);
            });
        });
        
        rbFechadas.addActionListener(e -> {
        	executarCarregamento(() -> {
  
                return Relatorio.gerarRelatorio(tipoOperacao,false);
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
            JFrame frame = new JFrame("Teste FrmResumoOperacoes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);

            JDesktopPane desktopPane = new JDesktopPane();
            desktopPane.setBackground(Color.LIGHT_GRAY);
            frame.setContentPane(desktopPane);

            FrmRelatorio internalFrame = new FrmRelatorio();
            desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true); 
            } catch (java.beans.PropertyVetoException ignored) {
 
            }

            frame.setVisible(true);
        });
    }
}
