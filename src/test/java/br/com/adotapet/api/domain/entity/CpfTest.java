package br.com.adotapet.api.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CpfTest {

    @Test
    @DisplayName("CPF válido")
    void cadastrar() {
        assertDoesNotThrow(() -> new Cpf("571.338.944-83"));
    }

    @Test
    @DisplayName("Lança erro quando CPF é nulo")
    void recusarNulo() {
        assertThrows(NullPointerException.class, () -> new Cpf(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "57133894483",
            "123.456.789-10"
    }
    )
    @DisplayName("")
    void recusar(String cpf) {
        assertThrows(IllegalArgumentException.class, () -> new Cpf(cpf));
    }
}