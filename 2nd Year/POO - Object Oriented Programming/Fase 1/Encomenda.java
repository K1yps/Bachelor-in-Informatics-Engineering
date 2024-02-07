
/**
 * Escreva a descrição da classe Encomenda aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Set;
import java.util.HashSet;
public class Encomenda
{
    private String loja,user,enc;
    private Set<LinhaEncomenda> produtos;
    private double peso;
    
    public Encomenda(Encomenda e) {
        this.loja=e.getLoja();
        this.user=e.getUser();
        this.enc=e.getEnc();
        this.peso=e.getPeso();
        this.setProdutos(e.getProdutos());
        
    }
    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
    
    public Set<LinhaEncomenda> getProdutos() {
        return new HashSet<LinhaEncomenda>(this.produtos);
    }
    
    public void setProdutos(Set<LinhaEncomenda> s){
        this.produtos=new HashSet<LinhaEncomenda>(s);
    }
    
    public Encomenda clone() {
        return new Encomenda(this);
    }

}
