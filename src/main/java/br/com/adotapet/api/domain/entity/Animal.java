package br.com.adotapet.api.domain.entity;

import br.com.adotapet.api.domain.exception.BusinessException;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "animais")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEspecie especie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPorte porte;

    @Column(nullable = false)
    private String historia;

    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAnimal status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "animal_tag",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    protected Animal() {
    }

    private Animal(Builder builder) {
        this.nome = builder.nome;
        this.dataNascimento = builder.dataNascimento;
        this.sexo = builder.sexo;
        this.especie = builder.especie;
        this.porte = builder.porte;
        this.historia = builder.historia;
        this.observacao = builder.observacao;
        this.status = StatusAnimal.DISPONIVEL;
    }

    public static class Builder {
        private String nome;
        private LocalDate dataNascimento;
        private TipoSexo sexo;
        private TipoEspecie especie;
        private TipoPorte porte;
        private String historia;
        private String observacao;

        public Builder() {
        }

        public Builder nome(String nome) {
            this.nome = Objects.requireNonNull(nome, "O nome do pet é obrigatório");
            return this;
        }

        public Builder dataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = Objects.requireNonNull(dataNascimento, "O campo data nascimento não pode ser nulo.");
            return this;
        }

        public Builder sexo(TipoSexo sexo) {
            this.sexo = Objects.requireNonNull(sexo, "É obrigatório preencher com o sexo do pet.");
            return this;
        }

        public Builder especie(TipoEspecie especie) {
            this.especie = Objects.requireNonNull(especie, "O campo espécie é obrigatório.");
            return this;
        }

        public Builder porte(TipoPorte porte) {
            this.porte = Objects.requireNonNull(porte, "É obrigatório preencher o porte do pet.");
            return this;
        }

        public Builder historia(String historia) {
            this.historia = Objects.requireNonNull(historia, "O campo história é obrigatório.");
            return this;
        }

        public Builder observacao(String observacao) {
            this.observacao = observacao;
            return this;
        }

        public Animal build() {
            return new Animal(this);
        }
    }

    void iniciarProcessoAdocao() {
        if (this.status != StatusAnimal.DISPONIVEL) {
            throw new BusinessException("Não foi possível iniciar o processo de adoção, pois o status " + this.status + " não se encontra DISPONIVEL.");
        }
        this.status = StatusAnimal.EM_PROCESSO_ADOCAO;
    }

    void adotar() {
        if (this.status != StatusAnimal.EM_PROCESSO_ADOCAO) {
            throw new BusinessException("Não foi possível adotar o animal de ID " + id + ", o status já se encontra como " + this.status);
        }
        this.status = StatusAnimal.ADOTADO;
    }

    void disponibilizarNovamente() {
        if (this.status != StatusAnimal.EM_PROCESSO_ADOCAO) {
            throw new BusinessException("Não foi possível disponibilizar o animal de ID " + id + ", o status já se encontra como " + this.status);
        }
        this.status = StatusAnimal.DISPONIVEL;
    }

    public void adicionarFoto(Foto foto) {
        Objects.requireNonNull(foto, "Foto não pode ser nulo.");
        fotos.add(foto);
        foto.setAnimal(this);
    }

    public void removerFoto(Foto foto) {
        Objects.requireNonNull(foto, "Foto não pode ser nulo.");
        fotos.remove(foto);
        foto.setAnimal(null);
    }

    public void adicionarTag(Tag tag) {
        Objects.requireNonNull(tag, "TAG não pode ser nulo.");
        tags.add(tag);
    }

    public void removerTag(Tag tag) {
        Objects.requireNonNull(tag, "TAG não pode ser nulo.");
        tags.remove(tag);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public TipoSexo getSexo() {
        return sexo;
    }

    public TipoEspecie getEspecie() {
        return especie;
    }

    public TipoPorte getPorte() {
        return porte;
    }

    public String getHistoria() {
        return historia;
    }

    public String getObservacao() {
        return observacao;
    }

    public StatusAnimal getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public List<Foto> getFotos() {
        return Collections.unmodifiableList(fotos);
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id != null && id.equals(animal.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", sexo=" + sexo +
                ", especie=" + especie +
                ", porte=" + porte +
                ", historia='" + historia + '\'' +
                ", observacao='" + observacao + '\'' +
                ", status=" + status +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }
}
