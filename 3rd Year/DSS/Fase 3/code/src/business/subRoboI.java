package business;

import business.subRobos.RoboInvalidoException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface subRoboI {
    //void registarErroOrdem(String codigoRobo) throws RoboInvalidoException;

    String getPalete(String codigoRobo) throws RoboInvalidoException;

    String notificacaoCarga(String codRobo) throws RoboInvalidoException;

    Map.Entry<String, Map.Entry<Integer, Integer>> notificacaoDescarga(String codRobo) throws RoboInvalidoException;

    Boolean enviaOrdem(String codRobo, int codprateleira, String codpalete, int vorigem, int vdestino, List<Integer> percursoTotal) throws RoboInvalidoException;

    Map<Integer, Set<String>> mapaRoboDisponiveis();

    void addRobo(String codRobo, String modelo, int Localizacao);

    boolean existeRobo(String codRobo);

}
