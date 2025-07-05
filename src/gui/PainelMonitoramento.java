package gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import infra.GerenciadorDeColeta;
import modelo.*;

public class PainelMonitoramento extends JPanel {
    private static final long serialVersionUID = 1L;
    private GerenciadorDeColeta gerenciador;
    private JPanel painelLixeiras;
    private Map<String, LixeiraPanel> lixeiraPanels;
    private Timer timerAtualizacao;

    public PainelMonitoramento(GerenciadorDeColeta gerenciador) {
        this.gerenciador = gerenciador;
        this.lixeiraPanels = new HashMap<>();
        inicializarComponentes();
        iniciarAtualizacaoAutomatica();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Monitoramento de Lixeiras em Tempo Real", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        painelLixeiras = new JPanel();
        painelLixeiras.setLayout(new GridLayout(0, 3, 10, 10));
        painelLixeiras.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(painelLixeiras);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar Agora");
        btnAtualizar.addActionListener(e -> atualizarPaineis());
        painelRodape.add(btnAtualizar);
        add(painelRodape, BorderLayout.SOUTH);
    }

    public void atualizarPaineis() {
        painelLixeiras.removeAll();
        lixeiraPanels.clear();

        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            for (Lixeira lixeira : zona.getLixeiras()) {
                LixeiraPanel panel = new LixeiraPanel(lixeira, zona.getNome());
                lixeiraPanels.put(lixeira.getId(), panel);
                painelLixeiras.add(panel);
            }
        }

        painelLixeiras.revalidate();
        painelLixeiras.repaint();
    }

    private void iniciarAtualizacaoAutomatica() {
        timerAtualizacao = new Timer(1000, e -> {
            for (LixeiraPanel panel : lixeiraPanels.values()) {
                panel.atualizar();
            }
        });
        timerAtualizacao.start();
    }

    private class LixeiraPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Lixeira lixeira;
        private JProgressBar barraProgresso;
        private JLabel lblNivel;
        private JLabel lblStatus;

        public LixeiraPanel(Lixeira lixeira, String nomeZona) {
            this.lixeira = lixeira;
            inicializarComponentes(nomeZona);
        }

        private void inicializarComponentes(String nomeZona) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JPanel painelTopo = new JPanel(new GridLayout(3, 1));
            painelTopo.add(new JLabel(lixeira.getId(), SwingConstants.CENTER));
            painelTopo.add(new JLabel(nomeZona, SwingConstants.CENTER));
            
            JLabel lblTipo = new JLabel(lixeira.getTipo().getNome(), SwingConstants.CENTER);
            lblTipo.setFont(new Font("Arial", Font.BOLD, 14));
            painelTopo.add(lblTipo);
            
            add(painelTopo, BorderLayout.NORTH);

            barraProgresso = new JProgressBar(0, 100);
            barraProgresso.setStringPainted(true);
            barraProgresso.setPreferredSize(new Dimension(200, 30));
            add(barraProgresso, BorderLayout.CENTER);

            JPanel painelInfo = new JPanel(new GridLayout(2, 1));
            lblNivel = new JLabel("Nível: 0%", SwingConstants.CENTER);
            lblStatus = new JLabel("Status: Normal", SwingConstants.CENTER);
            painelInfo.add(lblNivel);
            painelInfo.add(lblStatus);
            add(painelInfo, BorderLayout.SOUTH);

            atualizar();
        }

        public void atualizar() {
            double nivel = lixeira.getNivel();
            barraProgresso.setValue((int) nivel);
            lblNivel.setText(String.format("Nível: %.1f%%", nivel));

            if (nivel >= 80) {
                barraProgresso.setForeground(Color.RED);
                lblStatus.setText("Status: CHEIO!");
                lblStatus.setForeground(Color.RED);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 3),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            } else if (nivel >= 60) {
                barraProgresso.setForeground(Color.ORANGE);
                lblStatus.setText("Status: Quase cheio");
                lblStatus.setForeground(Color.ORANGE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.ORANGE, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            } else {
                barraProgresso.setForeground(Color.GREEN);
                lblStatus.setText("Status: Normal");
                lblStatus.setForeground(Color.GREEN);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        }
    }
}