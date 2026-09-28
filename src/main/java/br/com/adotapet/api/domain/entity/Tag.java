package br.com.adotapet.api.domain.entity;

import jakarta.persistence.*;

import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    public Tag() {
    }

    public Tag(String nome) {
        this.nome = validarNome(nome);
    }
    
    private String validarNome(String nome) {
        var nomeValidado = Objects.requireNonNull(nome, "Nome da TAG não pode ser nulo.")
                .trim()
                .toUpperCase(Locale.ROOT);
        if (nomeValidado.isBlank()) {
            throw new IllegalArgumentException("Nome da TAG não pode ser vazio.");
        }

        return nomeValidado;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id != null && id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
