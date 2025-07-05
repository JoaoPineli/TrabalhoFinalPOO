package test;

import modelo.*;
import estrategia.ColetaAgendada;
import org.junit.Test;
import static org.junit.Assert.*;

public class BuilderTest {
    
    @Test
    public void testConstrucaoZonaSimples() {
        ZonaDeColeta zona = new ZonaDeColetaBuilder()
            .setNome("Zona Teste")
            .addLixeira(TipoResiduo.ORGANICO)
            .addCaminhao(new ColetaAgendada())
            .construir();
        
        assertEquals("Zona Teste", zona.getNome());
        assertEquals(1, zona.getLixeiras().size());
        assertEquals(1, zona.getCaminhoes().size());
    }
    
    @Test
    public void testConstrucaoZonaCompleta() {
        ZonaDeColeta zona = new ZonaDeColetaBuilder()
            .setNome("Zona Complexa")
            .setLimiteNotificacao(70.0)
            .addLixeiras(TipoResiduo.RECICLAVEL, 3)
            .addLixeiras(TipoResiduo.PERIGOSO, 2)
            .addLixeira(TipoResiduo.GERAL)
            .addCaminhao(new ColetaAgendada())
            .construir();
        
        assertEquals("Zona Complexa", zona.getNome());
        assertEquals(6, zona.getLixeiras().size());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstrucaoSemNome() {
        new ZonaDeColetaBuilder()
            .addLixeira(TipoResiduo.ORGANICO)
            .construir();
    }
    
    @Test
    public void testLixeirasComTiposCorretos() {
        ZonaDeColeta zona = new ZonaDeColetaBuilder()
            .setNome("Zona Tipos")
            .addLixeira(TipoResiduo.ORGANICO)
            .addLixeira(TipoResiduo.RECICLAVEL)
            .construir();
        
        assertEquals(TipoResiduo.ORGANICO, zona.getLixeiras().get(0).getTipo());
        assertEquals(TipoResiduo.RECICLAVEL, zona.getLixeiras().get(1).getTipo());
    }
}