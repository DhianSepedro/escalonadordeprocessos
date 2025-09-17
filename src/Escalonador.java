public class Escalonador {
    Listasencadeadas escalonador;
    Node atual;
    int quantum;

    
    public Escalonador(int quantum) {
    this.escalonador = new Listasencadeadas();
    this.quantum = quantum;
    this.atual = null;
}
// adiciona um processo ao escalonador
   public void adicionarProcesso(Processos processo) {
        escalonador.adicionarProcesso(processo);
        if (atual == null) {
            atual = escalonador.head;
        }
        circular();
    }
    // remove o processo atual do escalonador
   private void removerProcesso() {
    if (atual == null || tamanho() == 0) {
        return;
    }
    
    if (tamanho() == 1) {
        atual = null;
        escalonador.head = null;
        escalonador.tail = null;
        escalonador.size = 0;
    } else {
        // SUPER ATUALIZAÇAO DE VERAO DA LISTA DUPLAMENTE ENCADEADA
        atual.anterior.proximo = atual.proximo;
        atual.proximo.anterior = atual.anterior;
        
        if (atual == escalonador.head) {
            escalonador.head = atual.proximo;
        }
        if (atual == escalonador.tail) {
            escalonador.tail = atual.anterior;
        }
        atual = atual.proximo;
        escalonador.size--;
    }
    circular();
}
    //recupera o tamanho do escalonador
    public int tamanho() {
        return escalonador.size;
    }
    // transforma a lista em circular
   private void circular(){ 
    if(escalonador.tail != null && escalonador.head != null){
        escalonador.tail.proximo = escalonador.head;
        escalonador.head.anterior = escalonador.tail; 
    }
}
    // executa o quantum do processo atual
    public void executarQuantum() {
    if (atual == null || tamanho() == 0) {
        return;
    }
        Processos processoAtual = atual.processo;
       // System.out.println("Executando processo: " + processoAtual.getNome());
        processoAtual.ciclos_necessarios -= quantum;
        if (processoAtual.ciclos_necessarios <= 0) {
            //System.out.println("Processo " + processoAtual.getNome() + " concluído.");
            removerProcesso();
        } else {
            //System.out.println("Processo " + processoAtual.getNome() + " pausado. Ciclos restantes: " + processoAtual.getCiclos());
            atual = atual.proximo;
        }
    }
    
        
    
    

}

