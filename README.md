# AdotaPet API 🐾

API REST para gerenciamento de um sistema de adoção de pets.
O projeto está sendo desenvolvido com foco em uma arquitetura organizada, separação de responsabilidades e aplicação de regras de negócio no domínio.

## 🚧 Status do projeto

Em desenvolvimento.  
Atualmente, o projeto possui a modelagem inicial do domínio e suas entidades JPA, incluindo as regras relacionadas ao processo de solicitação de adoção.

## 🎯 Objetivo

O AdotaPet tem como objetivo disponibilizar uma API para gerenciamento de animais disponíveis para adoção, permitindo:

* Cadastro e gerenciamento de animais;
* Cadastro de fotos dos animais;
* Categorização dos animais por tags;
* Registro de solicitações de adoção;
* Controle do status do animal durante o processo de adoção;
* Aprovação ou reprovação de solicitações.

## 🛠️ Tecnologias

* **Java 21**
* **Spring Boot 4**
* **Spring Data JPA**
* **Hibernate 7**
* **Jakarta Persistence (JPA 3.2)**
* **Maven**
* **Banco de dados relacional**

---

## 🧩 Domínio

O domínio atualmente é composto pelas seguintes entidades e objetos de valor:

### Animal
Representa um pet disponível para adoção.

Possui informações como:
* Nome;
* Data de nascimento;
* Sexo;
* Espécie;
* Porte;
* História;
* Observações;
* Status;
* Fotos;
* Tags.

O animal possui três estados possíveis:
* `DISPONIVEL`
* `EM_PROCESSO_ADOCAO`
* `ADOTADO`

#### Ciclo de adoção:
```
DISPONIVEL
     │
     ▼
EM_PROCESSO_ADOCAO
     │
     ├──► ADOTADO
     │
     └──► DISPONIVEL
```

* Quando uma solicitação é criada para um animal disponível, o animal passa para `EM_PROCESSO_ADOCAO`.
* Se a solicitação for aprovada, o animal passa para `ADOTADO`.
* Se a solicitação for reprovada, o animal volta para `DISPONIVEL`, permitindo que uma nova solicitação seja realizada.

---

### Solicitacao
Representa uma solicitação de adoção realizada por um solicitante para um animal.

Uma solicitação pode possuir os seguintes status:
* `PENDENTE`
* `APROVADO`
* `REPROVADO`

#### Fluxo da solicitação:
```
PENDENTE
   │
   ├──► APROVADO
   │
   └──► REPROVADO
```

* A aprovação da solicitação também altera o status do animal para `ADOTADO`.
* A reprovação libera novamente o animal para adoção.

---

### Solicitante
Representa os dados da pessoa que solicita a adoção.

Atualmente possui:
* Nome;
* E-mail;
* Telefone;
* CPF;
* História.

`Email` e `Cpf` são objetos de valor utilizados para encapsular suas respectivas regras de validação.

---

### Foto
Representa uma foto associada a um animal.  
Um animal pode possuir várias fotos e uma foto pertence a um único animal.

* **Relacionamento:** `Animal 1 ───── N Foto`
* As fotos são removidas junto com o animal por meio de `cascade = CascadeType.ALL` e `orphanRemoval = true`.

---

### Tag
Representa uma característica utilizada para categorizar os animais.

Exemplos:
* `CASTRADO`
* `VACINADO`
* `BRINCALHAO`
* `DOCIL`

As tags são armazenadas em letras maiúsculas.  
Um animal pode possuir várias tags e uma tag pode estar associada a vários animais.

* **Relacionamento:** `Animal N ───── N Tag` (com a tabela intermediária `animal_tag`).

---

## 📐 Regras de negócio

### Solicitação de adoção
Um animal pode receber uma solicitação ativa por vez.  
Isso é representado pelo status do animal:

```
DISPONIVEL
    │
    ▼
EM_PROCESSO_ADOCAO
```

Enquanto o animal estiver em `EM_PROCESSO_ADOCAO`, uma nova solicitação não pode ser criada.

Caso a solicitação seja:
* **Aprovada:** o animal passa para `ADOTADO`;
* **Reprovada:** o animal retorna para `DISPONIVEL`.

Dessa forma, o histórico das solicitações é preservado, mas apenas uma solicitação pode estar em andamento para determinado animal.

