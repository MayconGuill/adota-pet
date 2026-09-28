package br.com.adotapet.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record Cpf(
        @Column(nullable = false)
        String cpf
) {
    private static final Pattern FORMATO_VALIDO =
            Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");

    public Cpf {
        Objects.requireNonNull(cpf, "CPF não pode ser nulo.");

        if (!FORMATO_VALIDO.matcher(cpf).matches()) {
            throw new IllegalArgumentException("CPF com formato inválido.");
        }

        if (!isCpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    private static boolean isCpfValido(String cpf) {
        String numeros = cpf.replaceAll("\\D", "");

        if (numeros.chars().distinct().count() == 1) {
            return false;
        }

        int primeiroDigito = calcularDigito(numeros.substring(0, 9));
        int segundoDigito = calcularDigito(numeros.substring(0, 9) + primeiroDigito);

        return numeros.charAt(9) - '0' == primeiroDigito
                && numeros.charAt(10) - '0' == segundoDigito;
    }

    private static int calcularDigito(String cpfParcial) {
        int soma = 0;
        int peso = cpfParcial.length() + 1;

        for (char digito : cpfParcial.toCharArray()) {
            soma += (digito - '0') * peso--;
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
