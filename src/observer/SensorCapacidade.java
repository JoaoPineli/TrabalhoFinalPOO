package observer;

import modelo.Lixeira;
import java.util.ArrayList;
import java.util.List;

public class SensorCapacidade implements SensorLixeira {
    private final List<Observer> observers;
    private double nivel;
    private final double limiteNotificacao;
    private final Lixeira lixeira;

    public SensorCapacidade(Lixeira lixeira, double limiteNotificacao) {
        this.observers = new ArrayList<>();
        this.nivel = 0;
        this.limiteNotificacao = limiteNotificacao;
        this.lixeira = lixeira;
    }

    @Override
    public void registrarObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificar() {
        for (Observer observer : observers) {
            observer.atualizar(lixeira);
        }
    }

    @Override
    public double getNivel() {
        return nivel;
    }

    @Override
    public void setNivel(double nivel) {
        this.nivel = Math.min(100, Math.max(0, nivel));
        if (this.nivel >= limiteNotificacao) {
            notificar();
        }
    }

    public void simularEnchimento(double incremento) {
        setNivel(nivel + incremento);
    }
}