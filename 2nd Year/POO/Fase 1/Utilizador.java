
/**
 * Write a description of class Utilizadores here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Utilizador {
    private String email,passwd,cod,nome;
    private double coordenadaX,coordenadaY;
    
    public Utilizador(String email,String password,String codigo,String Nome,double x,double y) {
        this.email=email;
        this.passwd=password;
        this.cod=codigo;
        this.nome=Nome;
        this.coordenadaX=x;
        this.coordenadaY=y;
    }

    public String getEmail() {return this.email;}
    
    public String getPassword() {return this.passwd;}
    
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }
    
    public void SetEmail(String n) {this.email=n;}
    
    public void SetPassword(String n) {this.passwd=n;}
    
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

}
