package business.subStock;

import business.subStock.Exceptions.PrateleiraSemEspacoException;
import data.PrateleiraHasPaleteDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Prateleira {
    private int codigo;
    private float custoAcesso;
    private int disponibilidade;
    private final int capacidade;
    private Map<String, Palete> paletes;

    public Prateleira(int codigo, float custoAcesso, int disponibilidade, int capacidade) {
        this.codigo = codigo;
        this.custoAcesso = custoAcesso;
        this.disponibilidade = disponibilidade;
        this.capacidade = capacidade;
        this.paletes = new PrateleiraHasPaleteDAO(codigo);
    }


    public int getCapacidade() {
        return capacidade;
    }

    public void setPaletes(Map<String, Palete> paletes) {
        this.paletes = paletes;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public float getCustoAcesso() {
        return custoAcesso;
    }

    public void setCustoAcesso(float custoAcesso) {
        this.custoAcesso = custoAcesso;
    }

    public boolean isAvailable() {
        return disponibilidade < capacidade;
    }

    public int getDisponibilidade() {
        return disponibilidade;
    }

    public boolean reservarEspaco() {
        boolean res = disponibilidade > 0;
        if (res) disponibilidade--;
        return res;
    }

    public Palete getPalete(String codPalete) {
        return paletes.get(codPalete);
    }

    public List<Palete> getPaletes() {
        return new ArrayList<>(paletes.values());
    }

    public void removePalete(String codPalete) {
        paletes.remove(codPalete);
        this.disponibilidade++;
    }


    public void addPalete(Palete c) throws PrateleiraSemEspacoException {
        if (!(disponibilidade > -1))
            throw new PrateleiraSemEspacoException("A prateleira " + codigo + " econtra-se cheia. Livre" + (disponibilidade + 1) + '/' + capacidade);
        this.paletes.put(c.getCodigoPalete(), c);
    }
}
