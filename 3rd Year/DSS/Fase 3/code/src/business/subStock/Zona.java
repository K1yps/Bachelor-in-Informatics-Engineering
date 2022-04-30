package business.subStock;

import business.subStock.Exceptions.PrateleiraInvalidaException;
import business.subStock.Exceptions.PrateleiraSemEspacoException;
import data.PrateleiraDAO;

import java.util.Collection;
import java.util.Map;

public abstract class Zona {
    private String codigo;
    private Vertice vertice;
    private final Map<Integer, Prateleira> prateleiras;

    public void UpdatePrateleira(Prateleira p) {
        prateleiras.put(p.getCodigo(), p);
    }

    public Zona(String codigo, Vertice vertice) {
        this.codigo = codigo;
        this.vertice = vertice;
        this.prateleiras = new PrateleiraDAO(codigo);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Vertice getVertice() {
        return vertice;
    }

    public void setVertice(Vertice vertice) {
        this.vertice = vertice;
    }

    public int getNumPrateleiras() {
        return this.prateleiras.size();
    }

    public Map<Integer, Prateleira> getPrateleiras() {
        return prateleiras;
    }

    public Prateleira reservarPrateleira() {
        for (Prateleira p : prateleiras.values()) {
            if (p.reservarEspaco()) {
                UpdatePrateleira(p);
                return p;
            }
        }
        return null;
    }

    public void addPalete(int codigoPrateleira, Palete c) throws PrateleiraInvalidaException, PrateleiraSemEspacoException {
        try {
            Prateleira res;
            (res = this.prateleiras.get(codigoPrateleira)).addPalete(c);
            this.UpdatePrateleira(res);
        } catch (NullPointerException e) {
            throw new PrateleiraInvalidaException();
        }
    }

    public void removePalete(int prateleira, String codigoPalete) throws PrateleiraInvalidaException {
        try {
            Prateleira res;
            (res = this.prateleiras.get(prateleira)).removePalete(codigoPalete);
            this.UpdatePrateleira(res);
        } catch (NullPointerException e) {
            throw new PrateleiraInvalidaException(e);
        }
    }

    public Collection<Palete> getPaletes() {
        return this.prateleiras.values()
                .stream()
                .map(Prateleira::getPaletes)
                .reduce(null, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    public void addPrateleira(int codigo, float custo, int capacidade) {
        this.prateleiras.put(codigo, new Prateleira(codigo, custo, capacidade, capacidade));
    }

    public Prateleira getPrateleira(int codigoPrateleira) {
        return this.prateleiras.get(codigoPrateleira);
    }
}
