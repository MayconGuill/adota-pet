package br.com.adotapet.api.infra.repository;

import br.com.adotapet.api.domain.entity.Solicitacao;
import br.com.adotapet.api.domain.entity.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    boolean existsByAnimalIdAndStatus(Long idAnimal, StatusSolicitacao statusSolicitacao);

    List<Solicitacao> findAllByAnimalId(Long idAnimal);
}
