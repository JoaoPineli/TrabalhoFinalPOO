package gui;

import javax.swing.*;
import java.awt.*;
import infra.GerenciadorDeColeta;
import modelo.*;
import estrategia.*;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private PainelMonitoramento painelMonitoramento;
    private PainelControle painelControle;
    private PainelRelatorios painelRelatorios;
    private GerenciadorDeColeta gerenciador;

    public MainWindow() {
        gerenciador = GerenciadorDeColeta.getInstancia();
        inicializarComponentes();
        criarZonasDeExemplo();
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Coleta de Lixo Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();
        
        painelMonitoramento = new PainelMonitoramento(gerenciador);
        painelControle = new PainelControle(gerenciador, painelMonitoramento);
        painelRelatorios = new PainelRelatorios(gerenciador);

        tabbedPane.addTab("Monitoramento", new ImageIcon(), painelMonitoramento, "Visualizar status das lixeiras");
        tabbedPane.addTab("Controle", new ImageIcon(), painelControle, "Controlar simulação e coletas");
        tabbedPane.addTab("Relatórios", new ImageIcon(), painelRelatorios, "Gerar e visualizar relatórios");

        add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemSair);
        
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        
        menuBar.add(menuArquivo);
        menuBar.add(menuAjuda);
        setJMenuBar(menuBar);
    }

    private void criarZonasDeExemplo() {
        ZonaDeColeta zonaCentro = new ZonaDeColetaBuilder()
            .setNome("Centro da Cidade")
            .setLimiteNotificacao(75.0)
            .addLixeiras(TipoResiduo.RECICLAVEL, 3)
            .addLixeiras(TipoResiduo.GERAL, 2)
            .addCaminhao(new ColetaAgendada())
            .addCaminhao(new ColetaUrgente())
            .construir();

        ZonaDeColeta zonaResidencial = new ZonaDeColetaBuilder()
            .setNome("Bairro Residencial")
            .setLimiteNotificacao(80.0)
            .addLixeiras(TipoResiduo.ORGANICO, 4)
            .addLixeiras(TipoResiduo.RECICLAVEL, 2)
            .addCaminhao(new ColetaAgendada())
            .construir();

        ZonaDeColeta zonaIndustrial = new ZonaDeColetaBuilder()
            .setNome("Zona Industrial")
            .setLimiteNotificacao(60.0)
            .addLixeiras(TipoResiduo.PERIGOSO, 2)
            .addLixeiras(TipoResiduo.ELETRONICO, 2)
            .addLixeiras(TipoResiduo.GERAL, 1)
            .addCaminhao(new ColetaUrgente())
            .construir();

        gerenciador.registrarZona(zonaCentro);
        gerenciador.registrarZona(zonaResidencial);
        gerenciador.registrarZona(zonaIndustrial);
    }

    private void mostrarSobre() {
        String mensagem = "Sistema de Coleta de Lixo Inteligente\n\n" +
                         "Trabalho de POO - UFG\n" +
                         "Padrões implementados:\n" +
                         "- Observer\n" +
                         "- Strategy\n" +
                         "- Builder\n" +
                         "- Factory Method\n" +
                         "- Singleton";
        
        JOptionPane.showMessageDialog(this, mensagem, "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}