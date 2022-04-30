package business.subStock;

public class Aresta {
    private String id;
    private float custo;
    private final Vertice origem, destino;


    public Aresta(float custo, Vertice origem, Vertice destino) {
        this.id = genID(origem.getNumero(), destino.getNumero());
        this.custo = custo;
        this.origem = origem;
        this.destino = destino;
    }

    public Aresta(String id, float custo, Vertice origem, Vertice destino) {
        this.id = id;
        this.custo = custo;
        this.origem = origem;
        this.destino = destino;
    }

    private String genID(int origem, int destino) {
        return origem + "-" + destino;
    }

    public String getId() {
        return id;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public Vertice getOrigem() {
        return origem.clone();
    }

    public Vertice getDestino() {
        return destino.clone();
    }

    public Aresta invert() {
        return new Aresta(genID(destino.getNumero(), origem.getNumero()), custo, destino.clone(), origem.clone());
    }
}
