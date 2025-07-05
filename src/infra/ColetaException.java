package infra;

public class ColetaException extends Exception {
    public ColetaException(String mensagem) {
        super(mensagem);
    }

    public ColetaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}