package test;

import modelo.Lixeira;
import modelo.TipoResiduo;
import modelo.CaminhaoColetor;
import estrategia.ColetaAgendada;
import estrategia.ColetaUrgente;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StrategyTest {
    
    private CaminhaoColetor caminhao;
    private Lixeira lixeira;
    
    @Before
    public void setUp() {
        caminhao = new CaminhaoColetor("CAM-STRATEGY", new ColetaAgendada());
        lixeira = new Lixeira("L-STRATEGY", TipoResiduo.GERAL, 100.0);
    }
    
    @Test
    public void testEstrategiaInicial() {
        assertEquals("Coleta Agendada", caminhao.getEstrategiaColeta().getNomeEstrategia());
    }
    
    @Test
    public void testMudarEstrategia() {
        caminhao.setEstrategiaColeta(new ColetaUrgente());
        assertEquals("Coleta Urgente", caminhao.getEstrategiaColeta().getNomeEstrategia());
    }
    
    @Test
    public void testColetaComDiferentesEstrategias() {
        int coletasInicial = caminhao.getColetasRealizadas();
        
        caminhao.realizarColeta(lixeira);
        assertEquals(coletasInicial + 1, caminhao.getColetasRealizadas());
        
        caminhao.setEstrategiaColeta(new ColetaUrgente());
        caminhao.realizarColeta(lixeira);
        assertEquals(coletasInicial + 2, caminhao.getColetasRealizadas());
    }
}
