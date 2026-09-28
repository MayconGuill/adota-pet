package br.com.adotapet.api.domain.entity;

import br.com.adotapet.api.domain.exception.BusinessException;
import br.com.adotapet.api.domain.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    @ParameterizedTest(name = "Campo obrigatório: {0}")
    @MethodSource("camposObrigatorios")
    @DisplayName("Lança erro ao tentar cadastrar animal com campo obrigatório null")
    void lancaExceptionAoCadastrarAnimalComCampoObrigatorioNulo(String campo, Consumer<Animal.Builder> campos) {
        assertThrows(NullPointerException.class, () -> campos.accept(new Animal.Builder()));
    }

    @Test
    @DisplayName("Animal cadastrado inicia com status disponível")
    void cadastraAnimalJaComStatusDisponivel() {
        Animal animal = novoAnimal();

        assertEquals(StatusAnimal.DISPONIVEL, animal.getStatus());
    }

    @Test
    @DisplayName("Inicia processo de adoção de pet disponível com sucesso")
    void deveIniciarProcessoDeAdocaoComStatusAnimalDisponivel() {
        Animal animal = novoAnimal();

        animal.iniciarProcessoAdocao();
        assertEquals(StatusAnimal.EM_PROCESSO_ADOCAO, animal.getStatus());
    }

    @Test
    @DisplayName("Lança erro ao iniciar processo de adoção do pet com status adotado")
    void lancaExceptionAoIniciarProcessoDeAdocaoComStatusAdotado() {
        Animal animal = animalAdotado();

        assertThrows(BusinessException.class, animal::iniciarProcessoAdocao);
    }

    @Test
    @DisplayName("Lança erro ao iniciar processo de adoção do pet que já possuí o status")
    void lancaExceptionAoIniciarProcessoDeAdocaoComStatusEmProcessoAdocao() {
        Animal animal = animalEmProcessoAdocao();

        assertThrows(BusinessException.class, animal::iniciarProcessoAdocao);
    }

    @Test
    @DisplayName("Adota um pet em processo de adoção")
    void deveAdotarUmPetComStatusEmProcessoAdocao() {
        Animal animal = animalEmProcessoAdocao();

        animal.adotar();
        assertEquals(StatusAnimal.ADOTADO, animal.getStatus());
    }

    @Test
    @DisplayName("Lança erro ao adotar pet com status disponivel")
    void lancaExceptionAoAdotarUmPetComStatusDisponivel() {
        Animal animal = novoAnimal();

        assertThrows(BusinessException.class, animal::adotar);
    }

    @Test
    @DisplayName("Lança erro ao adotar pet já adotado")
    void lancaExceptionAoAdotarUmPetComStatusAdotado() {
        Animal animal = animalAdotado();

        assertThrows(BusinessException.class, animal::adotar);
    }

    @Test
    @DisplayName("Disponibiliza novamente o pet em processo de adoção")
    void deveDisponibilizarNovamenteOPetComStatusEmProcessoAdocao() {
        Animal animal = animalEmProcessoAdocao();

        animal.disponibilizarNovamente();
        assertEquals(StatusAnimal.DISPONIVEL, animal.getStatus());
    }

    @Test
    @DisplayName("Lança erro ao tentar disponibilizar novamente um pet com status adotado")
    void lancaExceptionAoDisponibilizarNovamenteUmPetComStatusAdotado() {
        Animal animal = animalAdotado();

        assertThrows(BusinessException.class, animal::disponibilizarNovamente);
    }

    @Test
    @DisplayName("Lança erro ao tentar disponibilizar novamente um pet com status disponivel")
    void lancaExceptionAoDisponibilizarNovamenteUmPetComStatusDisponivel() {
        Animal animal = novoAnimal();

        assertThrows(BusinessException.class, animal::disponibilizarNovamente);
    }

    @Test
    @DisplayName("Adiciona TAG ao animal")
    void adicionaTagAoAnimal() {
        Animal animal = animalAdotado();
        Tag tag = new Tag("Brincalhão");

        animal.adicionarTag(tag);
        assertTrue(animal.getTags().contains(tag));
    }

    @Test
    @DisplayName("Lança erro ao adicionar TAG nula")
    void lancaExceptionComTagNulo() {
        Animal animal = animalAdotado();

        assertThrows(NullPointerException.class, () -> animal.adicionarTag(null));
    }

    @Test
    @DisplayName("Não adiciona TAG duplicada")
    void naoDeveAdicionarTagDuplicada() {
        Animal animal = animalAdotado();
        Tag tag = new Tag("Brincalhão");
        Tag tag2 = new Tag("Brincalhão");

        animal.adicionarTag(tag);
        animal.adicionarTag(tag2);

        assertEquals(1, animal.getTags().size());
    }

    @Test
    @DisplayName("Remove TAG do animal")
    void removeTagDoAnimal() {
        Animal animal = novoAnimal();
        Tag tag = new Tag("Brincalhão");

        animal.adicionarTag(tag);
        animal.removerTag(tag);

        assertEquals(0, animal.getTags().size());
    }

    @Test
    @DisplayName("Lança erro ao remover TAG nulo")
    void lancaExceptionAoRemoverTagNulo() {
        Animal animal = novoAnimal();
        assertThrows(NullPointerException.class, () -> animal.removerTag(null));
    }

    @Test
    @DisplayName("Adiciona foto ao animal")
    void adicionaFotoAoAnimal() {
        Animal animal = novoAnimal();
        Foto foto = new Foto("teste", "teste.com.br", "teste");

        animal.adicionarFoto(foto);
        assertTrue(animal.getFotos().contains(foto));
    }

    @Test
    @DisplayName("Lança um erro ao tentar adicionar foto nulo")
    void lancaExceptionAoTentarAdicionarFotoNulo() {
        Animal animal = novoAnimal();

        assertThrows(NullPointerException.class, () -> animal.adicionarFoto(null));
    }

    @Test
    @DisplayName("Remove foto do animal")
    void removeFotoDoAnimal() {
        Animal animal = novoAnimal();
        Foto foto = new Foto("teste", "teste.com.br", "teste");

        animal.adicionarFoto(foto);
        animal.removerFoto(foto);
        assertEquals(0, animal.getFotos().size());
    }

    private static Stream<Arguments> camposObrigatorios() {
        return Stream.of(
                Arguments.of("nome não pode ser null", (Consumer<Animal.Builder>) builder -> builder.nome(null)),
                Arguments.of("dataNascimento não pode ser null", (Consumer<Animal.Builder>) builder -> builder.dataNascimento(null)),
                Arguments.of("sexo não pode ser null", (Consumer<Animal.Builder>) builder -> builder.sexo(null)),
                Arguments.of("especie não pode ser null", (Consumer<Animal.Builder>) builder -> builder.especie(null)),
                Arguments.of("porte não pode ser null", (Consumer<Animal.Builder>) builder -> builder.porte(null)),
                Arguments.of("historia não pode ser null", (Consumer<Animal.Builder>) builder -> builder.historia(null))
        );
    }

    private Animal novoAnimal() {
        return new Animal.Builder()
                .nome("Bob")
                .dataNascimento(DataUtil.newDate("01/01/2026"))
                .sexo(TipoSexo.MACHO)
                .especie(TipoEspecie.CACHORRO)
                .porte(TipoPorte.MEDIO)
                .historia("O nome do cachorro é Bob")
                .build();
    }

    private Animal animalAdotado() {
        Animal animal = novoAnimal();

        animal.iniciarProcessoAdocao();
        animal.adotar();

        return animal;
    }

    private Animal animalEmProcessoAdocao() {
        Animal animal = novoAnimal();

        animal.iniciarProcessoAdocao();

        return animal;
    }
}