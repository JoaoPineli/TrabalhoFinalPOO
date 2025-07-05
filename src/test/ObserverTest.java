package test;

import modelo.Lixeira;
import modelo.TipoResiduo;
import modelo.CaminhaoColetor;
import observer.SensorCapacidade;
import estrategia.ColetaUrgente;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObserverTest {
    
    private Lixeira lixeira;
    private SensorCapacidade sensor;
    private CaminhaoColetor caminhao;
    
    @Before
    public void setUp() {
        lixeira = new Lixeira("L2-TEST", TipoResiduo.ORGANICO, 100.0);
        sensor = new SensorCapacidade(lixeira, 75.0);
        lixeira.setSensor(sensor);
        caminhao = new CaminhaoColetor("CAM-TEST", new ColetaUrgente());
    }
    
    @Test
    public void testRegistrarObserver() {
        sensor.registrarObserver(caminhao);
        
        int coletasAntes = caminhao.getColetasRealizadas();
        sensor.setNivel(80.0);
        
        assertEquals(coletasAntes + 1, caminhao.getColetasRealizadas());
    }
    
    @Test
    public void testNotificacaoApenasAcimaDoLimite() {
        sensor.registrarObserver(caminhao);
        
        int coletasAntes = caminhao.getColetasRealizadas();
        sensor.setNivel(70.0);
        
        assertEquals(coletasAntes, caminhao.getColetasRealizadas());
    }
    
    @Test
    public void testRemoverObserver() {
        sensor.registrarObserver(caminhao);
        sensor.removerObserver(caminhao);
        
        int coletasAntes = caminhao.getColetasRealizadas();
        sensor.setNivel(90.0);
        
        assertEquals(coletasAntes, caminhao.getColetasRealizadas());
    }
    
    @Test
    public void testMultiplosObservers() {
        CaminhaoColetor caminhao2 = new CaminhaoColetor("CAM2-TEST", new ColetaUrgente());
        
        sensor.registrarObserver(caminhao);
        sensor.registrarObserver(caminhao2);
        
        sensor.setNivel(85.0);
        
        assertEquals(1, caminhao.getColetasRealizadas());
        assertEquals(1, caminhao2.getColetasRealizadas());
    }
}