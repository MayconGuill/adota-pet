package br.com.adotapet.api.infra.repository;

import br.com.adotapet.api.domain.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
}
