package br.com.adotapet.api.domain.entity;

import br.com.adotapet.api.domain.exception.BusinessException;
import br.com.adotapet.api.domain.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolicitacaoTest {
    @Test
    @DisplayName("Solicitação cadastrada inicia como Pendente")
    void cadastraSolicitacaoJaComStatusPendente() {
        var solicitacao = novaSolicitacao();

        assertEquals(StatusSolicitacao.PENDENTE, solicitacao.getStatus());
    }

    @Test
    @DisplayName("Solicitação cadastrada altera o status do animal para em processo de adoção")
    void solicitacaoAlteraOStatusDoAnimalParaEmProcessoAdocao() {
        var solicitacao = novaSolicitacao();

        assertEquals(StatusAnimal.EM_PROCESSO_ADOCAO, solicitacao.getAnimal().getStatus());
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação sem preencher solicitante")
    void lancaExceptionAoCadastrarSolicitacaoComSolicitanteNulo() {
        assertThrows(NullPointerException.class, () -> new Solicitacao(null, novoAnimal()));
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação sem preencher animal")
    void lancaExceptionAoCadastrarSolicitacaoComAnimalNulo() {
        assertThrows(NullPointerException.class, () -> new Solicitacao(novoSolicitante(), null));
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação com animal adotado")
    void lancaExceptionAoCadastrarSolicitacaoComAnimalAdotado() {
        assertThrows(BusinessException.class, () -> new Solicitacao(novoSolicitante(), animalAdotado()));
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação com animal já em processo de adoção")
    void lancaExceptionAoCadastrarSolicitacaoComAnimalEmProcessoDeAdocao() {
        assertThrows(BusinessException.class, () -> new Solicitacao(novoSolicitante(), animalEmProcessoAdocao()));
    }

    @Test
    @DisplayName("Deve aprovar solicitação com sucesso")
    void deveAprovarSolicitacaoComSucesso() {
        var solicitacao = novaSolicitacao();

        solicitacao.aprovar();
        assertEquals(StatusSolicitacao.APROVADO, solicitacao.getStatus());
        assertEquals(StatusAnimal.ADOTADO, solicitacao.getAnimal().getStatus());
    }

    @Test
    @DisplayName("Lança um erro ao tentar aprovar uma solicitação com status já aprovado")
    void lancaExceptionAoAprovarUmaSolicitacaoComStatusAprovado() {
        var solicitacao = solicitacaoAprovada();

        assertThrows(BusinessException.class, solicitacao::aprovar);
    }

    @Test
    @DisplayName("Lança um erro ao tentar aprovar uma solicitação com status reprovado")
    void lancaExceptionAoAprovarUmaSolicitacaoComStatusReprovado() {
        var solicitacao = solicitacaoReprovada();

        assertThrows(BusinessException.class, solicitacao::aprovar);
    }

    @Test
    @DisplayName("Deve reprovar solicitação com sucesso")
    void deveReprovarSolicitacaoComSucesso() {
        var solicitacao = novaSolicitacao();

        solicitacao.reprovar("Qualquer justificativa");

        assertEquals(StatusSolicitacao.REPROVADO, solicitacao.getStatus());
        assertEquals(StatusAnimal.DISPONIVEL, solicitacao.getAnimal().getStatus());
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com status já aprovado")
    void lancaExceptionAoReprovarUmaSolicitacaoComStatusAprovado() {
        var solicitacao = solicitacaoAprovada();

        assertThrows(BusinessException.class, () -> solicitacao.reprovar("Qualquer justificativa"));
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com status reprovado")
    void lancaExceptionAoReprovarUmaSolicitacaoComStatusReprovado() {
        var solicitacao = solicitacaoReprovada();

        assertThrows(BusinessException.class, () -> solicitacao.reprovar("Qualquer justificativa"));
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com justificativa nulo")
    void lancaExceptionAoReprovarUmaSolicitacaoComJustificativaNulo() {
        var solicitacao = novaSolicitacao();

        assertThrows(NullPointerException.class, () -> solicitacao.reprovar(null));
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com justificativa vazio")
    void lancaExceptionAoReprovarUmaSolicitacaoComJustificativaVazio() {
        var solicitacao = novaSolicitacao();

        assertThrows(BusinessException.class, () -> solicitacao.reprovar(""));
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com justificativa em branco")
    void lancaExceptionAoReprovarUmaSolicitacaoComJustificativaEmBranco() {
        var solicitacao = novaSolicitacao();

        assertThrows(BusinessException.class, () -> solicitacao.reprovar("  "));
    }

    private Solicitacao novaSolicitacao() {
        Animal animal = novoAnimal();
        Solicitante solicitante = novoSolicitante();

        return new Solicitacao(solicitante, animal);
    }

    private Solicitacao solicitacaoAprovada() {
        var solicitacao = novaSolicitacao();

        solicitacao.aprovar();

        return solicitacao;
    }

    private Solicitacao solicitacaoReprovada() {
        var solicitacao = novaSolicitacao();

        solicitacao.reprovar("Qualquer justificativa");

        return solicitacao;
    }

    private Solicitante novoSolicitante() {
        return new Solicitante(
                "teste",
                new Email("teste@gmail.com"),
                "19988887777",
                new Cpf("571.338.944-83"),
                "Qualquer história");
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
        var animal = novoAnimal();

        animal.iniciarProcessoAdocao();
        animal.adotar();
        return animal;
    }

    private Animal animalEmProcessoAdocao() {
        var animal = novoAnimal();
        animal.iniciarProcessoAdocao();

        return animal;
    }
}