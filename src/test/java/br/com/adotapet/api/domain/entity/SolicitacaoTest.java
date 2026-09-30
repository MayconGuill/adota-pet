package br.com.adotapet.api.domain.entity;

import br.com.adotapet.api.domain.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.adotapet.api.util.AnimalUtil.*;
import static br.com.adotapet.api.util.SolicitacaoUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolicitacaoTest {
    @Test
    @DisplayName("Solicitação cadastrada inicia como Pendente")
    void cadastraSolicitacaoJaComStatusPendente() {
        var solicitacao = criarSolicitacao();

        assertEquals(StatusSolicitacao.PENDENTE, solicitacao.getStatus());
    }

    @Test
    @DisplayName("Solicitação cadastrada altera o status do animal para em processo de adoção")
    void solicitacaoAlteraOStatusDoAnimalParaEmProcessoAdocao() {
        var solicitacao = criarSolicitacao();

        assertEquals(StatusAnimal.EM_PROCESSO_ADOCAO, solicitacao.getAnimal().getStatus());
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação sem preencher solicitante")
    void lancaExceptionAoCadastrarSolicitacaoComSolicitanteNulo() {
        assertThrows(NullPointerException.class, () -> new Solicitacao(null, criarAnimal()));
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação sem preencher animal")
    void lancaExceptionAoCadastrarSolicitacaoComAnimalNulo() {
        assertThrows(NullPointerException.class, () -> new Solicitacao(criarSolicitante(), null));
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação com animal adotado")
    void lancaExceptionAoCadastrarSolicitacaoComAnimalAdotado() {
        assertThrows(BusinessException.class, () -> new Solicitacao(criarSolicitante(), animalAdotado()));
    }

    @Test
    @DisplayName("Lança um erro ao cadastrar solicitação com animal já em processo de adoção")
    void lancaExceptionAoCadastrarSolicitacaoComAnimalEmProcessoDeAdocao() {
        assertThrows(BusinessException.class, () -> new Solicitacao(criarSolicitante(), animalEmProcessoAdocao()));
    }

    @Test
    @DisplayName("Deve aprovar solicitação com sucesso")
    void deveAprovarSolicitacaoComSucesso() {
        var solicitacao = criarSolicitacao();

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
        var solicitacao = criarSolicitacao();

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
        var solicitacao = criarSolicitacao();

        assertThrows(NullPointerException.class, () -> solicitacao.reprovar(null));
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com justificativa vazio")
    void lancaExceptionAoReprovarUmaSolicitacaoComJustificativaVazio() {
        var solicitacao = criarSolicitacao();

        assertThrows(BusinessException.class, () -> solicitacao.reprovar(""));
    }

    @Test
    @DisplayName("Lança um erro ao tentar reprovar uma solicitação com justificativa em branco")
    void lancaExceptionAoReprovarUmaSolicitacaoComJustificativaEmBranco() {
        var solicitacao = criarSolicitacao();

        assertThrows(BusinessException.class, () -> solicitacao.reprovar("  "));
    }
}