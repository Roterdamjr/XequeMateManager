package test;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.FrmOperacoesConsolidadas;

public class TestOperacoesConsolidadas {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            JFrame testFrame = new JFrame("Teste FrmOperacoesConsolidadas");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(700, 600);
            testFrame.setLocationRelativeTo(null);

            JDesktopPane desktopPane = new JDesktopPane();
            testFrame.setContentPane(desktopPane);

            FrmOperacoesConsolidadas frame = new FrmOperacoesConsolidadas();
            
            frame.setSize(550, 450);
            frame.setLocation(20, 20); 

            desktopPane.add(frame);

            frame.setVisible(true);

            testFrame.setVisible(true);
        });
    }
}