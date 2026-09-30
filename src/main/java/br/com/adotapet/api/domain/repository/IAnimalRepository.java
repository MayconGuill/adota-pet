package br.com.adotapet.api.domain.repository;

import br.com.adotapet.api.domain.entity.Animal;

import java.util.List;
import java.util.Optional;

public interface IAnimalRepository {
    Animal salvar(Animal animal);
    Optional<Animal> buscarPorId(Long id);
    boolean existePorId(Long id);
    List<Animal> buscarTodos();
}
