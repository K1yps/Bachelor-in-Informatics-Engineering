
/**
 * Escreva a descrição da classe LinhaEncomenda aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class LinhaEncomenda {
    private String cod,nome;
    private double preco;
    private int quantidade;

    public LinhaEncomenda(String c,String n,double p, int q) {
        this.cod=c;
        this.nome=n;
        this.preco=p;
        this.quantidade=q;
    }
    
    public LinhaEncomenda(LinhaEncomenda l) {
        this.cod=l.getCod();
        this.nome=l.getNome();
        this.preco=l.getPreco();
        this.quantidade=l.getQuantidade();
    }
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public LinhaEncomenda clone() {
        LinhaEncomenda res=new LinhaEncomenda(this);
        return res;
    }
}
