
/**
 * Escreva a descrição da classe Agente aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */

import java.util.Set;
import java.util.HashSet;
public class Agente
{
    private double coordenadaX;
    private double coordenadaY;
    private double raio_acao;
    Set<Encomenda> registo_encomendas;  ///registo das encomendas já entregues
    private String codigo,nome;
    
    public double getX() {return this.coordenadaX;}
    
    public double getY() {return this.coordenadaY;}
    
    public double getR() {return this.raio_acao;}
    
    public String getCodigo(){return this.codigo;}
    
    public Agente(String nome,String cod,double x,double y,double r) {
        this.coordenadaX=x;
        this.coordenadaY=y;
        this.nome=nome;
        this.codigo=cod;
        this.raio_acao=r;
        this.registo_encomendas=new HashSet<Encomenda>();
        
    }
}
