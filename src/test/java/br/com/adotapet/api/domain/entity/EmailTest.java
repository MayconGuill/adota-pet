package br.com.adotapet.api.domain.entity;

import br.com.adotapet.api.domain.exception.EmailInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @ParameterizedTest
    @MethodSource("emailValido")
    @DisplayName("Cadastra email com formato válido")
    void cadastrar(String email) {
        assertDoesNotThrow(() -> new Email(email));
    }

    @ParameterizedTest
    @MethodSource("emailInvalidos")
    @DisplayName("Lança erro com email inválido")
    void recusar(String email) {
        assertThrows(EmailInvalidoException.class, () -> new Email(email));
    }

    @Test
    @DisplayName("Lança erro quando email é nulo")
    void recusarNulo() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    private static Stream<String> emailInvalidos() {
        return Stream.of(
                "",
                "  ",
                "teste@gmail",
                "@gmail.com",
                "teste @gmail.com",
                "teste@"
        );
    }

    private static Stream<String> emailValido() {
        return Stream.of(
                "teste@gmail.com",
                "teste@gmail.com.br"
        );
    }
}