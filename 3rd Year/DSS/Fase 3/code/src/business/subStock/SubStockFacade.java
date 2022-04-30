package business.subStock;

import business.InfoPaletes;
import business.PathInfo;
import business.subStock.Exceptions.*;
import business.subStockI;
import data.MateriaPrimaDAO;
import data.ZonaDAO;


import java.util.*;

public class SubStockFacade implements subStockI {
    private final Map<String, Zona> zonas = ZonaDAO.getInstance();
    private final Map<Integer, MateriaPrima> materiasPrimas = MateriaPrimaDAO.getInstance();
    private final Grafo mapa = new Grafo();
    public static final String codigoZonaDescarga = "ZonaDescarga";

    public SubStockFacade() {
        //Default
        List<Prateleira> prat = new Vector<>(2);
        prat.add(new Prateleira(0, 0.0F, Integer.MAX_VALUE, Integer.MAX_VALUE));
        prat.add(new Prateleira(1, 0.0F, Integer.MAX_VALUE, Integer.MAX_VALUE));
        addZona(codigoZonaDescarga, mapa.verticeDescarga(), prat);
        materiasPrimas.putIfAbsent(0, new MateriaPrima(0, "Frete Industrial"));
    }

    protected void UpdateZonas(Zona z) {
        zonas.put(z.getCodigo(), z);
    }

    public void adicionarZona(String codigoZona, float custoAcesso) throws ZonaJaExisteException {
        if (this.zonas.containsKey(codigoZona))
            throw new ZonaJaExisteException("A zona " + codigoZona + " já está registada.");
        addZona(codigoZona, this.mapa.genVertice(null, custoAcesso), null);
    }

    public boolean addZona(String codigoZona, Vertice v, Collection<Prateleira> prateleiras) {
        if (this.zonas.get(codigoZona) == null) {
            Zona res = new ZonaArmazenamento(codigoZona, v);
            v.setZona(res);
            zonas.put(res.getCodigo(), res);
            if (prateleiras != null)
                prateleiras.forEach(s -> res.addPrateleira(s.getCodigo(), s.getCustoAcesso(), s.getCapacidade()));
            this.zonas.putIfAbsent(codigoZona, res);
            v.setZona(res);
            return true;
        }
        return false;
    }

    public void adicionarAresta(String zonaOrigem, String zonaDestino, float custo) throws ZonaInvalidaException {
        try {
            mapa.adicionarAresta(this.getCodigoVertice(zonaOrigem), this.getCodigoVertice(zonaDestino), custo);
        } catch (VerticeInvalidoException e) {
            throw new ZonaInvalidaException(e);
        }
    }

    public String getCodigoZona(int numerovertice) {
        return mapa.getVertice(numerovertice).getZona().getCodigo();
    }

    @Override
    public List<InfoPaletes> getInfoPaletes() {
        List<InfoPaletes> res = new ArrayList<>();
        for (MateriaPrima m : materiasPrimas.values()) {
            for (Palete p : m.getPaletes()) {
                res.add(p.getInfo(m.getNome()));
            }
        }
        return res;
    }

    @Override
    public void registarPalete(String codPalete, int codMatPrima) {
        Palete p = new Palete(codMatPrima, codPalete, "N/A", new EmArmazenamento(codigoZonaDescarga, 0));
        try {
            materiasPrimas.get(codMatPrima).addPalete(p);
            zonas.get(codigoZonaDescarga).addPalete(0, p);
        } catch (NullPointerException e) {
            materiasPrimas.get(0).addPalete(p);
        } catch (PrateleiraSemEspacoException | PrateleiraInvalidaException ignored) {
        }
    }

    @Override
    public Palete getPalete(String codPalete) {
        Palete res;
        for (MateriaPrima p : materiasPrimas.values())
            if ((res = p.getPalete(codPalete)) != null)
                return res;
        return null;
    }


