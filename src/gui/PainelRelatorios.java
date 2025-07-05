package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import infra.GerenciadorDeColeta;
import modelo.*;

public class PainelRelatorios extends JPanel {
    private static final long serialVersionUID = 1L;
    private GerenciadorDeColeta gerenciador;
    private JTextArea areaRelatorio;
    private JTextField campoNomeArquivo;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public PainelRelatorios(GerenciadorDeColeta gerenciador) {
        this.gerenciador = gerenciador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        painelSuperior.add(new JLabel("Nome do arquivo:"));
        campoNomeArquivo = new JTextField(20);
        campoNomeArquivo.setText("relatorio_coleta");
        painelSuperior.add(campoNomeArquivo);
        
        JButton btnGerar = new JButton("Gerar Relatório");
        btnGerar.addActionListener(e -> gerarRelatorio());
        painelSuperior.add(btnGerar);
        
        JButton btnSalvar = new JButton("Salvar em Arquivo");
        btnSalvar.addActionListener(e -> salvarRelatorio());
        painelSuperior.add(btnSalvar);
        
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> areaRelatorio.setText(""));
        painelSuperior.add(btnLimpar);
        
        add(painelSuperior, BorderLayout.NORTH);
        
        areaRelatorio = new JTextArea();
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRelatorio.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaRelatorio);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Relatório"));
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel painelInfo = new JPanel();
        painelInfo.add(new JLabel("💡 Dica: Gere o relatório para visualizar estatísticas detalhadas do sistema"));
        add(painelInfo, BorderLayout.SOUTH);
    }

    private void gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();
        
        relatorio.append("=== RELATÓRIO DE COLETA DE LIXO ===\n");
        relatorio.append("Gerado em: ").append(LocalDateTime.now().format(formatter)).append("\n\n");
        
        int totalLixeiras = 0;
        int totalCaminhoes = 0;
        int totalColetas = 0;
        int lixeirasCheias = 0;
        int lixeirasQuaseCheias = 0;
        
        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            relatorio.append("=====================================\n");
            relatorio.append("ZONA: ").append(zona.getNome()).append("\n");
            relatorio.append("=====================================\n");
            relatorio.append("Lixeiras: ").append(zona.getLixeiras().size()).append("\n");
            relatorio.append("Caminhões: ").append(zona.getCaminhoes().size()).append("\n\n");
            
            relatorio.append("--- Detalhes das Lixeiras ---\n");
            for (Lixeira lixeira : zona.getLixeiras()) {
                double nivel = lixeira.getNivel();
                String status = nivel >= 80 ? "CHEIO" : nivel >= 60 ? "Quase cheio" : "Normal";
                
                relatorio.append(String.format("  %s - Tipo: %s - Nível: %.1f%% - Status: %s\n",
                    lixeira.getId(), lixeira.getTipo().getNome(), nivel, status));
                
                if (nivel >= 80) lixeirasCheias++;
                else if (nivel >= 60) lixeirasQuaseCheias++;
            }
            
            relatorio.append("\n--- Detalhes dos Caminhões ---\n");
            for (CaminhaoColetor caminhao : zona.getCaminhoes()) {
                relatorio.append(String.format("  %s - Estratégia: %s - Coletas realizadas: %d\n",
                    caminhao.getId(), 
                    caminhao.getEstrategiaColeta().getNomeEstrategia(),
                    caminhao.getColetasRealizadas()));
                totalColetas += caminhao.getColetasRealizadas();
            }
            
            totalLixeiras += zona.getLixeiras().size();
            totalCaminhoes += zona.getCaminhoes().size();
            relatorio.append("\n");
        }
        
        relatorio.append("=====================================\n");
        relatorio.append("=== RESUMO GERAL ===\n");
        relatorio.append("=====================================\n");
        relatorio.append("Total de zonas: ").append(gerenciador.getZonas().size()).append("\n");
        relatorio.append("Total de lixeiras: ").append(totalLixeiras).append("\n");
        relatorio.append("  - Cheias (>80%): ").append(lixeirasCheias).append("\n");
        relatorio.append("  - Quase cheias (60-80%): ").append(lixeirasQuaseCheias).append("\n");
        relatorio.append("  - Normais (<60%): ").append(totalLixeiras - lixeirasCheias - lixeirasQuaseCheias).append("\n");
        relatorio.append("Total de caminhões: ").append(totalCaminhoes).append("\n");
        relatorio.append("Total de coletas realizadas: ").append(totalColetas).append("\n");
        
        double eficiencia = totalLixeiras > 0 ? 
            (100.0 - (lixeirasCheias * 100.0 / totalLixeiras)) : 100.0;
        relatorio.append(String.format("\nEficiência do sistema: %.1f%%\n", eficiencia));
        
        if (lixeirasCheias > 0) {
            relatorio.append("\nATENÇÃO: Existem lixeiras cheias que precisam de coleta urgente!\n");
        }
        
        areaRelatorio.setText(relatorio.toString());
    }

    private void salvarRelatorio() {
        if (areaRelatorio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Gere um relatório antes de salvar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nomeArquivo = campoNomeArquivo.getText();
        if (!nomeArquivo.endsWith(".txt")) {
            nomeArquivo += ".txt";
        }
        
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write(areaRelatorio.getText());
            JOptionPane.showMessageDialog(this, 
                "Relatório salvo em: " + nomeArquivo, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar arquivo: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}