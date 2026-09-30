package br.com.adotapet.api.util;

import br.com.adotapet.api.domain.entity.*;

import static br.com.adotapet.api.util.AnimalUtil.criarAnimal;

public class SolicitacaoUtil {
    public static Solicitacao criarSolicitacao() {
        Animal animal = criarAnimal();

        return new Solicitacao(criarSolicitante(), animal);
    }

    public static Solicitacao solicitacaoAprovada() {
        var solicitacao = criarSolicitacao();

        solicitacao.aprovar();

        return solicitacao;
    }

    public static Solicitacao solicitacaoReprovada() {
        var solicitacao = criarSolicitacao();

        solicitacao.reprovar("Qualquer justificativa");

        return solicitacao;
    }

    public static Solicitante criarSolicitante() {
        return new Solicitante(
                "teste",
                new Email("teste@gmail.com"),
                "19988887777",
                new Cpf("571.338.944-83"),
                "Qualquer história");
    }
}
