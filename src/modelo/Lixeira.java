package modelo;

import observer.SensorLixeira;

public class Lixeira {
    private final String id;
    private final TipoResiduo tipo;
    private final double capacidadeTotal;
    private SensorLixeira sensor;

    public Lixeira(String id, TipoResiduo tipo, double capacidadeTotal) {
        this.id = id;
        this.tipo = tipo;
        this.capacidadeTotal = capacidadeTotal;
    }

    public void setSensor(SensorLixeira sensor) {
        this.sensor = sensor;
    }

    public double getNivel() {
        return sensor != null ? sensor.getNivel() : 0;
    }

    public void esvaziar() {
        if (sensor != null) {
            sensor.setNivel(0);
        }
    }

    public String getId() {
        return id;
    }

    public TipoResiduo getTipo() {
        return tipo;
    }

    public double getCapacidadeTotal() {
        return capacidadeTotal;
    }

    public SensorLixeira getSensor() {
        return sensor;
    }

    @Override
    public String toString() {
        return String.format("Lixeira[id=%s, tipo=%s, nível=%.1f%%]", 
            id, tipo.getNome(), getNivel());
    }
}