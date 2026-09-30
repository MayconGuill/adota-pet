package br.com.adotapet.api.infra.repository;

import br.com.adotapet.api.domain.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
