package business;

import business.subStock.Exceptions.*;
import business.subStock.Palete;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface subStockI {
    void adicionarZona(String codigoZona, float custoAcesso) throws ZonaJaExisteException;

    void registarPalete(String codPalete, int codMatPrima);

    String getCodigoZona(int numerovertice);

    Palete getPalete(String palete);

    Map.Entry<String, Integer> findPrateleiraLivre();

    void armazenarPalete(String codigoPalete, String codigoZona, int codigoPrateleira)
            throws PrateleiraInvalidaException, PrateleiraSemEspacoException, ZonaInvalidaException;

    void retirarPalete(String codigoPalete, String robo) throws EstadoPaleteInvalidoException;

    String getPaleteFromDropOff();

    int getVerticeZonaDescarga();

    PathInfo pathThroughZonaDescarga(Set<Integer> robosPos, String codigoZona)
            throws ZonaInvalidaException;

    List<InfoPaletes> getInfoPaletes();

    void adicionarPrateleira(String zona, int codigo, Float custo, int capacidade) throws PrateleiraJaExisteException, ZonaInvalidaException;

    void addMatPrima(int id, String nome);

    void adicionarAresta(String zonaOrigem, String zonaDestino, float custo) throws ZonaInvalidaException;

    boolean existeZona(String codZona);

    boolean existePrateleira(int codPrateleira);
}

