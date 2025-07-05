package infra;

import modelo.ZonaDeColeta;
import modelo.CaminhaoColetor;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeColeta {
    private static GerenciadorDeColeta instancia;
    private final List<ZonaDeColeta> zonas;
    private final DateTimeFormatter formatter;
    private boolean simulacaoAtiva;

    private GerenciadorDeColeta() {
        this.zonas = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.simulacaoAtiva = false;
    }

    public static synchronized GerenciadorDeColeta getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorDeColeta();
        }
        return instancia;
    }

    public void registrarZona(ZonaDeColeta zona) {
        zonas.add(zona);
        System.out.println("Zona '" + zona.getNome() + "' registrada no gerenciador");
    }

    public void iniciarSimulacao() {
        simulacaoAtiva = true;
        System.out.println("\nSIMULAÇÃO INICIADA!");
        System.out.println("Horário: " + LocalDateTime.now().format(formatter));
        System.out.println("Zonas ativas: " + zonas.size());
    }

    public void pararSimulacao() {
        simulacaoAtiva = false;
        System.out.println("\nSIMULAÇÃO PARADA!");
    }

    public void gerarRelatorio(String nomeArquivo) throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write("=== RELATÓRIO DE COLETA DE LIXO ===\n");
            writer.write("Gerado em: " + LocalDateTime.now().format(formatter) + "\n\n");
            
            int totalLixeiras = 0;
            int totalCaminhoes = 0;
            int totalColetas = 0;
            
            for (ZonaDeColeta zona : zonas) {
                writer.write("ZONA: " + zona.getNome() + "\n");
                writer.write("Lixeiras: " + zona.getLixeiras().size() + "\n");
                writer.write("Caminhões: " + zona.getCaminhoes().size() + "\n");
                
                totalLixeiras += zona.getLixeiras().size();
                totalCaminhoes += zona.getCaminhoes().size();
                
                for (CaminhaoColetor caminhao : zona.getCaminhoes()) {
                    writer.write("  Caminhão " + caminhao.getId() + 
                        " - Coletas realizadas: " + caminhao.getColetasRealizadas() + "\n");
                    totalColetas += caminhao.getColetasRealizadas();
                }
                
                writer.write("\n");
            }
            
            writer.write("=== RESUMO GERAL ===\n");
            writer.write("Total de zonas: " + zonas.size() + "\n");
            writer.write("Total de lixeiras: " + totalLixeiras + "\n");
            writer.write("Total de caminhões: " + totalCaminhoes + "\n");
            writer.write("Total de coletas realizadas: " + totalColetas + "\n");
            
            System.out.println("Relatório salvo em: " + nomeArquivo);
        }
    }

    public void exibirStatusGeral() {
        System.out.println("\n=== STATUS GERAL DO SISTEMA ===");
        System.out.println("Zonas registradas: " + zonas.size());
        System.out.println("Simulação ativa: " + (simulacaoAtiva ? "SIM" : "NÃO"));
        
        for (ZonaDeColeta zona : zonas) {
            zona.exibirStatus();
        }
    }

    public List<ZonaDeColeta> getZonas() {
        return new ArrayList<>(zonas);
    }

    public boolean isSimulacaoAtiva() {
        return simulacaoAtiva;
    }
}