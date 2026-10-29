# MoviesManager

Aplicativo Android (Kotlin) para cadastrar, visualizar, editar, remover e
ordenar os filmes favoritos do usuário, com persistência local em SQLite.

[![Assista ao vídeo explicativo do MoviesManager](https://img.youtube.com/vi/Nn0YsVyqOkU/0.jpg)](https://www.youtube.com/watch?v=Nn0YsVyqOkU)

## Funcionalidades

- Listagem dos filmes cadastrados, com nome, ano, gênero, duração e nota.
- Cadastro de novo filme, com seleção do gênero por um `Spinner`.
- Visualização de todos os detalhes de um filme ao tocar na célula da lista.
- Edição de um filme pelo botão de lápis da célula — todos os campos são
  editáveis, exceto o nome.
- Remoção de um filme pelo botão de lixeira da célula.
- Ordenação da lista por nome ou por nota, pelo menu de overflow da toolbar.
- Validação do formulário: nome obrigatório e único, ano e duração
  obrigatórios, nota opcional entre 0 e 10.

## Arquitetura

Arquitetura MVC, separada em pacotes:

- `model` — entidade `Movie`, o enum `MovieOrder`, as constantes de
  comunicação entre telas (`Constant`) e a camada de persistência
  (`MovieDao`, interface, e `MovieSqlite`, a implementação em SQLite puro).
- `controller` — `MainController`, uma camada fina entre a `view` e o `model`.
- `view` — as duas `Activity` (`MainActivity` e `MovieActivity`) e o contrato
  de clique da lista (`OnMovieClickListener`).
- `adapter` — `MovieRvAdapter`, o adapter do `RecyclerView` da tela principal.

## Persistência

Os dados são gravados em um banco SQLite próprio do aplicativo (arquivo
`moviesManager`), na tabela `movie`:

```sql
CREATE TABLE IF NOT EXISTS movie (
  name         TEXT    NOT NULL PRIMARY KEY COLLATE NOCASE,
  release_year INTEGER NOT NULL,
  studio       TEXT    NOT NULL,
  duration     INTEGER NOT NULL,
  watched      INTEGER NOT NULL DEFAULT 0,
  rating       INTEGER CHECK (rating IS NULL OR rating BETWEEN 0 AND 10),
  genre        TEXT    NOT NULL
);
```

O nome do filme é a chave primária: é único (`PRIMARY KEY`), não pode ser
vazio (`NOT NULL`) e a comparação ignora maiúsculas/minúsculas
(`COLLATE NOCASE`). O campo `watched` é gravado como `0`/`1`. A nota
(`rating`) pode ficar em branco (`NULL`) até o filme ser assistido, e o banco
garante, pelo próprio `CHECK`, que qualquer valor gravado esteja entre 0 e 10.
A ordenação da lista (por nome ou por nota) é feita pelo próprio SQL
(`ORDER BY`), não em memória.

## Como executar

1. Abra a pasta `MoviesManager` no Android Studio (versão estável recente).
2. Aguarde o Gradle Sync (ele baixa o SDK 36 e o Gradle 8.13 se necessário).
3. Rode o app (`Run`) em um emulador ou celular com Android 8.0 (API 26) ou
   superior.

## Vídeo de demonstração

_(link a ser adicionado)_
