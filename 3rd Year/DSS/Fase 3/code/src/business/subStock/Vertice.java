package business.subStock;

import java.util.Objects;

/**
 * Os vertices representam zonas, que nesta fase final representaram os corredores
 */
public class Vertice {
    private int numero;
    private Zona zona;
    private float custo; //(neste caso comprimento)


    public Vertice(int numero, Zona zona, float custo) {
        this.numero = numero;
        this.zona = zona;
        this.custo = custo;
    }

    public Vertice(Vertice input) {
        this.numero = input.numero;
        this.zona = input.zona;
        this.custo = input.custo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public Vertice clone() {
        return new Vertice(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return numero == vertice.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    public String toString() {
        return "[" + numero + "]-" + custo;
    }

}
