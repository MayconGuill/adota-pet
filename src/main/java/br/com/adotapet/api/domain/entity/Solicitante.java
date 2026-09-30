package br.com.adotapet.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.util.Objects;

@Embeddable
public class Solicitante {
    @Column(nullable = false)
    private String nome;

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String telefone;

    @Embedded
    private Cpf cpf;

    @Column(nullable = false)
    private String historia;

    protected Solicitante() {
    }

    public Solicitante(String nome, Email email, String telefone, Cpf cpf, String historia) {
        this.nome = Objects.requireNonNull(nome, "O campo nome é obrigatório.");
        this.email = Objects.requireNonNull(email, "O campo e-mail é obrigatório.");
        this.telefone = Objects.requireNonNull(telefone, "O campo telefone é obrigatório.");
        this.cpf = Objects.requireNonNull(cpf, "O campo CPF é obrigatório.");
        this.historia = Objects.requireNonNull(historia, "O campo história é obrigatório.");
    }

    public String getNome() {
        return nome;
    }

    public Email getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public String getHistoria() {
        return historia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Solicitante that = (Solicitante) o;
        return Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(cpf, that.cpf) && Objects.equals(historia, that.historia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, telefone, cpf, historia);
    }
}
