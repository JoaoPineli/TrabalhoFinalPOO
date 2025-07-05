package modelo;

import estrategia.EstrategiaColeta;
import observer.SensorCapacidade;
import java.util.ArrayList;
import java.util.List;

public class ZonaDeColetaBuilder {
    private String nome;
    private final List<Lixeira> lixeiras;
    private final List<CaminhaoColetor> caminhoes;
    private double limiteNotificacao = 80.0;

    public ZonaDeColetaBuilder() {
        this.lixeiras = new ArrayList<>();
        this.caminhoes = new ArrayList<>();
    }

    public ZonaDeColetaBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public ZonaDeColetaBuilder setLimiteNotificacao(double limite) {
        this.limiteNotificacao = limite;
        return this;
    }

    public ZonaDeColetaBuilder addLixeira(TipoResiduo tipo) {
        String id = "L" + (lixeiras.size() + 1) + "-" + tipo.name();
        Lixeira lixeira = new Lixeira(id, tipo, 100.0);
        
        SensorCapacidade sensor = new SensorCapacidade(lixeira, limiteNotificacao);
        lixeira.setSensor(sensor);
        
        lixeiras.add(lixeira);
        return this;
    }

    public ZonaDeColetaBuilder addLixeiras(TipoResiduo tipo, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            addLixeira(tipo);
        }
        return this;
    }

    public ZonaDeColetaBuilder addCaminhao(EstrategiaColeta estrategia) {
        String id = "CAM-" + (caminhoes.size() + 1);
        CaminhaoColetor caminhao = new CaminhaoColetor(id, estrategia);
        caminhoes.add(caminhao);
        return this;
    }

    public ZonaDeColeta construir() {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalStateException("Nome da zona é obrigatório");
        }
        
        ZonaDeColeta zona = new ZonaDeColeta(nome, lixeiras, caminhoes);
        zona.registrarCaminhoesNasLixeiras();
        
        return zona;
    }
}