package estrategia;

import modelo.Lixeira;

public class ColetaUrgente implements EstrategiaColeta {
    @Override
    public void coletar(Lixeira lixeira) {
        System.out.println("COLETA URGENTE iniciada!");
        System.out.println("Coletando lixeira " + lixeira.getId() + " (Tipo: " + lixeira.getTipo().getNome() + ")");
        System.out.println("Nível antes da coleta: " + String.format("%.1f%%", lixeira.getNivel()));
        
        lixeira.esvaziar();
        
        System.out.println("Lixeira esvaziada com prioridade máxima!");
        System.out.println("----------------------------------------");
    }

    @Override
    public String getNomeEstrategia() {
        return "Coleta Urgente";
    }
}