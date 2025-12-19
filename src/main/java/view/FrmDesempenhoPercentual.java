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
import java.util.stream.Collectors;

public class FrmDesempenhoPercentual extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private JTextArea textAreaRelatorio;

    public FrmDesempenhoPercentual() {
        super("Desempenho Percentual Mensal", true, true, true, true);
        setSize(500, 400);
        setupUI();
        
        // Executa o carregamento para os três tipos
        executarCarregamento(TipoOperacaoEnum.DIVIDENDO3X);
        textAreaRelatorio.append("\n" + "=".repeat(40) + "\n\n");
        executarCarregamento(TipoOperacaoEnum.GANHA_GANHA);
        textAreaRelatorio.append("\n" + "=".repeat(40) + "\n\n");
        executarCarregamento(TipoOperacaoEnum.TRES_PRA_UM);
    }

    private void setupUI() {
        getContentPane().setLayout(new BorderLayout());
        textAreaRelatorio = new JTextArea();
        textAreaRelatorio.setEditable(false);
        // Fonte monoespaçada é crucial para alinhar as colunas de números
        textAreaRelatorio.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textAreaRelatorio.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(textAreaRelatorio);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void executarCarregamento(TipoOperacaoEnum tipoOperacao) {
        List<Acao> acoes = new AcaoDAO().obterAcoesFechadas(tipoOperacao.getDbValue());
        
        // Filtra nulos usando Stream para código mais limpo
        List<Acao> acoesProcessadas = acoes.stream()
                                           .filter(a -> a != null)
                                           .collect(Collectors.toList());

        // Obtemos o consolidado
        Desempenho.DesempenhoConsolidado resultado = Desempenho.calcularlDesempenhoMensal(acoesProcessadas);

        Map<String, Double> totaisPorMesPerc = resultado.percentual;
        
        Map<String, Double> totaisOrdenados = new TreeMap<>(Utils.MES_ANO_COMPARATOR);
        totaisOrdenados.putAll(totaisPorMesPerc);
        
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DE RENTABILIDADE: ").append(tipoOperacao.getDbValue()).append("\n");
        sb.append("------------------------------------------\n");
        
        double somaPercentual = 0;
        for (Map.Entry<String, Double> entry : totaisOrdenados.entrySet()) {
            String mes = entry.getKey();
            Double valorPercentual = entry.getValue() *100;

            // Formatação alinhada: %-10s (mês), %10s (valor)
            sb.append(String.format("%-10s: %10s%%\n", mes, Utils.formatarParaDuasDecimais(valorPercentual)));
            
            somaPercentual += valorPercentual;
        }
        
        sb.append("------------------------------------------\n");
        
        if (!totaisOrdenados.isEmpty()) {
            double media = somaPercentual / totaisOrdenados.size();
            sb.append(String.format("Rentabilidade Média Mensal  : %10s%%\n", Utils.formatarParaDuasDecimais(media)));
        } else {
            sb.append("Sem dados de rentabilidade para este tipo.\n");
        }
        
        textAreaRelatorio.append(sb.toString());
    }
}