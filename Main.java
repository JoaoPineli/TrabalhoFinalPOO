import estrategia.ColetaAgendada;
import estrategia.ColetaUrgente;
import infra.GerenciadorDeColeta;
import infra.SimuladorEnchimento;
import modelo.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final List<SimuladorEnchimento> simuladores = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("BEM-VINDO AO SISTEMA DE COLETA DE LIXO INTELIGENTE!");
        System.out.println("====================================================\n");

        GerenciadorDeColeta gerenciador = GerenciadorDeColeta.getInstancia();

        criarZonasDeExemplo(gerenciador);

        exibirMenu(gerenciador);

        encerrarSimulacao();
    }

    private static void criarZonasDeExemplo(GerenciadorDeColeta gerenciador) {
        System.out.println("🔧 Criando zonas de exemplo...\n");

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

        System.out.println("\n✅ Zonas criadas com sucesso!\n");
    }

    private static void exibirMenu(GerenciadorDeColeta gerenciador) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Iniciar simulação automática");
            System.out.println("2. Parar simulação");
            System.out.println("3. Exibir status geral");
            System.out.println("4. Simular enchimento manual");
            System.out.println("5. Gerar relatório");
            System.out.println("6. Mudar estratégia de coleta");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        iniciarSimulacaoAutomatica(gerenciador);
                        break;
                    case 2:
                        pararSimulacao();
                        break;
                    case 3:
                        gerenciador.exibirStatusGeral();
                        break;
                    case 4:
                        simularEnchimentoManual(gerenciador);
                        break;
                    case 5:
                        gerarRelatorio(gerenciador);
                        break;
                    case 6:
                        mudarEstrategiaColeta(gerenciador);
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("❌ Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void iniciarSimulacaoAutomatica(GerenciadorDeColeta gerenciador) {
        if (gerenciador.isSimulacaoAtiva()) {
            System.out.println("Simulação já está em andamento!");
            return;
        }

        gerenciador.iniciarSimulacao();

        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            for (Lixeira lixeira : zona.getLixeiras()) {
                SimuladorEnchimento simulador = new SimuladorEnchimento(lixeira, 3);
                simuladores.add(simulador);
                executorService.submit(simulador);
            }
        }

        System.out.println("Simulação automática iniciada!");
    }

    private static void pararSimulacao() {
        for (SimuladorEnchimento simulador : simuladores) {
            simulador.parar();
        }
        simuladores.clear();
        GerenciadorDeColeta.getInstancia().pararSimulacao();
        System.out.println("Simulação parada!");
    }

    private static void simularEnchimentoManual(GerenciadorDeColeta gerenciador) {
        System.out.println("\n=== SIMULAÇÃO MANUAL ===");
        
        List<Lixeira> todasLixeiras = new ArrayList<>();
        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            todasLixeiras.addAll(zona.getLixeiras());
        }

        for (int i = 0; i < todasLixeiras.size(); i++) {
            Lixeira lixeira = todasLixeiras.get(i);
            System.out.println((i + 1) + ". " + lixeira);
        }

        System.out.print("Escolha a lixeira (número): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha > 0 && escolha <= todasLixeiras.size()) {
            Lixeira lixeiraSelecionada = todasLixeiras.get(escolha - 1);
            System.out.print("Quanto encher (0-100%): ");
            double incremento = scanner.nextDouble();
            scanner.nextLine();

            lixeiraSelecionada.getSensor().setNivel(
                lixeiraSelecionada.getNivel() + incremento
            );

            System.out.println("Lixeira atualizada! Nível atual: " + 
                String.format("%.1f%%", lixeiraSelecionada.getNivel()));
        } else {
            System.out.println("Escolha inválida!");
        }
    }

    private static void gerarRelatorio(GerenciadorDeColeta gerenciador) {
        System.out.print("Nome do arquivo de relatório: ");
        String nomeArquivo = scanner.nextLine();

        if (!nomeArquivo.endsWith(".txt")) {
            nomeArquivo += ".txt";
        }

        try {
            gerenciador.gerarRelatorio(nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private static void mudarEstrategiaColeta(GerenciadorDeColeta gerenciador) {
        System.out.println("\n=== MUDAR ESTRATÉGIA DE COLETA ===");
        
        List<CaminhaoColetor> todosCaminhoes = new ArrayList<>();
        for (ZonaDeColeta zona : gerenciador.getZonas()) {
            todosCaminhoes.addAll(zona.getCaminhoes());
        }

        for (int i = 0; i < todosCaminhoes.size(); i++) {
            CaminhaoColetor caminhao = todosCaminhoes.get(i);
            System.out.println((i + 1) + ". " + caminhao.getId());
        }

        System.out.print("Escolha o caminhão (número): ");
        int escolhaCaminhao = scanner.nextInt();
        scanner.nextLine();

        if (escolhaCaminhao > 0 && escolhaCaminhao <= todosCaminhoes.size()) {
            CaminhaoColetor caminhaoSelecionado = todosCaminhoes.get(escolhaCaminhao - 1);
            
            System.out.println("Estratégias disponíveis:");
            System.out.println("1. Coleta Agendada");
            System.out.println("2. Coleta Urgente");
            System.out.print("Escolha a estratégia: ");
            
            int escolhaEstrategia = scanner.nextInt();
            scanner.nextLine();

            switch (escolhaEstrategia) {
                case 1:
                    caminhaoSelecionado.setEstrategiaColeta(new ColetaAgendada());
                    break;
                case 2:
                    caminhaoSelecionado.setEstrategiaColeta(new ColetaUrgente());
                    break;
                default:
                    System.out.println("Estratégia inválida!");
            }
        } else {
            System.out.println("Caminhão inválido!");
        }
    }

    private static void encerrarSimulacao() {
        System.out.println("\nEncerrando sistema...");
        
        pararSimulacao();
        
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        
        scanner.close();
        System.out.println("Sistema encerrado com sucesso!");
    }
}