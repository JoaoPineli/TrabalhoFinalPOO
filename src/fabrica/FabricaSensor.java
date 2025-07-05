package fabrica;

import modelo.Lixeira;
import modelo.TipoResiduo;
import observer.SensorCapacidade;
import observer.SensorLixeira;

public class FabricaSensor {
    
    public SensorLixeira criarSensor(TipoResiduo tipo, Lixeira lixeira) {
        double limiteNotificacao = obterLimiteNotificacao(tipo);
        
        switch (tipo) {
            case ORGANICO:
                return criarSensorOrganico(lixeira, limiteNotificacao);
            case PERIGOSO:
                return criarSensorPerigoso(lixeira, limiteNotificacao);
            case ELETRONICO:
                return criarSensorEletronico(lixeira, limiteNotificacao);
            default:
                return criarSensorPadrao(lixeira, limiteNotificacao);
        }
    }

    private double obterLimiteNotificacao(TipoResiduo tipo) {
        switch (tipo) {
            case ORGANICO:
                return 70.0;
            case PERIGOSO:
                return 50.0;
            case ELETRONICO:
                return 60.0;
            case RECICLAVEL:
                return 85.0;
            default:
                return 80.0;
        }
    }

    private SensorLixeira criarSensorOrganico(Lixeira lixeira, double limite) {
        System.out.println("Criando sensor especializado para resíduos orgânicos");
        return new SensorCapacidade(lixeira, limite);
    }

    private SensorLixeira criarSensorPerigoso(Lixeira lixeira, double limite) {
        System.out.println("Criando sensor com monitoramento especial para resíduos perigosos");
        return new SensorCapacidade(lixeira, limite);
    }

    private SensorLixeira criarSensorEletronico(Lixeira lixeira, double limite) {
        System.out.println("Criando sensor para resíduos eletrônicos");
        return new SensorCapacidade(lixeira, limite);
    }

    private SensorLixeira criarSensorPadrao(Lixeira lixeira, double limite) {
        System.out.println("Criando sensor padrão");
        return new SensorCapacidade(lixeira, limite);
    }
}