### Aprovação
Uma solicitação só pode ser aprovada enquanto estiver com status `PENDENTE`.  
Ao aprovar:
* **Solicitacao:** `PENDENTE` → `APROVADO`
* **Animal:** `EM_PROCESSO_ADOCAO` → `ADOTADO`

### Reprovação
Uma solicitação só pode ser reprovada enquanto estiver com status `PENDENTE`.  
A reprovação exige uma justificativa.  
Ao reprovar:
* **Solicitacao:** `PENDENTE` → `REPROVADO`
* **Animal:** `EM_PROCESSO_ADOCAO` → `DISPONIVEL`

---

## 🗃️ Persistência

As entidades utilizam **Jakarta Persistence (JPA)**.

### Mapeamento de Relacionamentos
```
Animal
 ├── 1:N ── Foto
 └── N:N ── Tag

Solicitacao
 └── N:1 ── Animal

Solicitacao
 └── Solicitante (dados embutidos)
```

* **Identificadores:** Gerados utilizando `@GeneratedValue(strategy = GenerationType.IDENTITY)`
* **Timestamps:** Datas de criação e atualização utilizam `@CreationTimestamp` e `@UpdateTimestamp`
* **Enums:** Persistidos como String usando `@Enumerated(EnumType.STRING)`

---

## 🧪 Testes

Os testes das entidades e das regras de negócio fazem parte da próxima etapa de desenvolvimento.  
Entre os cenários que devem ser cobertos estão:

* [ ] Criação de um animal;
* [ ] Validação dos campos obrigatórios;
* [ ] Criação de uma solicitação;
* [ ] Bloqueio de nova solicitação enquanto o animal estiver em processo de adoção;
* [ ] Aprovação de uma solicitação;
* [ ] Reprovação de uma solicitação;
* [ ] Retorno do animal para `DISPONIVEL` após reprovação;
* [ ] Impedimento de aprovar uma solicitação já processada;
* [ ] Impedimento de reprovar uma solicitação já processada;
* [ ] Validação de CPF;
* [ ] Validação de e-mail;
* [ ] Criação e associação de fotos;
* [ ] Criação e associação de tags.

---

## ▶️ Executando o projeto

### Pré-requisitos
Antes de executar o projeto, certifique-se de possuir:
* Java 21
* Maven

### Compilação
Para verificar se o projeto está compilando corretamente:
```bash
mvn clean compile
```

### Testes
Quando os testes estiverem implementados:
```bash
mvn test
```

---

## 📁 Estrutura atual

A camada de domínio está organizada da seguinte maneira:

```text
adotapet
    └── api
        ├── application
        |      ├── advice
        |      |     └── GlobalException
        |      └── v1         
                    ├── controller
        |           └── dto
        ├── domain
        |      ├── entity
        |      |      ├── Animal.java
        |      |      ├── Cpf.java
        |      |      ├── Email.java
        |      |      ├── Foto.java
        |      |      ├── Solicitacao.java
        |      |      ├── Solicitante.java
        |      |      ├── Tag.java
        |      |      ├── StatusAnimal.java
        |      |      ├── StatusSolicitacao.java
        |      |      ├── TipoEspecie.java
        |      |      ├── TipoPorte.java
        |      |      └── TipoSexo.java
        |      ├── exception
        |      ├── repository
        |      ├── usecase
        |      └── util 
        └── infra
               ├── config
               ├── mapper
               └── repository 
```

---

## 🗺️ Roadmap

Próximas etapas planejadas:

* [ ] Implementar testes unitários das entidades;
* [ ] Implementar migrations do banco de dados;
* [ ] Criar repositories;
* [ ] Criar camada de serviços;
* [ ] Criar DTOs;
* [ ] Criar controllers e endpoints REST;
* [ ] Implementar tratamento global de exceções;
* [ ] Implementar validações da camada HTTP;
* [ ] Documentar a API;
* [ ] Implementar testes de integração;
* [ ] Configurar banco de dados para os ambientes de desenvolvimento e produção.

---

## 📌 Observações

O projeto está sendo desenvolvido de forma incremental, utilizando **Issues** e **Pull Requests** para simular um fluxo de desenvolvimento próximo ao utilizado em projetos reais.

As regras de negócio devem permanecer protegidas no domínio sempre que fizer sentido, evitando que alterações de estado importantes dependam exclusivamente da camada de infraestrutura ou dos controllers.