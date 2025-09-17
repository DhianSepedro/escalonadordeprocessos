public class Espera {
    private Listasencadeadas listaEspera;        
    private Listasencadeadas altaPrioridade;   
    private Listasencadeadas mediaPrioridade;    
    private Listasencadeadas baixaPrioridade;    
    private Listasencadeadas listaBloqueados; 
    private Listasencadeadas listaCompletos;     
    private Escalonador escalonador;          
    private int processosAltaConsecutivos = 0; //contador para a logica de anti-inanição do sistema
    
    
    public Espera(int quantum) {
        this.listaEspera = new Listasencadeadas();
        this.altaPrioridade = new Listasencadeadas();
        this.mediaPrioridade = new Listasencadeadas();
        this.baixaPrioridade = new Listasencadeadas();
        this.listaBloqueados = new Listasencadeadas(); 
        this.listaCompletos = new Listasencadeadas();
        this.escalonador = new Escalonador(quantum);
        this.processosAltaConsecutivos = 0;
    }
    
    // adiciona um processo na lista de espera e organiza por prioridade
    public void chegadaProcesso(Processos processo) {
       //System.out.println("Processo " + processo.getNome() + " adicionado à lista de espera");
        listaEspera.adicionarProcesso(processo);
        organizarPorPrioridade();
    }
    
    // separa os processos da lista de espera em filas de prioridade
    private void organizarPorPrioridade() {
        
        while (listaEspera.size > 0) {
            Node atual = listaEspera.head;
            Processos processo = atual.processo;
            
            switch (processo.getPrioridade()) {
                case 1: 
                    altaPrioridade.adicionarProcesso(processo);
                    //System.out.println("Processo " + processo.getNome() + " -> ALTA prioridade");
                    break;
                case 2: 
                    mediaPrioridade.adicionarProcesso(processo);
                    //System.out.println("Processo " + processo.getNome() + " -> MÉDIA prioridade");
                    break;
                case 3: 
                    baixaPrioridade.adicionarProcesso(processo);
                    //System.out.println("Processo " + processo.getNome() + " -> BAIXA prioridade");
                    break;
                default:
                    mediaPrioridade.adicionarProcesso(processo); 
                    break;
            }
            
            listaEspera.removerProcesso(); 
        }
    }
    // seleciona o proximo processo a ser executado com base na prioridade e na logica de anti-inanição
     public Processos selecionarProximoProcesso() { 
        if (processosAltaConsecutivos >= 5) {  //executa anti-inanição se 5 processos de alta prioridade foram executados consecutivamente
            if (mediaPrioridade.size > 0) {
                processosAltaConsecutivos = 0; 
                //System.out.println("ANTI-INANIÇÃO: Executando processo de media prioridade");
                return removerDaFila(mediaPrioridade);
            } else if (baixaPrioridade.size > 0) {
                processosAltaConsecutivos = 0; 
                //System.out.println("ANTI-INANIÇÃO: Executando processo de baixa prioridade");
                return removerDaFila(baixaPrioridade);
            }
            processosAltaConsecutivos = 0;
        }
        if (altaPrioridade.size > 0) { //prioriza processos de alta prioridade, ja que a o sistema de anti-inanição nao foi acionado
            processosAltaConsecutivos++;
            //System.out.println("Selecionando ALTA prioridade (consecutivos: " + 
                            // processosAltaConsecutivos + "/5)");
            return removerDaFila(altaPrioridade);
            
        } else if (mediaPrioridade.size > 0) {
            processosAltaConsecutivos = 0;
            //System.out.println("Selecionando MÉDIA prioridade");
            return removerDaFila(mediaPrioridade);
            
        } else if (baixaPrioridade.size > 0) {
            processosAltaConsecutivos = 0; 
            //System.out.println("Selecionando BAIXA prioridade");
            return removerDaFila(baixaPrioridade);
        }
        
        return null;
    }
    // remove e retorna o processo do inicio da fila especificada
    private Processos removerDaFila(Listasencadeadas fila) {
        if (fila.size > 0) {
            Processos processo = fila.head.processo;
            fila.removerProcesso();
            return processo;
        }
        return null;
    }
    // verifica se o processo precisa de disco e o move para bloqueados se necessario
      public Processos verificarEPrepararProcesso(Processos processo) {
        if (processo == null) return null;
        if (processo.precisaDisco() && !processo.jaUsouDisco) {
            //System.out.println("BLOQUEIO: Processo " + processo.getNome() + 
                             //" precisa do DISCO - movendo para bloqueados");
            processo.jaUsouDisco = true;
            listaBloqueados.adicionarProcesso(processo);
            return null;
        }
        return processo; 
    }
    // desbloqueia o processo mais antigo da fila de bloqueados e o retorna para sua fila original
    public void desbloquearProcessoMaisAntigo() {
        if (listaBloqueados.size > 0) {
            Processos processoDesbloqueado = listaBloqueados.head.processo;
            listaBloqueados.removerProcesso();
            
            //System.out.println("DESBLOQUEIO: Processo " + processoDesbloqueado.getNome() + 
              //" foi desbloqueado e retorna à sua fila original");
            switch (processoDesbloqueado.getPrioridade()) {
                case 1:
                    altaPrioridade.adicionarProcesso(processoDesbloqueado);
                    break;
                case 2:
                    mediaPrioridade.adicionarProcesso(processoDesbloqueado);
                    break;
                case 3:
                    baixaPrioridade.adicionarProcesso(processoDesbloqueado);
                    break;
            }
        }
    }
    // metodo para executar a simulacao completa
    public void executarSimulacao() {
        System.out.println("\n=== INICIANDO SIMULACAO ===");
        mostrarEstadoFilas();
        int quantumpassados= 0;
        while (temProcessosPendentes()) {
            //System.out.println("\n--- Quantumns usados: " + quantumpassados + " ---");
            desbloquearProcessoMaisAntigo();
            if (escalonador.tamanho() == 0) {
                Processos proximo = selecionarProximoProcesso();
                proximo = verificarEPrepararProcesso(proximo);
                if (proximo != null) {
                    escalonador.adicionarProcesso(proximo);
                    //System.out.println("Processo " + proximo.getNome() + 
                                     //" enviado para o escalonador");
                } else {
                    //System.out.println("Nenhum processo disponivel para executar neste ciclo");
                }
            }
            
            if (escalonador.tamanho() > 0) {
                Processos processoAntes = escalonador.atual.processo;
                escalonador.executarQuantum();
                
                if (escalonador.tamanho() == 0 || escalonador.atual.processo != processoAntes) {
                    if (processoAntes.getCiclos() <= 0) {
                        listaCompletos.adicionarProcesso(processoAntes);
                        //System.out.println("Processo " + processoAntes.getNome() + 
                                         //" foi movido para COMPLETOS");
                    }
                }
            }   
            quantumpassados += escalonador.quantum;
            if (quantumpassados % 1000 == 0) { //para muitos processos aumente o valor(otimizador)||mostra o estado das filas a cada 100 quantuns, otimizando os souts 
                mostrarEstadoFilas();
            }
        
        }
        System.out.println("\n=== SIMULAÇÃO CONCLUIDA ===");
        System.out.println("Quantum total: " + quantumpassados);
        System.out.println("Processos completados: " + listaCompletos.size);
    }
    // verifica se ainda ha processos pendentes em qualquer fila
    private boolean temProcessosPendentes() {
        return (altaPrioridade.size > 0) || 
               (mediaPrioridade.size > 0) || 
               (baixaPrioridade.size > 0) || 
               (escalonador.tamanho() > 0);
    }
    
   public void mostrarEstadoFilas() {
        System.out.println("\n--- Estado das Filas ---");
        System.out.println("Alta prioridade: " + altaPrioridade.size + " processos");
        System.out.println("Média prioridade: " + mediaPrioridade.size + " processos");
        System.out.println("Baixa prioridade: " + baixaPrioridade.size + " processos");
        System.out.println("Bloqueados: " + listaBloqueados.size + " processos"); 
        System.out.println("Em execução: " + escalonador.tamanho() + " processos");
        System.out.println("Completados: " + listaCompletos.size + " processos");
        System.out.println("Anti-inanição: " + processosAltaConsecutivos + "/5 processos alta consecutivos");
    }
    // adiciona multiplos processos de uma vez ao sistema
    public void adicionarProcessos(Processos... processos) {
        for (Processos processo : processos) {
            chegadaProcesso(processo);
        }
    }
    public int getTotalCompletos() { return listaCompletos.size; }
    public int getTotalPendentes() { return altaPrioridade.size + mediaPrioridade.size + baixaPrioridade.size; }
    public int getTotalEmExecucao() { return escalonador.tamanho(); }
}