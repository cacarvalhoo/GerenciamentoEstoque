# Gerenciamento de Estoque

Aplicativo desktop em Java (Swing) para controlar o estoque de uma loja de cosméticos. Permite cadastrar produtos, atualizar quantidades, excluir itens, consultar o que está com estoque baixo e gerar um backup em texto. Os dados ficam em um banco SQLite local.

## Funcionalidades

- **Inserir** produto (nome e quantidade), com validação de nome obrigatório e quantidade ≥ 0.
- **Atualizar** a quantidade de um produto pelo código.
- **Excluir** produto pelo código.
- **Listar todos** os produtos na tabela.
- **Estoque < 10**: lista apenas os produtos com menos de 10 unidades.
- **Fazer backup**: grava todos os produtos em `backup_estoque.txt`.

## Estrutura do projeto

```
GerenciamentoEstoque/
├── src/
│   ├── App.java                 # classe gerada pelo VS Code (não é o ponto de entrada)
│   ├── model/Produto.java       # entidade Produto
│   ├── dao/Conexao.java         # conexão com o SQLite
│   ├── dao/ProdutoDAO.java      # operações no banco (CRUD e consultas)
│   ├── backup/BackupService.java# exportação do backup em texto
│   └── view/MainFrame.java      # interface gráfica e ponto de entrada
├── lib/sqlite-jdbc-3.53.2.0.jar # driver JDBC do SQLite
├── banco.sql                    # script SQL da tabela produto
└── bin/                         # arquivos compilados (.class)
```

## Classes

| Classe | Pacote | Responsabilidade |
|---|---|---|
| `Produto` | `model` | Guarda `codigo`, `nome` e `quantidade`. |
| `Conexao` | `dao` | Abre a conexão com `jdbc:sqlite:estoque.db`. |
| `ProdutoDAO` | `dao` | Cria a tabela e executa inserir, atualizar, excluir, listar todos e listar estoque baixo, com `PreparedStatement` e validações. |
| `BackupService` | `backup` | Escreve a lista de produtos em arquivo, no formato `codigo;nome;quantidade`. |
| `MainFrame` | `view` | Janela Swing com formulários, tabela e botões. Contém o `main`. |

## Banco de dados

Tabela `produto`:

| Coluna | Tipo | Observação |
|---|---|---|
| `codigo` | INTEGER | chave primária, autoincremento |
| `nome` | TEXT | obrigatório |
| `quantidade` | INTEGER | obrigatório, não negativo |

A tabela é criada automaticamente na primeira execução. O arquivo `estoque.db` é criado na pasta de onde o programa é executado. O script `banco.sql` está disponível para criar a tabela manualmente, se preferir.

## Requisitos

- JDK 11 ou superior
- Driver `sqlite-jdbc` (já incluído em `lib/`)

## Como compilar

Na pasta raiz do projeto:

**Linux / macOS**

```bash
javac -cp "lib/*" -d bin src/model/*.java src/dao/*.java src/backup/*.java src/view/*.java
```

**Windows (PowerShell ou CMD)**

```bat
javac -cp "lib/*" -d bin src\model\*.java src\dao\*.java src\backup\*.java src\view\*.java
```

## Como executar

Também na pasta raiz:

**Linux / macOS**

```bash
java -cp "bin:lib/*" view.MainFrame
```

**Windows**

```bat
java -cp "bin;lib/*" view.MainFrame
```

No VS Code, também é possível abrir `src/view/MainFrame.java` e clicar em **Run**.

## Backup

O botão **Fazer Backup** gera `backup_estoque.txt` na pasta de execução, neste formato:

```
=== BACKUP DE ESTOQUE ===
1;Shampoo Hidratante;25
2;Condicionador Nutritivo;18
```

## Tecnologias

Java, Swing, JDBC e SQLite.