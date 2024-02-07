package Models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Write a description of class Utilizadores here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Utilizador implements Serializable {
    private String email, passwd, cod, nome;
    private double coordenadaX, coordenadaY;
    private int nEnc;
    private boolean estado; //true se estiver com uma encomenda a caminho, falso caso contrário

    public Utilizador(String email, String password, String codigo, String Nome, double x, double y) {
        this.email = email;
        this.passwd = password;
        this.cod = codigo;
        this.nome = Nome;
        this.coordenadaX = x;
        this.coordenadaY = y;
        nEnc = 0;
        estado=false;
    }

    public Utilizador(Utilizador in) {
        this.email = in.email;
        this.passwd = in.passwd;
        this.cod = in.cod;
        this.nome = in.nome;
        this.coordenadaX = in.coordenadaX;
        this.coordenadaY = in.coordenadaY;
        this.nEnc = in.nEnc;
        estado=false;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean getEstado() {
        return estado;
    }


    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.passwd;
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

    public void SetEmail(String n) {
        this.email = n;
    }

    public void SetPassword(String n) {
        this.passwd = n;
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

    public int getnEnc() {
        return nEnc;
    }

    public void setnEnc(int nEnc) {
        this.nEnc = nEnc;
    }

    public void inc_Encomendas() {
        this.nEnc++;
    }

    public Utilizador clone() {
        return new Utilizador(this);
    }

    public void receive_Enc(){
        this.estado=false;
        this.nEnc++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizador)) return false;
        Utilizador that = (Utilizador) o;
        return Double.compare(that.coordenadaX, coordenadaX) == 0 &&
                Double.compare(that.coordenadaY, coordenadaY) == 0 &&
                nEnc == that.nEnc &&
                Objects.equals(email, that.email) &&
                Objects.equals(passwd, that.passwd) &&
                Objects.equals(cod, that.cod) &&
                Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, passwd, cod, nome, coordenadaX, coordenadaY, nEnc);
    }
}
