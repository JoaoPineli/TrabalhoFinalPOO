package modelo;

import java.util.ArrayList;
import java.util.List;

public class ZonaDeColeta {
    private final String nome;
    private final List<Lixeira> lixeiras;
    private final List<CaminhaoColetor> caminhoes;

    public ZonaDeColeta(String nome, List<Lixeira> lixeiras, List<CaminhaoColetor> caminhoes) {
        this.nome = nome;
        this.lixeiras = new ArrayList<>(lixeiras);
        this.caminhoes = new ArrayList<>(caminhoes);
    }

    public void adicionarLixeira(Lixeira lixeira) {
        lixeiras.add(lixeira);
    }

    public void adicionarCaminhao(CaminhaoColetor caminhao) {
        caminhoes.add(caminhao);
    }

    public void registrarCaminhoesNasLixeiras() {
        for (Lixeira lixeira : lixeiras) {
            if (lixeira.getSensor() != null) {
                for (CaminhaoColetor caminhao : caminhoes) {
                    lixeira.getSensor().registrarObserver(caminhao);
                }
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public List<Lixeira> getLixeiras() {
        return new ArrayList<>(lixeiras);
    }

    public List<CaminhaoColetor> getCaminhoes() {
        return new ArrayList<>(caminhoes);
    }

    public void exibirStatus() {
        System.out.println("\n=== ZONA: " + nome + " ===");
        System.out.println("Lixeiras: " + lixeiras.size());
        System.out.println("Caminhões: " + caminhoes.size());
        
        for (Lixeira lixeira : lixeiras) {
            System.out.println("  " + lixeira);
        }
    }
}