public class Processos {
    int id;
    String nome;
    int prioridade;
    int ciclos_necessarios;
    String recursos_necessarios;
    boolean jaUsouDisco = false; 
    
    public int getCiclos() {
        return ciclos_necessarios;
    }
    public int getPrioridade() {
        return prioridade;
    }
    public String getNome() {
        return nome;
    }
    public boolean precisaDisco() {
        return recursos_necessarios != null && 
               recursos_necessarios.toUpperCase().contains("DISCO");
    }
}