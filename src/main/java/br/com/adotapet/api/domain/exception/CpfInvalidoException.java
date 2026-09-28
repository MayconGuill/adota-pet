package br.com.adotapet.api.domain.exception;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException(String message) {
        super(message);
    }
}
