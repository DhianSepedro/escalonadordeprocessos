public class Listasencadeadas {
    Node head;
    Node tail;
    int size;
    // adiciona um processo ao final da lista
    public void adicionarProcesso(Processos processo) {
    Node novoNo = new Node(processo);
    if (head == null) {
        head = novoNo;
        tail = novoNo;
    } else {
        tail.proximo = novoNo;
        novoNo.anterior = tail;
        tail = novoNo;
    }
    size++;
}
    // remove o processo do inicio da lista
    public void removerProcesso() {
    if (head != null) {
        head = head.proximo;
        if (head != null) {  
            head.anterior = null;
        }
        size--;
        if (head == null) {
            tail = null;
        }
    }
}
}
