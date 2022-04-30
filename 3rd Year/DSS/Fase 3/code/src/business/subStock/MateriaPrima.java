package business.subStock;


import data.PaleteDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MateriaPrima {
    private final int id;
    private String nome;
    private final Map<String, Palete> materiaPrima;//sao as paletes o nome ficou assim na fase 2 though

    public MateriaPrima(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.materiaPrima = new PaleteDAO(id);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addPalete(Palete p) {
        materiaPrima.put(p.getCodigoPalete(), p);
    }

    public Palete getPalete(String codPalete) {
        return materiaPrima.get(codPalete);
    }

    public List<Palete> getPaletes() {
        return new ArrayList<>(materiaPrima.values());
    }

    public boolean contains(String codigoPalete) {
        return materiaPrima.containsKey(codigoPalete);
    }

}