    // Pode ser evoluido fazer uma procura pelas zonas + proximas e nao pelo codigo
    @Override
    public Map.Entry<String, Integer> findPrateleiraLivre() {
        for (Zona a : zonas.values()) {
            if (!a.getCodigo().equals(codigoZonaDescarga)) {
                Prateleira pres;
                if ((pres = a.reservarPrateleira()) != null)
                    return new AbstractMap.SimpleEntry<>(a.getCodigo(), pres.getCodigo()); //O estado da zona nao muda (so a prateleira)
            }
        }
        return null;
    }

    @Override
    public void armazenarPalete(String codigoPalete, String codigoZona, int codigoPrateleira)
            throws PrateleiraInvalidaException, PrateleiraSemEspacoException, ZonaInvalidaException {
        Palete c = this.getPalete(codigoPalete);
        c.setEstado(new EmArmazenamento(codigoZona, codigoPrateleira));
        try {
            this.zonas.get(codigoZona).addPalete(codigoPrateleira, c);
        } catch (NullPointerException exc) {
            throw new ZonaInvalidaException(exc);
        }
        UpdatePalete(c);
    }

    @Override
    public void retirarPalete(String codigoPalete, String robo) throws EstadoPaleteInvalidoException {
        Palete pres = this.getPalete(codigoPalete);
        EmArmazenamento est;
        try {
            est = (EmArmazenamento) pres.getEstado();
            this.zonas.get(est.getZona()).removePalete(est.getPrateleira(), pres.getCodigoPalete()); //remover a palete da zona e prateleira
            pres.setEstado(new EmTransporte(robo));
            UpdatePalete(pres);

        } catch (ClassCastException | NullPointerException | PrateleiraInvalidaException e) {
            e.printStackTrace();
            throw new EstadoPaleteInvalidoException(e);

        }



    }

    @Override
    public String getPaleteFromDropOff() {
        Palete res;
        try {
            res = this.zonas.get(codigoZonaDescarga).getPrateleira(0).getPaletes().iterator().next();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (res != null) {
            try {
                this.retirarPalete(res.getCodigoPalete(), null);
                this.armazenarPalete(res.getCodigoPalete(), codigoZonaDescarga, 1);
            } catch (ZonaInvalidaException | PrateleiraInvalidaException | PrateleiraSemEspacoException | EstadoPaleteInvalidoException ignored) {
            }
            return res.getCodigoPalete();
        }
        return null;
    }

    @Override
    public int getVerticeZonaDescarga() {
        return Grafo.numeroVerticeDescarga;
    }

    public int getCodigoVertice(String codigoZona) throws ZonaInvalidaException {
        try {
            return this.zonas.get(codigoZona).getVertice().getNumero();
        } catch (NullPointerException e) {
            throw new ZonaInvalidaException(e);
        }
    }

    public PathInfo pathThroughZonaDescarga(Set<Integer> robosPos, String codigoZona) throws ZonaInvalidaException {
        int verticeZona = getCodigoVertice(codigoZona);
        Map.Entry<Integer, Map.Entry<List<Integer>, Float>> res =
                this.mapa.bestTarget(Grafo.numeroVerticeDescarga, verticeZona, robosPos);
        return new PathInfo(res.getKey(), Grafo.numeroVerticeDescarga, verticeZona, res.getValue().getValue(), res.getValue().getKey());
    }

    protected void UpdatePalete(Palete p) {
        this.materiasPrimas.get(p.getMateria()).addPalete(p);
    }

    public void adicionarPrateleira(String zona, int codigo, Float custo, int capacidade) throws PrateleiraJaExisteException, ZonaInvalidaException {
        try {
            this.zonas.get(zona).addPrateleira(codigo, custo, capacidade);
        } catch (NullPointerException e) {
            throw new ZonaInvalidaException(e);
        }
    }

    public void addMatPrima(int id, String nome) {
        MateriaPrima matprim = new MateriaPrima(id, nome);
        this.materiasPrimas.put(id, matprim);
    }

    public boolean existeZona(String codZona) {
        return zonas.containsKey(codZona);
    }

    public boolean existePrateleira(int codPrateleira) {
        for (Zona x : this.zonas.values()) {
            for (int cod : x.getPrateleiras().keySet()) {
                if (cod == codPrateleira) return true;
            }
        }
        return false;
    }
}
