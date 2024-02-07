import java.util.Set;
import java.util.HashSet;
/**
 * Escreva a descrição da classe EmpresaTransportadora aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */



public class EmpresaTransportadora extends Agente
{
    private double taxa_km;
    private int capacidade;
    Set<Encomenda> encomendas_ativas; ///encomendas por entregar
    
        public EmpresaTransportadora(String nome, String cod,double x,double y , double r,double taxa,int cap) {
        super(nome,cod,x,y,r);
        this.taxa_km=taxa;
        this.capacidade=cap;
        this.encomendas_ativas=new HashSet<Encomenda>();
    }
}
