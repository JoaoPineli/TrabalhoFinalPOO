package infra;

import modelo.Lixeira;
import observer.SensorCapacidade;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SimuladorEnchimento implements Runnable {
    private final Lixeira lixeira;
    private final Random random;
    private volatile boolean ativo;
    private final int intervaloSegundos;

    public SimuladorEnchimento(Lixeira lixeira, int intervaloSegundos) {
        this.lixeira = lixeira;
        this.random = new Random();
        this.ativo = true;
        this.intervaloSegundos = intervaloSegundos;
    }

    @Override
    public void run() {
        System.out.println("Simulador iniciado para " + lixeira.getId());
        
        while (ativo && !Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(intervaloSegundos);
                
                if (lixeira.getNivel() < 100) {
                    double incremento = 5 + random.nextDouble() * 15;
                    
                    if (lixeira.getSensor() instanceof SensorCapacidade) {
                        ((SensorCapacidade) lixeira.getSensor()).simularEnchimento(incremento);
                        System.out.println(lixeira.getId() + " encheu " + 
                            String.format("%.1f%%", incremento) + 
                            " - Nível atual: " + String.format("%.1f%%", lixeira.getNivel()));
                    }
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Simulador parado para " + lixeira.getId());
    }

    public void parar() {
        ativo = false;
    }
}