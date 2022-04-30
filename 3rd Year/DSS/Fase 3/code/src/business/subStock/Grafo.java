package business.subStock;

import business.subStock.Exceptions.VerticeInvalidoException;
import data.GrafoDAO;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;


public class Grafo {
    private final Map<Vertice, List<Aresta>> grafo = GrafoDAO.getInstance();
    public static final int numeroVerticeDescarga = 0;

    public Grafo() {
        this.grafo.putIfAbsent(new Vertice(numeroVerticeDescarga, null, 0), new ArrayList<>());
    }

    public void addToList(Vertice v, Aresta adition) {
        List<Aresta> n = grafo.get(v);
        n.add(adition);
        grafo.put(v, n);
    }

    protected Vertice verticeDescarga() {
        Vertice r;
        if ((r = this.getVertice(numeroVerticeDescarga)) == null)
            r = new Vertice(numeroVerticeDescarga, null, 0);
        return r;
    }

    public Vertice genVertice(Zona z, Float custo) {
        Vertice res;
        grafo.put((res = new Vertice(grafo.size(), z, custo)), new ArrayList<>());
        return res;
    }

    public void addAresta(Aresta a) throws VerticeInvalidoException {
        if (a.getOrigem() == null || !this.grafo.containsKey(a.getOrigem()))
            throw new VerticeInvalidoException("" + a.getOrigem().getNumero());
        if (a.getDestino() == null || !this.grafo.containsKey(a.getDestino()))
            throw new VerticeInvalidoException("" + a.getDestino().getNumero());

        addToList(a.getOrigem(), a);
        addToList(a.getDestino(), a.invert());
    }

    public void adicionarAresta(int codVerticeOrigem, int codVerticeDestino, float custo) throws VerticeInvalidoException {
        Vertice origem = this.getVertice(codVerticeOrigem);
        Vertice destino = this.getVertice(codVerticeDestino);
        addAresta(new Aresta(custo, origem, destino));
    }

    private List<Integer> computePath(Map<Vertice, Entry<Float, Vertice>> PTree, Vertice choosenOne) {
        List<Integer> caminho = new ArrayList<>();

        Vertice v = choosenOne;
        while (v != null) {
            caminho.add(v.getNumero());
            v = PTree.get(v).getValue();
        }

        return caminho;
    }

    public Entry<Integer, Entry<List<Integer>, Float>> bestTarget(int origem, int destino, Set<Integer> target_locations) {
        Vertice ori = this.getVertice(origem);
        Map<Vertice, Entry<Float, Vertice>> PTree = arvoreCaminhoMaisCurto(ori);
        Vertice choosenOne = grafo.keySet()
                .stream()
                .filter(s -> target_locations.contains(s.getNumero()))
                .min(Comparator.comparingDouble(s -> PTree.get(s).getKey())).orElse(null);
        if (choosenOne == null) return null;

        Vertice dest = this.getVertice(destino);

        List<Integer> pt1 = computePath(PTree, choosenOne);
        List<Integer> pt2;
        Collections.reverse(pt2 = computePath(PTree, dest));
        pt2.remove(0);
        pt1.addAll(pt2);
        return new SimpleEntry<>(choosenOne.getNumero(), new SimpleEntry<>(pt1, PTree.get(choosenOne).getKey() + PTree.get(dest).getKey()));
    }

    //é private pq devolve parte do estado interno, se quiserem por public metam um clone no return
    public Vertice getVertice(int numerovertice) {
        for (Vertice v : grafo.keySet()) {
            if (v.getNumero() == numerovertice) return v;
        }
        return null;
    }


    /**
     * Calcula a arvore de pais correspondente à arvore de caminho mais curto do vertice dado
     *
     * @param source vertice origem
     * @return Mapa correspondente a arvore de pais
     */
    public Map<Vertice, Entry<Float, Vertice>> arvoreCaminhoMaisCurto(Vertice source) {

        // (Origem,(Custo,Destino))
        Map<Vertice, Entry<Float, Vertice>> nodosAprocessar = new HashMap<>();
        Map<Vertice, Entry<Float, Vertice>> pais = new HashMap<>();

        nodosAprocessar.put(source, new SimpleEntry<>(0.0F, null));

        while (nodosAprocessar.size() > 0) {
            //Calcular vertice por defenir com o menor custo de acesso
            Entry<Vertice, Entry<Float, Vertice>> currentNode = nodosAprocessar
                    .entrySet()
                    .stream()
                    .min(Comparator.comparingDouble(a -> a.getValue().getKey())).orElse(null);

            //Marcar o pai do vertice como definitivo
            Entry<Float, Vertice> now = nodosAprocessar.remove(currentNode.getKey());
            pais.put(currentNode.getKey(), now);
            float fowardCost = now.getKey() + (now.getValue() == null ? 0.0F : now.getValue().getCusto()); //
            //O custo comum para proseguir para os vertices vizinhos por este vertice
            // é o custo de chegar a este vertice mais o custo de o atravessar

            //Verificar os vertices adjacentes
            for (Aresta ar : grafo.get(currentNode.getKey())) {
                if (!pais.containsKey(ar.getDestino())) {                //Se o vertice destino da aresta não tem vertice pai permanente
                    float outCome = ar.getCusto() + fowardCost;          //Calcula o custo de chegar a esse vertice por esta aresta
                    boolean teste = true;
                    try {
                        teste = outCome < nodosAprocessar.get(ar.getDestino()).getKey();//Testa se este vertice pai é melhor que o que existe atualmente
                    } catch (NullPointerException ignored) {
                    }
                    if (teste)                                                                             //Se o teste for positivo
                        nodosAprocessar.put(ar.getDestino(), new SimpleEntry<>(outCome, currentNode.getKey())); //substitui/adicina o pai
                }
            }

        }
        return pais;
    }
}
