import java.util.Map;
import java.util.HashMap;

/**
 * Escreva a descrição da classe Lojas aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Loja {
    private String cod,nome;
    private double coordenadaX,coordenadaY;
    private Map<String,Boolean> estado;

    public Loja(String cod,String n,double x,double y) {
        this.cod=cod;
        this.nome=n;
        this.coordenadaX=x;
        this.coordenadaY=y;
        this.estado=new HashMap<String,Boolean>();
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

    public double getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(double coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public double getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(double coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public void request(String codEnc){
        this.estado.put(codEnc,false);
    }

    public Boolean ready(String codEnc){
        return this.estado.replace(codEnc,true);
    }

    public Boolean deliver(String codEnc){
        return this.estado.remove((codEnc));
   }

}
