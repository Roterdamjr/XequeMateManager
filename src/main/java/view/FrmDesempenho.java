package view;

import util.Utils;
import javax.swing.*;
import dao.AcaoDAO;
import model.Acao;
import model.TipoOperacaoEnum;
import relat.Desempenho;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FrmDesempenho extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textAreaRelatorio;

    public FrmDesempenho() {
        super("Desempenho", true, true, true, true);
        
        setSize(500, 300);
        
        setupUI();
        
        textAreaRelatorio.setText(""); 
        executarCarregamento(TipoOperacaoEnum.DIVIDENDO3X) ;
        textAreaRelatorio.append("\n\n==========================================\n\n");
        executarCarregamento(TipoOperacaoEnum.GANHA_GANHA) ;
        textAreaRelatorio.append("\n\n==========================================\n\n");
        executarCarregamento(TipoOperacaoEnum.TRES_PRA_UM) ;
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
        
    }

    private void executarCarregamento(TipoOperacaoEnum tipoOperacao) {

		List<Acao> acoes = new AcaoDAO().obterAcoesFechadas(tipoOperacao.getDbValue());
		
		List<Acao> acoesProcessadas = new ArrayList<>();
		for (Acao acao : acoes) { 
		    if (acao != null) {
		        acoesProcessadas.add(acao);
		    }
		}
		
		Map<String, Double> totaisPorMes = Desempenho.calcularlDesempenhoMensal(acoesProcessadas);
		
        Map<String, Double> totaisOrdenados = new TreeMap<>(Utils.MES_ANO_COMPARATOR);
        totaisOrdenados.putAll(totaisPorMes);
        
		List<String> linhas = new ArrayList<String>();
		
		// 1. Título do Relatório
		linhas.add("RELATÓRIO: " + tipoOperacao.getDbValue());
		linhas.add("-------------------------");
		
		double soma=0;
        for (Map.Entry<String, Double> entry : totaisOrdenados.entrySet()) {
            String mes = entry.getKey();
            Double valorTotal = entry.getValue();
            linhas.add( mes + ": " +  
            			Utils.formatarParaDuasDecimais(valorTotal * 100)
            			+"%"
            );
            soma+=valorTotal;
        }
        
        linhas.add("-------------------------"); 
        
        // Verifica se há dados para evitar divisão por zero
        if (totaisOrdenados.size() > 0) {
             linhas.add( "Média : " +  
                        Utils.formatarParaDuasDecimais(soma / totaisOrdenados.size() * 100)
                        +"%"
            );
        } else {
             linhas.add("Média : 0.00%");
             linhas.add("(Sem dados para este tipo de operação)");
        }
       
        
        try {
  
            textAreaRelatorio.append(String.join("\n", linhas));
        } catch (Exception e) {
            textAreaRelatorio.append("\nErro ao carregar o relatório:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
            FrmDesempenho internalFrame = new FrmDesempenho();
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