package view;

import javax.swing.*;
import model.TipoOperacaoEnum;
import relat.BaseRelatorio;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public abstract class BaseFrmRelatorio extends JInternalFrame {

	private static final long serialVersionUID = 1L;
    protected JTabbedPane tabbedPaneRelatorios; 
    private final String titulo; // Novo atributo para armazenar o título
    
    // Construtor alterado para receber o título como argumento
    public BaseFrmRelatorio(String titulo) {
        // Agora, o título é passado diretamente para o construtor da superclasse JInternalFrame
        super(titulo, true, true, true, true);
        this.titulo = titulo;
        
        setSize(800, 600);
        
        setupUI();
        carregarAbasRelatorios();
    }
    
    // --- MÉTODOS ABSTRATOS (O getTituloFrame foi removido) ---
    
    /**
     * Retorna a instância concreta da classe de relatório (Abertas ou Fechadas).
     */
    protected abstract BaseRelatorio getBaseRelatorio(TipoOperacaoEnum tipoOperacao);

    // --- Getter (Substitui o método abstrato de antes) ---
    public String getTituloFrame() {
        return titulo;
    }

    // ... (restante da classe setupUI, carregarAbasRelatorios, executarCarregamento permanece inalterado)

    private void setupUI() {
        getContentPane().setLayout(new BorderLayout());

        tabbedPaneRelatorios = new JTabbedPane();
        
        getContentPane().add(tabbedPaneRelatorios, BorderLayout.CENTER);
    }

    private void carregarAbasRelatorios() {
        for (TipoOperacaoEnum tipo : TipoOperacaoEnum.values()) {
            
            JTextArea textAreaRelatorio = new JTextArea();
            textAreaRelatorio.setEditable(false);
            textAreaRelatorio.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textAreaRelatorio);

            String nomeAba = tipo.name().replace("_", " ");
            
            tabbedPaneRelatorios.addTab(nomeAba, scrollPane);

            executarCarregamento(textAreaRelatorio, () -> {
                BaseRelatorio relatorio = getBaseRelatorio(tipo);
                return relatorio.gerarRelatorio();
            });
        }
    }

    private void executarCarregamento(JTextArea targetArea, Supplier<List<String>> reportGenerator) {
        targetArea.setText("Carregando relatório...");

        try {
            List<String> linhas = reportGenerator.get();
            String textoFinal = String.join("\n", linhas);
            targetArea.setText(textoFinal);
        } catch (Exception e) {
            String textoErro = "Erro ao carregar o relatório:\n" + e.getMessage();
            targetArea.setText(textoErro);
            e.printStackTrace();
        }
    }
}