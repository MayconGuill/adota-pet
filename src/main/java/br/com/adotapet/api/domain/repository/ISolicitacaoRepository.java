package br.com.adotapet.api.domain.repository;

import br.com.adotapet.api.domain.entity.Solicitacao;

import java.util.List;
import java.util.Optional;

public interface ISolicitacaoRepository {
    Solicitacao salvar(Solicitacao solicitacao);
    Optional<Solicitacao> buscarPorId(Long id);
    boolean existePorId(Long id);
    List<Solicitacao> buscarTodos();
    List<Solicitacao> buscarPorAnimalId(Long idAnimal);
    boolean existeStatusPendenteParaAnimalId(Long idAnimal);
}
