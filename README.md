# Simulador de Escalonamento icevOS

## Informações Acadêmicas

**Disciplina:** [Algoritmo e Estrutura de Dados]  
**Professor:** [Dimmy Magalhães]  
**Turma:** [Borg]

### Integrantes do Projeto
- **Nome:** [Dhian Pablo Dias Sepedro] 
- **Nome:** [Gabriel Leal Batista]
- **Nome:** [Arthur Hagel Porfirio Mendes] 

**Repositório:** [https://github.com/DhianSepedro/escalonadordeprocessos]

---

## Descrição do Projeto

O **icevOS** é um simulador de escalonamento de processos desenvolvido em Java que implementa algoritmos de gerenciamento de processos com múltiplas filas de prioridade e sistema Round Robin com quantum configurável.

### Características Principais

- **Escalonamento por Prioridades:** Três níveis de prioridade (1-Alta, 2-Média, 3-Baixa)
- **Round Robin:** Implementação com quantum configurável (padrão: 2 ciclos)
- **Sistema Anti-Inanição:** Previne que processos de baixa prioridade fiquem indefinidamente sem execução
- **Gerenciamento de Recursos:** Simula bloqueio/desbloqueio de processos que necessitam de recursos específicos (Disco, Memória, Rede)
- **Lista Circular:** Estrutura de dados otimizada para escalonamento
- **Leitura de Arquivos:** Suporte para carregamento de processos via arquivo de texto

### Componentes do Sistema

1. **Main.java** - Classe principal que gerencia a inicialização e leitura de processos
2. **Processos.java** - Representa um processo do sistema com seus atributos
3. **Espera.java** - Gerencia as filas de prioridade e coordena o escalonamento
4. **Escalonador.java** - Implementa o algoritmo Round Robin com lista circular
5. **Listasencadeadas.java** - Estrutura de dados para as filas de processos
6. **Node.java** - Nó da lista duplamente encadeada

---

## Como Compilar e Executar

### Pré-requisitos

- **Java Development Kit (JDK)** versão 8 ou superior
- Terminal/Prompt de comando

### Compilação

1. **Clone ou baixe o projeto:**
   ```bash
   git clone [URL_DO_REPOSITÓRIO]
   cd simulador-icevos
   ```

2. **Compile todos os arquivos Java:**
   ```bash
   javac *.java
   ```

   Ou compile arquivo por arquivo:
   ```bash
   javac Node.java
   javac Processos.java  
   javac Listasencadeadas.java
   javac Escalonador.java
   javac Espera.java
   javac Main.java
   ```

### Execução

#### Método 1: Com arquivo de processos (Recomendado)

1. **Crie um arquivo chamado `processos.txt`** no mesmo diretório dos arquivos compilados:
   ```
   # Formato: id;NOME;prioridade;ciclos_necessarios;recurso_necessario
   1;ProcessoA;1;10;CPU, Memoria
   2;ProcessoB;2;8;CPU, Disco  
   3;ProcessoC;3;6;CPU
   4;ProcessoD;1;12;CPU, Rede
   5;ProcessoE;2;5;CPU, Memoria
   ```

2. **Execute o simulador:**
   ```bash
   java Main
   ```

#### Método 2: Com processos de exemplo

Se não houver o arquivo `processos.txt`, o sistema executará automaticamente com processos de exemplo predefinidos:

```bash
java Main
```

### Configuração do Quantum

Para alterar o quantum padrão (2 ciclos), edite a linha 8 do arquivo `Main.java`:

```java
int quantum = 2; // Altere este valor conforme necessário
```

Recompile e execute novamente.

---

## Formato do Arquivo de Entrada

O arquivo `processos.txt` deve seguir o formato:

```
id;NOME;prioridade;ciclos_necessarios;recurso_necessario
```

**Onde:**
- **id:** Identificador único do processo (número inteiro)
- **NOME:** Nome descritivo do processo (string sem espaços ou use aspas)
- **prioridade:** Nível de prioridade (1=Alta, 2=Média, 3=Baixa)
- **ciclos_necessarios:** Número de ciclos de CPU necessários (número inteiro > 0)
- **recurso_necessario:** Recursos necessários separados por vírgula (opcional - padrão: "CPU")

### Exemplo de arquivo válido:
```
# Este é um comentário - será ignorado
1;SistemaOperacional;1;15;CPU, Memoria
2;EditorTexto;2;8;CPU, Disco
3;Calculadora;3;4;CPU
4;NavegadorWeb;1;20;CPU, Rede, Memoria
5;PlayerMusica;2;6;CPU, Disco
```

---

## Saída do Sistema

O simulador produz:

1. **Estado Inicial:** Mostra a distribuição inicial dos processos nas filas
2. **Progresso da Simulação:** Atualiza o estado das filas periodicamente
3. **Relatório Final:** Estatísticas completas da execução:
   - Total de processos completados
   - Processos pendentes/em execução
   - Quantums totais utilizados

### Exemplo de Saída:
```
=== SIMULADOR DE ESCALONAMENTO icevOS ===

Total de 5 processos lidos do arquivo.

==================================================
Estado inicial do sistema:

--- Estado das Filas ---
Alta prioridade: 2 processos
Média prioridade: 2 processos  
Baixa prioridade: 1 processos
Bloqueados: 0 processos
Em execução: 0 processos
Completados: 0 processos

=== INICIANDO SIMULACAO ===
[... progresso da simulação ...]

==================================================
RELATORIO FINAL
==================================================
Processos completados: 5
Processos pendentes: 0
Processos em execução: 0

TODOS OS PROCESSOS FORAM EXECUTADOS COM SUCESSO
```

---

## Funcionalidades Avançadas

### Sistema Anti-Inanição
- Após 5 processos consecutivos de alta prioridade, o sistema força a execução de um processo de média ou baixa prioridade

### Gerenciamento de Recursos  
- Processos que necessitam de "DISCO" são automaticamente bloqueados na primeira execução
- Sistema de desbloqueio automático a cada ciclo

### Otimizações
- Para arquivos com muitos processos (>1000), apenas resumos são exibidos
- Atualizações de estado otimizadas para grandes volumes
- Inibido alguns prints do escalonador, para otimizar o terminal e evitar gargalo na execução (para ativar basta remover as "//" na frente de cada "System.out.Println")

---

## Possíveis Problemas e Soluções

### Erro: "Could not find or load main class Main"
**Solução:** Verifique se todos os arquivos foram compilados corretamente e se você está no diretório correto.

### Erro: "Arquivo não encontrado"
**Solução:** Certifique-se de que o arquivo `processos.txt` está no mesmo diretório dos arquivos `.class` compilados.

### Formato inválido no arquivo
**Solução:** Verifique se cada linha segue exatamente o formato: `id;NOME;prioridade;ciclos;recurso`

---

## Tecnologias Utilizadas

- **Linguagem:** Java
- **Estruturas de Dados:** Listas Duplamente Encadeadas Circulares
- **Algoritmos:** Round Robin, Escalonamento por Prioridades
- **Paradigma:** Programação Orientada a Objetos

---

Este projeto foi desenvolvido para fins educacionais como parte da disciplina de [Algoritmo e Estrutura de Dados].

---

