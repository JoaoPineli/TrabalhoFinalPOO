package test;

import modelo.Lixeira;
import modelo.TipoResiduo;
import observer.SensorCapacidade;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LixeiraTest {
    
    private Lixeira lixeira;
    private SensorCapacidade sensor;
    
    @Before
    public void setUp() {
        lixeira = new Lixeira("L1-TEST", TipoResiduo.RECICLAVEL, 100.0);
        sensor = new SensorCapacidade(lixeira, 80.0);
        lixeira.setSensor(sensor);
    }
    
    @Test
    public void testCriacaoLixeira() {
        assertEquals("L1-TEST", lixeira.getId());
        assertEquals(TipoResiduo.RECICLAVEL, lixeira.getTipo());
        assertEquals(100.0, lixeira.getCapacidadeTotal(), 0.01);
        assertEquals(0.0, lixeira.getNivel(), 0.01);
    }
    
    @Test
    public void testEnchimentoLixeira() {
        sensor.setNivel(50.0);
        assertEquals(50.0, lixeira.getNivel(), 0.01);
        
        sensor.simularEnchimento(30.0);
        assertEquals(80.0, lixeira.getNivel(), 0.01);
    }
    
    @Test
    public void testEsvaziarLixeira() {
        sensor.setNivel(75.0);
        lixeira.esvaziar();
        assertEquals(0.0, lixeira.getNivel(), 0.01);
    }
    
    @Test
    public void testLimiteMaximo() {
        sensor.setNivel(150.0);
        assertEquals(100.0, lixeira.getNivel(), 0.01);
    }
    
    @Test
    public void testLimiteMinimo() {
        sensor.setNivel(-50.0);
        assertEquals(0.0, lixeira.getNivel(), 0.01);
    }
}