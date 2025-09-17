import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SIMULADOR DE ESCALONAMENTO icevOS ===\n");
        int quantum = 2; //quantum padrao do simulador, pode ser alterado aqui
        
        Espera listaEspera = new Espera(quantum); //cria o objeto de gerenciamento de processos e passa o quantum definido acima
        
        String nomeArquivo = "processos.txt"; // nome do arquivo de entrada
        // tenta ler os processos do arquivo predefinido acima, se nao houver ele ira executar com processos de exemplo hardcoded
        try {
            int processosLidos = lerProcessosDoArquivo(nomeArquivo, listaEspera);
            
            if (processosLidos == 0) {
                System.out.println("Nenhum processo foi encontrado no arquivo ou arquivo está vazio.");
                System.out.println("Executando com processos de exemplo...\n");
                executarComProcessosDeExemplo(listaEspera);
                return;
            }
            
            System.out.println("\nTotal de " + processosLidos + " processos lidos do arquivo.\n");
            
            System.out.println("=".repeat(50));
            System.out.println("Estado inicial do sistema:");
            listaEspera.mostrarEstadoFilas();
            
            
            listaEspera.executarSimulacao();
            
            
            mostrarEstatisticasFinais(listaEspera);
            
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            System.out.println("Certifique-se de que o arquivo '" + nomeArquivo + "' existe e está no diretório correto.");
            
            System.out.println("\nExecutando com processos de exemplo...\n");
            executarComProcessosDeExemplo(listaEspera);
        }
    }
    
    
     // le os processos de um arquivo e os adiciona ao sistema
    private static int lerProcessosDoArquivo(String nomeArquivo, Espera sistema) throws IOException {
    int processosLidos = 0;
    
    try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
        String linha;
        int numeroLinha = 0;
        
        System.out.println("Lendo processos do arquivo " + nomeArquivo + ":\n");
        
        while ((linha = br.readLine()) != null) {
            numeroLinha++;
            
            linha = linha.trim();
            if (linha.isEmpty() || linha.startsWith("#")) {
                continue;
            }
            
            try {
                Processos processo = processarLinha(linha);
                sistema.chegadaProcesso(processo);
                processosLidos++;
                
                //print so mostra detalhes se poucos processos ou a cada 1000, para otimizar a leitura de muitos processos
                if (processosLidos <= 20 || processosLidos % 1000 == 0) {
                    System.out.println("Processo " + processo.getNome() + 
                                     " adicionado (ID: " + processo.id +
                                     ", Prioridade: " + processo.getPrioridade() + 
                                     ", Ciclos: " + processo.getCiclos() + ")");
                }
                                     
            } catch (Exception e) {
                System.err.println("Erro na linha " + numeroLinha + ": " + linha);
                System.err.println("  Motivo: " + e.getMessage());
                System.err.println("  Formato esperado: id;NOME;prioridade;ciclos_necessarios;recurso_necessario (se houver)");
            }
        }
        
        //resumo final se tiver muitos processos
        if (processosLidos > 20) {
            System.out.println("... (" + (processosLidos - 20) + " processos adicionais carregados)");
        }
    }
    
    return processosLidos;
}
    // processa uma linha do arquivo e cria um objeto processo
     private static Processos processarLinha(String linha) throws IllegalArgumentException {
        String[] partes = linha.split(";");
        
        if (partes.length != 4 && partes.length != 5) {
            throw new IllegalArgumentException("Linha deve ter 4 ou 5 campos separados por ';'");
        }
        
        try {
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();
            int prioridade = Integer.parseInt(partes[2].trim());
            int ciclos = Integer.parseInt(partes[3].trim());
            
            String recurso = "CPU"; // recurso padrao caso nao seja fornecido
            if (partes.length == 5) {
                recurso = partes[4].trim();
            }
            
            // validações basicas dos dados do arquivo
            if (nome.isEmpty()) {
                throw new IllegalArgumentException("Nome não pode estar vazio");
            }
            if (prioridade < 1 || prioridade > 3) {
                throw new IllegalArgumentException("Prioridade deve ser 1, 2 ou 3");
            }
            if (ciclos <= 0) {
                throw new IllegalArgumentException("Ciclos necessários deve ser maior que 0");
            }
            
            Processos processo = new Processos();
            processo.id = id;
            processo.nome = nome;
            processo.prioridade = prioridade;
            processo.ciclos_necessarios = ciclos;
            processo.recursos_necessarios = recurso; 
            
            return processo;
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erro ao converter números: " + e.getMessage());
        }
    }
    // Executa a simulação com processos de exemplo criados hardcoded
    private static void executarComProcessosDeExemplo(Espera listaEspera) {
        System.out.println("Criando processos de exemplo...\n");
        
        Processos p1 = criarProcesso(1, "ProcessoA", 1, 8, "CPU, Memoria");
        Processos p2 = criarProcesso(2, "ProcessoB", 2, 6, "CPU, Disco");
        Processos p3 = criarProcesso(3, "ProcessoC", 3, 4, "CPU");
        Processos p4 = criarProcesso(4, "ProcessoD", 1, 5, "CPU, Rede");
        Processos p5 = criarProcesso(5, "ProcessoE", 2, 3, "CPU, Memoria");
        
        listaEspera.chegadaProcesso(p1);
        listaEspera.chegadaProcesso(p2);
        listaEspera.chegadaProcesso(p3);
        listaEspera.chegadaProcesso(p4);
        listaEspera.chegadaProcesso(p5);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Estado inicial do sistema:");
        listaEspera.mostrarEstadoFilas();
        
        listaEspera.executarSimulacao();
        mostrarEstatisticasFinais(listaEspera);
    }
    // Cria um processo com os parâmetros fornecidos pelo sistema
     private static Processos criarProcesso(int id, String nome, int prioridade, 
                                         int ciclos, String recursos) {
        Processos processo = new Processos();
        processo.id = id;
        processo.nome = nome;
        processo.prioridade = prioridade;
        processo.ciclos_necessarios = ciclos;
        processo.recursos_necessarios = recursos; 
        
        //System.out.println("Criado: " + nome + " (Prioridade: " + prioridade + 
                          //", Ciclos: " + ciclos + ", Recursos: " + recursos + ")");
        
        return processo;
    }
    //retorno das estatisticas finais do sistema para o usuario
    private static void mostrarEstatisticasFinais(Espera sistema) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RELATORIO FINAL");
        System.out.println("=".repeat(50));
        System.out.println("Processos completados: " + sistema.getTotalCompletos());
        System.out.println("Processos pendentes: " + sistema.getTotalPendentes());
        System.out.println("Processos em execução: " + sistema.getTotalEmExecucao());
        
        if (sistema.getTotalPendentes() == 0 && sistema.getTotalEmExecucao() == 0) {
            System.out.println("\nTODOS OS PROCESSOS FORAM EXECUTADOS COM SUCESSO");
        } else {
            System.out.println("\n Ainda há processos pendentes ou em execução.");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Simulação finalizada.");
    }
}