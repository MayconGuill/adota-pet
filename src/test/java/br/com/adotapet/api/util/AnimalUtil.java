package br.com.adotapet.api.util;

import br.com.adotapet.api.domain.entity.*;
import br.com.adotapet.api.domain.util.DataUtil;

public class AnimalUtil {
    public static Animal criarAnimal() {
        return new Animal.Builder()
                .nome("Bob")
                .dataNascimento(DataUtil.newDate("01/01/2026"))
                .sexo(TipoSexo.MACHO)
                .especie(TipoEspecie.CACHORRO)
                .porte(TipoPorte.MEDIO)
                .historia("O nome do cachorro é Bob")
                .build();
    }

    public static Animal criarAnimalComFoto() {
        var animal = criarAnimal();
        animal.adicionarFoto(criarFoto());

        return animal;
    }

    public static Animal animalAdotado() {
        var solicitacao = SolicitacaoUtil.solicitacaoAprovada();
        return solicitacao.getAnimal();
    }

    public static Animal animalEmProcessoAdocao() {
        var solicitacao = SolicitacaoUtil.criarSolicitacao();
        return solicitacao.getAnimal();
    }

    private static Foto criarFoto() {
        return new Foto("file-name-1.jpg", "https://example.com/foto1.jpg","Y29udGVudC0x" );
    }
}
