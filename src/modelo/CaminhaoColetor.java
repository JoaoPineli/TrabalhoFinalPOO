package modelo;

import observer.Observer;
import estrategia.EstrategiaColeta;

public class CaminhaoColetor implements Observer {
    private final String id;
    private EstrategiaColeta estrategiaColeta;
    private int coletasRealizadas;

    public CaminhaoColetor(String id, EstrategiaColeta estrategiaColeta) {
        this.id = id;
        this.estrategiaColeta = estrategiaColeta;
        this.coletasRealizadas = 0;
    }

    @Override
    public void atualizar(Lixeira lixeira) {
        System.out.println("\nCaminhão " + id + " notificado!");
        System.out.println("Lixeira " + lixeira.getId() + " atingiu " + 
            String.format("%.1f%%", lixeira.getNivel()) + " de capacidade");
        
        realizarColeta(lixeira);
    }

    public void realizarColeta(Lixeira lixeira) {
        System.out.println("\nCaminhão " + id + " iniciando coleta...");
        System.out.println("Usando estratégia: " + estrategiaColeta.getNomeEstrategia());
        
        estrategiaColeta.coletar(lixeira);
        coletasRealizadas++;
    }

    public void setEstrategiaColeta(EstrategiaColeta estrategiaColeta) {
        this.estrategiaColeta = estrategiaColeta;
        System.out.println("Caminhão " + id + " mudou para estratégia: " + 
            estrategiaColeta.getNomeEstrategia());
    }

    public String getId() {
        return id;
    }

    public int getColetasRealizadas() {
        return coletasRealizadas;
    }

    public EstrategiaColeta getEstrategiaColeta() {
        return estrategiaColeta;
    }
}