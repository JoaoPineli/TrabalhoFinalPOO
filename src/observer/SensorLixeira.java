package observer;

public interface SensorLixeira {
    void registrarObserver(Observer observer);
    void removerObserver(Observer observer);
    void notificar();
    double getNivel();
    void setNivel(double nivel);
}