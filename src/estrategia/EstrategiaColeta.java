package estrategia;

import modelo.Lixeira;

public interface EstrategiaColeta {
    void coletar(Lixeira lixeira);
    String getNomeEstrategia();
}