package br.com.adotapet.api.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Solicitante solicitante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    private String justificativa;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime respondidoEm;

    public Solicitacao() {
    }

    public Solicitacao(Solicitante solicitante, Animal animal) {
        this.solicitante = Objects.requireNonNull(solicitante, "O campo solicitante é obrigatório.");
        this.animal = Objects.requireNonNull(animal, "O campo animal é obrigatório");

        if (animal.getStatus() != StatusAnimal.DISPONIVEL) {
            throw new IllegalArgumentException("Não é possível solicitar um animal que não está disponível para adoção.");
        }

        animal.iniciarProcessoAdocao();

        this.status = StatusSolicitacao.PENDENTE;
    }

    public void aprovar() {
        validarPendente();

        animal.adotar();

        status = StatusSolicitacao.APROVADO;
        respondidoEm = LocalDateTime.now();
    }

    public void reprovar(String justificativa) {
        validarPendente();

        this.justificativa = Objects.requireNonNull(justificativa, "A justificativa é obrigatória para reprovar uma solicitação.");

        animal.disponibilizarNovamente();

        status = StatusSolicitacao.REPROVADO;
        respondidoEm = LocalDateTime.now();
    }

    private void validarPendente() {
        if (this.status != StatusSolicitacao.PENDENTE) {
            throw new IllegalArgumentException("A solicitação não se encontra com status pendente. (" + this.status + ")");
        }
    }

    public Long getId() {
        return id;
    }

    public Solicitante getSolicitante() {
        return solicitante;
    }

    public Animal getAnimal() {
        return animal;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getRespondidoEm() {
        return respondidoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitacao solicitacao = (Solicitacao) o;
        return id != null && id.equals(solicitacao.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
