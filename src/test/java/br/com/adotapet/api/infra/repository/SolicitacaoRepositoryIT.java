package br.com.adotapet.api.infra.repository;

import br.com.adotapet.api.domain.entity.Solicitacao;
import br.com.adotapet.api.domain.entity.StatusAnimal;
import br.com.adotapet.api.domain.entity.StatusSolicitacao;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static br.com.adotapet.api.util.AnimalUtil.criarAnimal;
import static br.com.adotapet.api.util.SolicitacaoUtil.criarSolicitacao;
import static br.com.adotapet.api.util.SolicitacaoUtil.criarSolicitante;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SolicitacaoRepositoryIT {
    @Autowired
    private SolicitacaoRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Deve salvar uma solicitação com sucesso")
    void deveSalvarUmaSolicitacaoComSucesso() {
        var solicitacaoParaSalvar = criarSolicitacao();
        var animal = solicitacaoParaSalvar.getAnimal();

        entityManager.persist(animal);
        entityManager.flush();

        assertNotNull(animal.getId());

        var solicitacaoSalva = repository.save(solicitacaoParaSalvar);

        entityManager.flush();
        entityManager.clear();

        Optional<Solicitacao> solicitacaoEncontrada = repository.findById(solicitacaoSalva.getId());

        assertTrue(solicitacaoEncontrada.isPresent());
        var solicitacao = solicitacaoEncontrada.get();

        assertNotNull(solicitacao.getId());
        assertEquals(solicitacaoSalva.getAnimal().getId(), solicitacao.getAnimal().getId());
        assertEquals(solicitacaoSalva.getStatus(), solicitacao.getStatus());
        assertEquals(solicitacaoSalva.getSolicitante(), solicitacao.getSolicitante());
        assertNotNull(solicitacao.getCriadoEm());
        assertNull(solicitacao.getRespondidoEm());
    }

    @Test
    @DisplayName("Deve persistir uma solicitação aprovada")
    void devePersistirSolicitacaoAprovada() {
        var solicitacao = criarSolicitacao();

        entityManager.persist(solicitacao.getAnimal());

        solicitacao.aprovar();

        repository.save(solicitacao);

        entityManager.flush();
        entityManager.clear();

        var encontrada = repository.findById(solicitacao.getId()).orElseThrow();

        assertEquals(StatusSolicitacao.APROVADO, encontrada.getStatus());
        assertNotNull(encontrada.getRespondidoEm());
        assertNull(encontrada.getJustificativa());
    }

    @Test
    @DisplayName("Deve persistir uma solicitação reprovada")
    void devePersistirSolicitacaoReprovada() {
        var solicitacao = criarSolicitacao();

        entityManager.persist(solicitacao.getAnimal());

        solicitacao.reprovar("Animal incompatível com o perfil.");

        repository.save(solicitacao);

        entityManager.flush();
        entityManager.clear();

        var encontrada = repository.findById(solicitacao.getId()).orElseThrow();

        assertEquals("Animal incompatível com o perfil.", encontrada.getJustificativa());
        assertNotNull(encontrada.getRespondidoEm());
    }

    @Test
    @DisplayName("Deve permitir várias solicitações para o mesmo animal")
    void devePermitirVariasSolicitacoesParaOMesmoAnimal() {
        var animal = criarAnimal();

        entityManager.persist(animal);

        var primeira = new Solicitacao(criarSolicitante(), animal);

        primeira.reprovar("Primeira solicitação reprovada.");

        var segunda = new Solicitacao(criarSolicitante(), animal);

        segunda.reprovar("Segunda solicitação reprovada.");

        var terceira = new Solicitacao(criarSolicitante(), animal);

        repository.saveAll(List.of(primeira, segunda, terceira));

        entityManager.flush();
        entityManager.clear();

        List<Solicitacao> solicitacoes = repository.findAllByAnimalId(animal.getId());

        assertEquals(3, solicitacoes.size());
        assertEquals(2, solicitacoes.stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.REPROVADO)
                .count());
        assertEquals(1, solicitacoes.stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.PENDENTE)
                .count());
    }

    @Test
    @DisplayName("Deve verificar se existe solicitação pendente para o animal")
    void deveVerificarSeExisteSolicitacaoPendente() {
        var animal = criarAnimal();

        entityManager.persist(animal);

        var solicitacao = new Solicitacao(criarSolicitante(), animal);

        repository.save(solicitacao);

        entityManager.flush();
        entityManager.clear();

        assertTrue(repository.existsByAnimalIdAndStatus(animal.getId(), StatusSolicitacao.PENDENTE));

        assertFalse(repository.existsByAnimalIdAndStatus(animal.getId(), StatusSolicitacao.REPROVADO));
    }

    @Test
    @DisplayName("Deve verificar a existência de uma solicitação")
    void deveVerificarExistenciaDeUmaSolicitacao() {
        var solicitacao = criarSolicitacao();

        entityManager.persist(solicitacao.getAnimal());

        repository.save(solicitacao);

        entityManager.flush();
        entityManager.clear();

        assertTrue(repository.existsById(solicitacao.getId()));
        assertFalse(repository.existsById(999L));
    }
}