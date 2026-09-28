package br.com.adotapet.api.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "fotos")
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String link;

    @Transient
    private String base64;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    public Foto() {
    }

    public Foto(String fileName, String link, String base64) {
        this.fileName = Objects.requireNonNull(fileName, "FileName não pode ser nulo.");
        this.link = Objects.requireNonNull(link, "Link não pode ser nulo.");
        this.base64 = Objects.requireNonNull(base64, "Base64 não pode ser nulo.");
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLink() {
        return link;
    }

    public String getBase64() {
        return base64;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public Animal getAnimal() {
        return animal;
    }

    void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foto foto = (Foto) o;
        return id != null && id.equals(foto.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
