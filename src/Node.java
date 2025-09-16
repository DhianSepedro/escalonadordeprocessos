public class Node {
    Processos processo;
    Node proximo;
    Node anterior;
    public Node(Processos processo) {
        this.processo = processo;
        this.proximo = null;
        this.anterior = null;
    }
}

