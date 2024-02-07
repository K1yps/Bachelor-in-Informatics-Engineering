package business;

import business.subRobos.RoboInvalidoException;
import business.subStaff.GestorInvalidoException;
import business.subStock.Exceptions.*;

import java.util.List;

public interface ArmazemI {

    void adicionarZona(String codigoZona, float custoAcesso) throws ZonaJaExisteException;

    public void addRobo(String codRobo, String modelo, int ultimaLocalizacao);

    void adicionarAresta(String zonaOrigem, String zonaDestino, float custo) throws ZonaInvalidaException;

    void notificacaoCarga(String codigoRobo) throws RoboInvalidoException;

    void notificacaoDescarga(String codigoRobo) throws RoboInvalidoException, PrateleiraSemEspacoException, PrateleiraInvalidaException, ZonaInvalidaException;

    List<InfoPaletes> getStock();

    void registarPalete(String codigo);

    void adicionarPrateleira(String zona, int codigo, Float custo, int capacidade) throws PrateleiraJaExisteException, ZonaInvalidaException;

    void addMatPrima(int id, String nome);

    boolean existeZona(String codZona);

    boolean existeRobo(String codRobo);

    boolean existePrateleira(int codPrateleira);
}