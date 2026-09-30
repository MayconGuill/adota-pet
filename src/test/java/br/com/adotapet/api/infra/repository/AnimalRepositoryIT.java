package br.com.adotapet.api.infra.repository;

import br.com.adotapet.api.domain.entity.Animal;
import br.com.adotapet.api.domain.entity.Foto;
import br.com.adotapet.api.domain.entity.Tag;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static br.com.adotapet.api.util.AnimalUtil.criarAnimal;
import static br.com.adotapet.api.util.AnimalUtil.criarAnimalComFoto;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AnimalRepositoryIT {

    @Autowired
    private AnimalRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Deve salvar um Animal com sucesso no banco de dados")
    void deveSalvarAnimalComSucesso() {
        var expectativaAnimal = criarAnimal();

        var animalSalvo = repository.save(expectativaAnimal);

        var animalEncontrado = repository.findById(animalSalvo.getId());

        assertTrue(animalEncontrado.isPresent());

        var animal = animalEncontrado.get();

        assertNotNull(animal.getId());
        assertEquals(expectativaAnimal.getNome(), animal.getNome());
        assertEquals(expectativaAnimal.getDataNascimento(), animal.getDataNascimento());
        assertEquals(expectativaAnimal.getStatus(), animal.getStatus());
        assertEquals(expectativaAnimal.getEspecie(), animal.getEspecie());
        assertEquals(expectativaAnimal.getSexo(), animal.getSexo());
        assertEquals(expectativaAnimal.getPorte(), animal.getPorte());
        assertEquals(expectativaAnimal.getHistoria(), animal.getHistoria());
    }

    @Test
    @DisplayName("Deve buscar um Animal com sucesso no banco de dados")
    void deveBuscarUmAnimalComSucesso() {
        var expectativaAnimal = criarAnimal();

        var animalSalvo = repository.save(expectativaAnimal);

        entityManager.flush();
        entityManager.clear();

        Optional<Animal> animalEncontrado = repository.findById(animalSalvo.getId());

        assertTrue(animalEncontrado.isPresent());
        assertEquals(animalSalvo.getId(), animalEncontrado.get().getId());
    }

    @Test
    @DisplayName("Deve verificar a existência de um Animal pelo ID")
    void deveVerificarExistenciaDoAnimal() {
        var animal = criarAnimal();

        var animalSalvo = repository.save(animal);

        entityManager.flush();
        entityManager.clear();

        assertTrue(repository.existsById(animalSalvo.getId()));
        assertFalse(repository.existsById(999L));
    }

    @Test
    @DisplayName("Deve persistir um Animal com Foto através do cascade")
    void deveSalvarAnimalComFotoComSucesso() {
        var expectativaAnimal = criarAnimalComFoto();

        var animalSalvo = repository.save(expectativaAnimal);

        entityManager.flush();
        entityManager.clear();

        var animalEncontrado = repository.findById(animalSalvo.getId());

        assertTrue(animalEncontrado.isPresent());
        assertEquals(1, animalEncontrado.get().getFotos().size());

        var foto = animalEncontrado.get().getFotos().getFirst();

        assertNotNull(foto.getId());
        assertNull(foto.getBase64());
        assertEquals(animalEncontrado.get().getId(), foto.getAnimal().getId());
    }

    @Test
    @DisplayName("Deve trazer uma lista de Animais")
    void deveListarTodosAnimais() {
        var animal = criarAnimal();
        var animal2 = criarAnimal();

        repository.save(animal);
        repository.save(animal2);

        entityManager.flush();
        entityManager.clear();

        List<Animal> listaDeAnimais = repository.findAll();

        assertEquals(2, listaDeAnimais.size());
    }

    @Test
    @DisplayName("Deve persistir um Animal com TAG")
    void deveSalvarAnimalComTagComSucesso() {
        var animal = criarAnimal();
        var tag = new Tag("brincalhão");

        entityManager.persist(tag);

        animal.adicionarTag(tag);

        var animalSalvo = repository.save(animal);

        entityManager.flush();
        entityManager.clear();

        var animalEncontrado = repository.findById(animalSalvo.getId());

        assertTrue(animalEncontrado.isPresent());
        assertEquals(1, animalEncontrado.get().getTags().size());

        var tagEncontrada = animalEncontrado.get()
                .getTags()
                .iterator()
                .next();

        assertNotNull(tagEncontrada.getId());
        assertEquals(tag.getNome(), tagEncontrada.getNome());
    }

    @Test
    @DisplayName("Deve permitir associar a mesma TAG a múltiplos Animais")
    void deveAssociarMesmaTagAMultiplosAnimais() {
        var tag = new Tag("brincalhão");

        entityManager.persist(tag);

        var animal1 = criarAnimal();
        var animal2 = criarAnimal();

        animal1.adicionarTag(tag);
        animal2.adicionarTag(tag);

        repository.save(animal1);
        repository.save(animal2);

        entityManager.flush();
        entityManager.clear();

        var animal1Encontrado = repository.findById(animal1.getId());
        var animal2Encontrado = repository.findById(animal2.getId());

        assertTrue(animal1Encontrado.isPresent());
        assertTrue(animal2Encontrado.isPresent());

        assertEquals(1, animal1Encontrado.get().getTags().size());
        assertEquals(1, animal2Encontrado.get().getTags().size());

        var tagDoAnimal1 = animal1Encontrado.get()
                .getTags()
                .iterator()
                .next();

        var tagDoAnimal2 = animal2Encontrado.get()
                .getTags()
                .iterator()
                .next();

        assertEquals(tag.getId(), tagDoAnimal1.getId());
        assertEquals(tag.getId(), tagDoAnimal2.getId());
    }

    @Test
    @DisplayName("Deve remover Foto através do orphanRemoval")
    void deveRemoverFotoAtravésDoOrphanRemoval() {
        var animal = criarAnimalComFoto();

        repository.save(animal);

        entityManager.flush();
        entityManager.clear();

        var animalEncontrado = repository.findById(animal.getId());

        assertTrue(animalEncontrado.isPresent());
        assertEquals(1, animalEncontrado.get().getFotos().size());

        var foto = animalEncontrado.get().getFotos().getFirst();
        var fotoId = foto.getId();

        animalEncontrado.get().removerFoto(foto);

        entityManager.flush();
        entityManager.clear();

        var animalAtualizado = repository.findById(animal.getId());

        assertTrue(animalAtualizado.isPresent());
        assertTrue(animalAtualizado.get().getFotos().isEmpty());

        var fotoNoBanco = entityManager.find(Foto.class, fotoId);

        assertNull(fotoNoBanco);
    }
}