package br.com.adotapet.api.domain.entity;

import br.com.adotapet.api.domain.exception.EmailInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record Email(
        @Column(nullable = false)
        String email
) {
    private static final Pattern FORMATO_VALIDO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");

    public Email {
        Objects.requireNonNull(email, "E-mail não pode ser nulo.");
        if (!FORMATO_VALIDO.matcher(email).matches()) {
            throw new EmailInvalidoException("E-mail com formato inválido.");
        }
    }
}
