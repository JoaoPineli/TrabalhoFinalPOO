package modelo;

public enum TipoResiduo {
    ORGANICO("Orgânico", "Restos de comida, folhas, etc."),
    RECICLAVEL("Reciclável", "Plástico, papel, metal, vidro"),
    PERIGOSO("Perigoso", "Pilhas, baterias, produtos químicos"),
    ELETRONICO("Eletrônico", "Equipamentos eletrônicos"),
    GERAL("Geral", "Resíduos não recicláveis");

    private final String nome;
    private final String descricao;

    TipoResiduo(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}