
/**
 * Sistema principal do progama TrazAqui!.
 * 
 * @authors (Benjamim Coelho,Henrique Neto,Sara Marques)
 * @version (0)
 */

import java.util.Map;
import java.util.Set;
public class Sistema
{
    private Set<Encomenda> pedidos_encomenda;///encomendas solicitadas pelos utilizadores que ainda nao foram aceites por um Agente
    private Map<String,Utilizador> users; /// lista dos utilizadores
    private Map<String,Agente> agentes; /// lista dos agentes
    private Map<String,Loja> lojas; /// lista das lojas

    private String generateKey(String prefix, Set<String> keys) {
        int i=0;
        while(keys.contains(prefix+i))
            i++;
        return prefix+i;
    }

    public void registarUtilizador(String cod,String email,String password,String Nome,double x,double y) {
        if (cod == null) cod=generateKey("u",this.users.keySet());
        Utilizador res=new Utilizador(email,password,cod,Nome,x,y);
        this.users.put(res.getCod(),res);
    }
    
    public void registarVoluntario(String cod,String nome, double x,double y , double r) {
        if (cod == null) cod=generateKey("v",this.users.keySet());
        Voluntario res=new Voluntario(nome,cod,x,y,r);
        this.agentes.put(res.getCodigo(),res);
    }
    
    public void registarEmpresa(String cod,String nome, double x,double y , double r,double taxa, int cap) {
        if (cod == null) cod=generateKey("t",this.users.keySet());
        EmpresaTransportadora res=new EmpresaTransportadora(nome,cod,x,y,r,taxa,cap);
        this.agentes.put(res.getCodigo(),res);
    }
    
    
    public void registarLoja(String cod,String n,double x,double y,Boolean Queue){
        if (cod == null) cod=generateKey("l",this.users.keySet());
        Loja res;
        if (Queue) res=new Loja_Queue(cod,n,x,y);
        else res= new Loja(cod,n,x,y);
        this.lojas.put(res.getCod(),res);
    }

    public boolean validarCredenciais(String email,String password){
        if (this.users.containsKey(email)) {
            Utilizador aux=this.users.get(email);
            return aux.getPassword().equals(password);
        }
        else return false;
    }

    public int quantosUtilizadores(){return this.users.size();}
    
    public void inserirPedidoEncomenda(Encomenda x) {
        this.pedidos_encomenda.add(x.clone());
    }
}
