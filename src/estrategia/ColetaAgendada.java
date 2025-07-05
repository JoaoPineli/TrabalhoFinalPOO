package estrategia;

import modelo.Lixeira;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ColetaAgendada implements EstrategiaColeta {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void coletar(Lixeira lixeira) {
        System.out.println("COLETA AGENDADA");
        System.out.println("Horário: " + LocalDateTime.now().format(formatter));
        System.out.println("Coletando lixeira " + lixeira.getId() + " (Tipo: " + lixeira.getTipo().getNome() + ")");
        System.out.println("Nível antes da coleta: " + String.format("%.1f%%", lixeira.getNivel()));
        
        lixeira.esvaziar();
        
        System.out.println("Lixeira esvaziada conforme agenda!");
        System.out.println("----------------------------------------");
    }

    @Override
    public String getNomeEstrategia() {
        return "Coleta Agendada";
    }
}