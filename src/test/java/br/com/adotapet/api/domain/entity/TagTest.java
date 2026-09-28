package br.com.adotapet.api.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {
    @Test
    @DisplayName("Cria uma TAG em maiúsculo e sem espaços com sucesso")
    void cadastrarTagNormalizandoNome() {
        Tag tag = new Tag(" Brincalhão ");

        assertEquals("BRINCALHÃO", tag.getNome());
    }

    @Test
    @DisplayName("Lança um erro ao tentar cadastrar uma TAG com nome nulo")
    void lancaExceptionAoCadastrarTagComNomeNulo() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    @DisplayName("Lança um erro ao tentar cadastrar uma TAG em branco")
    void lancaExceptionAoCadastrarTagComNomeEmBranco() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(" "));
    }

    @Test
    @DisplayName("Lança um erro ao tentar cadastrar uma TAG em vazio")
    void lancaExceptionAoCadastrarTagComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(""));
    }

    @Test
    @DisplayName("TAG com o mesmo nome é igual")
    void tagComMesmoNomeDevemSerIguais() {
        Tag tag = new Tag("Brincalhão");
        Tag tag2 = new Tag(" brincalhão ");

        assertEquals(tag.hashCode(), tag2.hashCode());
    }

    @Test
    @DisplayName("TAG com nome diferente é diferente")
    void tagComNomeDiferenteDevemSerDiferentes() {
        Tag tag = new Tag("Brincalhão");
        Tag tag2 = new Tag("Raivoso");

        assertNotEquals(tag, tag2);
    }

}