# 🖥️ Simulador de Escalonamento icevOS

Um simulador de escalonamento de processos desenvolvido em Java que implementa algoritmos de prioridade com Round Robin e mecanismos anti-inanição.

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Características](#-características)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar](#-como-executar)
- [Formato do Arquivo de Processos](#-formato-do-arquivo-de-processos)
- [Algoritmo de Escalonamento](#-algoritmo-de-escalonamento)
- [Exemplo de Uso](#-exemplo-de-uso)
- [Estruturas de Dados](#-estruturas-de-dados)

## 🎯 Visão Geral

O icevOS é um simulador que demonstra como um sistema operacional gerencia processos usando:
- **Escalonamento por Prioridades** (1=Alta, 2=Média, 3=Baixa)
- **Round Robin** com quantum configurável
- **Mecanismo Anti-inanição** para processos de baixa prioridade
- **Gerenciamento de Recursos** (CPU, Disco, Memória, Rede)
- **Bloqueio/Desbloqueio** de processos que precisam de disco

## ✨ Características

- 📊 **Múltiplas filas de prioridade** com algoritmo anti-starvation
- ⏰ **Quantum configurável** para execução Round Robin
- 🔒 **Sistema de bloqueio** para processos que precisam de recursos especiais
- 📁 **Leitura de processos** via arquivo `.txt`
- 📈 **Relatórios detalhados** do estado do sistema
- 🔄 **Execução circular** dos processos no escalonador

## 📁 Estrutura do Projeto

```
icevOS/
├── src/
│   ├── Main.java              # Classe principal e leitura de arquivos
│   ├── Processos.java         # Modelo de processo
│   ├── Node.java              # Nó da lista encadeada
│   ├── Listasencadeadas.java  # Implementação de lista encadeada
│   ├── Escalonador.java       # Escalonador Round Robin
│   └── Espera.java            # Gerenciador de filas de prioridade
├── processos.txt              # Arquivo de processos (opcional)
└── README.md
```

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior
- Compilador javac

### Compilação
```bash
javac *.java
```

### Execução
```bash
java Main
```

### Execução com Processos Personalizados
1. Crie um arquivo `processos.txt` no mesmo diretório
2. Execute o programa - ele lerá automaticamente do arquivo
3. Se o arquivo não existir, o programa usará processos de exemplo

## 📄 Formato do Arquivo de Processos

Crie um arquivo `processos.txt` com o seguinte formato:

```
# Formato: id;nome;prioridade;ciclos_necessarios;recursos
1;ProcessoA;1;8;CPU, Memoria
2;ProcessoB;2;6;CPU, Disco
3;ProcessoC;3;4;CPU
4;ProcessoD;1;5;CPU, Rede
5;ProcessoE;2;3;CPU, Memoria
```

### Parâmetros:
- **id**: Identificador único do processo (número inteiro)
- **nome**: Nome descritivo do processo
- **prioridade**: 1 (Alta), 2 (Média), 3 (Baixa)
- **ciclos_necessarios**: Quantidade de quantum necessários para conclusão
- **recursos**: Lista de recursos separados por vírgula (CPU, Disco, Memoria, Rede)

### Regras:
- Linhas vazias e que começam com `#` são ignoradas
- O campo `recursos` é opcional (padrão: "CPU")
- Processos que precisam de "Disco" serão bloqueados na primeira execução

## ⚙️ Algoritmo de Escalonamento

### 1. **Organização por Prioridade**
- Os processos são organizados em três filas: Alta, Média e Baixa prioridade
- Processos de alta prioridade têm preferência na execução

### 2. **Mecanismo Anti-inanição**
- Após 5 processos consecutivos de alta prioridade, o sistema força a execução de processos de menor prioridade
- Garante que todos os processos eventualmente sejam executados

### 3. **Round Robin**
- Cada processo executa por um quantum de tempo (configurável)
- Se não terminar, retorna ao final da fila para nova execução
- Quantum padrão: 2 ciclos

### 4. **Gerenciamento de Recursos**
- Processos que precisam de disco são bloqueados na primeira execução
- A cada ciclo, o processo bloqueado mais antigo é desbloqueado
- Processos desbloqueados retornam à sua fila original de prioridade

## 💡 Exemplo de Uso

### Saída do Programa:
```
=== SIMULADOR DE ESCALONAMENTO icevOS ===

Lendo processos do arquivo processos.txt:

✓ Processo ProcessoA adicionado (ID: 1, Prioridade: 1, Ciclos: 8)
✓ Processo ProcessoB adicionado (ID: 2, Prioridade: 2, Ciclos: 6)
...

=== INICIANDO SIMULACAO ===

--- Estado das Filas ---
Alta prioridade: 2 processos
Média prioridade: 2 processos
Baixa prioridade: 1 processos
Bloqueados: 0 processos
Em execução: 0 processos
Completados: 0 processos

--- Quantumns usados: 0 ---
Selecionando ALTA prioridade (consecutivos: 1/5)
Executando processo: ProcessoA
Processo ProcessoA pausado. Ciclos restantes: 6

...

=== SIMULAÇÃO CONCLUÍDA ===
Quantum total: 26
Processos completados: 5
```

## 🏗️ Estruturas de Dados

### Classes Principais:

- **`Processos`**: Representa um processo com ID, nome, prioridade, ciclos e recursos
- **`Node`**: Nó de lista encadeada que contém um processo
- **`Listasencadeadas`**: Lista encadeada personalizada para gerenciar processos
- **`Escalonador`**: Implementa Round Robin circular com quantum configurável
- **`Espera`**: Gerencia múltiplas filas de prioridade e coordena todo o sistema
- **`Main`**: Classe principal com leitura de arquivos e interface do usuário

### Fluxo de Execução:
1. **Chegada**: Processos chegam e são organizados por prioridade
2. **Seleção**: Sistema seleciona próximo processo respeitando prioridades e anti-inanição
3. **Verificação**: Verifica se processo precisa de recursos especiais (bloqueio)
4. **Execução**: Processo executa por um quantum no escalonador
5. **Finalização**: Processo completo vai para lista de finalizados

## 🔧 Configuração

Para alterar o quantum, modifique a variável na classe `Main`:
```java
int quantum = 2; // Altere aqui o valor do quantum
```

## 📊 Métricas Reportadas

- Total de processos completados
- Processos ainda pendentes por prioridade
- Processos em execução atual
- Processos bloqueados aguardando recursos
- Quantum total utilizado na simulação
- Estatísticas do mecanismo anti-inanição

---

**Desenvolvido como projeto acadêmico para demonstração de algoritmos e estruturas de dados para o escalonamento de processos em Sistemas Operacionais .**
