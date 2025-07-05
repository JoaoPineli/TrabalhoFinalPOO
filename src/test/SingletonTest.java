package test;

import infra.GerenciadorDeColeta;
import modelo.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingletonTest {
    
    @Before
    public void setUp() {
        GerenciadorDeColeta gerenciador = GerenciadorDeColeta.getInstancia();
        gerenciador.getZonas().clear();
    }
    
    @Test
    public void testInstanciaUnica() {
        GerenciadorDeColeta instancia1 = GerenciadorDeColeta.getInstancia();
        GerenciadorDeColeta instancia2 = GerenciadorDeColeta.getInstancia();
        
        assertSame(instancia1, instancia2);
    }
    
    @Test
    public void testRegistrarZona() {
        GerenciadorDeColeta gerenciador = GerenciadorDeColeta.getInstancia();
        
        ZonaDeColeta zona = new ZonaDeColetaBuilder()
            .setNome("Zona Singleton")
            .construir();
        
        int zonasAntes = gerenciador.getZonas().size();
        gerenciador.registrarZona(zona);
        
        assertEquals(zonasAntes + 1, gerenciador.getZonas().size());
    }
    
    @Test
    public void testEstadoSimulacao() {
        GerenciadorDeColeta gerenciador = GerenciadorDeColeta.getInstancia();
        
        assertFalse(gerenciador.isSimulacaoAtiva());
        
        gerenciador.iniciarSimulacao();
        assertTrue(gerenciador.isSimulacaoAtiva());
        
        gerenciador.pararSimulacao();
        assertFalse(gerenciador.isSimulacaoAtiva());
    }
}