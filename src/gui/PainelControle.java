package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import infra.*;
import modelo.*;
import estrategia.*;
import observer.SensorCapacidade;

public class PainelControle extends JPanel {
    private static final long serialVersionUID = 1L;
    private GerenciadorDeColeta gerenciador;
    private PainelMonitoramento painelMonitoramento;
    private JButton btnIniciarSimulacao;
    private JButton btnPararSimulacao;
    private JComboBox<Lixeira> comboLixeiras;
    private JSpinner spinnerNivel;
    private JComboBox<CaminhaoColetor> comboCaminhoes;
    private JComboBox<String> comboEstrategias;
    private JTextArea logArea;
    
    private ExecutorService executorService;
    private List<SimuladorEnchimento> simuladores;

    public PainelControle(GerenciadorDeColeta gerenciador, PainelMonitoramento painelMonitoramento) {
        this.gerenciador = gerenciador;
        this.painelMonitoramento = painelMonitoramento;
        this.executorService = Executors.newCachedThreadPool();
        this.simuladores = new ArrayList<>();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        JPanel painelSuperior = new JPanel(new GridLayout(1, 3, 10, 10));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        painelSuperior.add(criarPainelSimulacao());
        painelSuperior.add(criarPainelEnchimentoManual());
        painelSuperior.add(criarPainelEstrategia());
        
        add(painelSuperior, BorderLayout.NORTH);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de Eventos"));
        scrollLog.setPreferredSize(new Dimension(0, 300));
        
        add(scrollLog, BorderLayout.CENTER);
    }

    private JPanel criarPainelSimulacao() {
        JPanel painel = new JPanel(new GridLayout(3, 1, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Controle de Simulação"));
        
        btnIniciarSimulacao = new JButton("Iniciar Simulação");
        btnIniciarSimulacao.addActionListener(e -> iniciarSimulacao());
        
        btnPararSimulacao = new JButton("Parar Simulação");
        btnPararSimulacao.setEnabled(false);
        btnPararSimulacao.addActionListener(e -> pararSimulacao());
        
        JButton btnStatus = new JButton("Ver Status Geral");
        btnStatus.addActionListener(e -> mostrarStatus());
        
        painel.add(btnIniciarSimulacao);
        painel.add(btnPararSimulacao);
        painel.add(btnStatus);
        
        return painel;
    }

    private JPanel criarPainelEnchimentoManual() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Enchimento Manual"));
        
        comboLixeiras = new JComboBox<>();
        atualizarComboLixeiras();
        
        spinnerNivel = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 100.0, 5.0));
        
        JButton btnEncher = new JButton("Adicionar Lixo");
        btnEncher.addActionListener(e -> encherLixeiraManual());
        
        JButton btnEsvaziar = new JButton("Esvaziar Lixeira");
        btnEsvaziar.addActionListener(e -> esvaziarLixeira());
        
        painel.add(comboLixeiras);
        painel.add(new JLabel("Quantidade (%):"));
        painel.add(spinnerNivel);
        painel.add(btnEncher);
        painel.add(btnEsvaziar);
        
        return painel;
    }

    private JPanel criarPainelEstrategia() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Estratégia de Coleta"));
        
        comboCaminhoes = new JComboBox<>();
        atualizarComboCaminhoes();
        
        comboEstrategias = new JComboBox<>(new String[]{"Coleta Agendada", "Coleta Urgente"});
        
        JButton btnMudar = new JButton("Mudar Estratégia");
        btnMudar.addActionListener(e -> mudarEstrategia());
        
        painel.add(new JLabel("Caminhão:"));
        painel.add(comboCaminhoes);
        painel.add(new JLabel("Nova Estratégia:"));
        painel.add(comboEstrategias);
        painel.add(btnMudar);
        
        return painel;
    }

    private void iniciarSimulacao() {
        if (!gerenciador.isSimulacaoAtiva()) {
            gerenciador.iniciarSimulacao();
            
            for (ZonaDeColeta zona : gerenciador.getZonas()) {
                for (Lixeira lixeira : zona.getLixeiras()) {
                    SimuladorEnchimento simulador = new SimuladorEnchimento(lixeira, 2);
                    simuladores.add(simulador);
                    executorService.submit(simulador);
                }
            }
            
            btnIniciarSimulacao.setEnabled(false);
            btnPararSimulacao.setEnabled(true);
            
            painelMonitoramento.atualizarPaineis();
            
            log("Simulação automática iniciada!");
        }
    }

    private void pararSimulacao() {
        for (SimuladorEnchimento simulador : simuladores) {
            simulador.parar();
        }
        simuladores.clear();
        gerenciador.pararSimulacao();
        
        btnIniciarSimulacao.setEnabled(true);
        btnPararSimulacao.setEnabled(false);
        
        log("Simulação parada!");
    }

    private void encherLixeiraManual() {
        Lixeira lixeira = (Lixeira) comboLixeiras.getSelectedItem();
        if (lixeira != null) {
            double incremento = (Double) spinnerNivel.getValue();
            
            if (lixeira.getSensor() instanceof SensorCapacidade) {
                ((SensorCapacidade) lixeira.getSensor()).simularEnchimento(incremento);
                log(String.format("%s encheu %.1f%% - Nível atual: %.1f%%", 
                    lixeira.getId(), incremento, lixeira.getNivel()));
            }
        }
    }

    private void esvaziarLixeira() {
        Lixeira lixeira = (Lixeira) comboLixeiras.getSelectedItem();
        if (lixeira != null) {
            lixeira.esvaziar();
            log(lixeira.getId() + " foi esvaziada!");
        }
    }

    private void mudarEstrategia() {
        CaminhaoColetor caminhao = (CaminhaoColetor) comboCaminhoes.getSelectedItem();
        if (caminhao != null) {
            String estrategiaSelecionada = (String) comboEstrategias.getSelectedItem();
            EstrategiaColeta novaEstrategia = estrategiaSelecionada.equals("Coleta Urgente") 
                ? new ColetaUrgente() : new ColetaAgendada();
            
            caminhao.setEstrategiaColeta(novaEstrategia);
            log(caminhao.getId() + " mudou para " + estrategiaSelecionada);
        }
    }

    private void mostrarStatus() {
        StringBuilder status = new StringBuilder("=== STATUS GERAL ===\n");
        status.append("Zonas registradas: ").append(gerenciador.getZonas().size()).append("\n");
        status.append("Simulação ativa: ").append(gerenciador.isSimulacaoAtiva() ? "SIM" : "NÃO").append("\n\n");
        
        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            status.append("ZONA: ").append(zona.getNome()).append("\n");
            status.append("  Lixeiras: ").append(zona.getLixeiras().size()).append("\n");
            status.append("  Caminhões: ").append(zona.getCaminhoes().size()).append("\n");
            
            for (Lixeira lixeira : zona.getLixeiras()) {
                status.append("    ").append(lixeira).append("\n");
            }
            status.append("\n");
        }
        
        JOptionPane.showMessageDialog(this, status.toString(), "Status Geral", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarComboLixeiras() {
        comboLixeiras.removeAllItems();
        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            for (Lixeira lixeira : zona.getLixeiras()) {
                comboLixeiras.addItem(lixeira);
            }
        }
    }

    private void atualizarComboCaminhoes() {
        comboCaminhoes.removeAllItems();
        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            for (CaminhaoColetor caminhao : zona.getCaminhoes()) {
                comboCaminhoes.addItem(caminhao);
            }
        }
    }

    private void log(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(mensagem + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}