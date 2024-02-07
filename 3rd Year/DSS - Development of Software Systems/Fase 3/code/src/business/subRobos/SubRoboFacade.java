package business.subRobos;

import business.subRoboI;
import data.RoboDAO;

import java.util.*;


public class SubRoboFacade implements subRoboI {
    private final Map<String, Robo> robos = RoboDAO.getInstance();

    public void UpdateRobo(Robo b) {
        this.robos.put(b.getCodRobo(), b);
    }

    public String getPalete(String codigoRobo) throws RoboInvalidoException {
        try {
            return robos.get(codigoRobo).getPalete();
        } catch (NullPointerException e) {
            throw new RoboInvalidoException(e);
        }
    }

    public String notificacaoCarga(String codigoRobo) throws RoboInvalidoException {
        try {
            Robo r;
            String res = (r = robos.get(codigoRobo)).efetuarCarga();
            UpdateRobo(r);
            return res;
        } catch (NullPointerException e) {
            throw new RoboInvalidoException(e);
        }
    }

    public Map.Entry<String, Map.Entry<Integer, Integer>> notificacaoDescarga(String codigoRobo) throws RoboInvalidoException {
        try {
            Robo r;
            Map.Entry<String, Map.Entry<Integer, Integer>> res = (r = robos.get(codigoRobo)).efetuarDescarga();
            UpdateRobo(r);
            return res;
        } catch (NullPointerException e) {
            throw new RoboInvalidoException(e);
        }
    }

    public Boolean enviaOrdem(String codRobo, int codprateleira, String codpalete, int vorigem, int vdestino, List<Integer> percursoTotal) throws RoboInvalidoException {
        try {
            Robo r = robos.get(codRobo);
            r.setOrdem(new OrdemTransporte(codprateleira, codpalete, vorigem, vdestino, percursoTotal));
            UpdateRobo(r);
        } catch (NullPointerException e) {
            throw new RoboInvalidoException(e);
        }
        return true;
    }


    public void addRobo(String codRobo, String modelo, int ultimaLocalizacao) {
        robos.put(codRobo, new Robo(codRobo, modelo, ultimaLocalizacao));
    }

    @Override
    public Map<Integer, Set<String>> mapaRoboDisponiveis() {

        Map<Integer, Set<String>> res = new HashMap<>();
        this.robos.values().stream().filter(Robo::isDisponivel)
                .forEach(r -> {
                    try {
                        res.get(r.getUltimaLocalizacao()).add(r.getCodRobo());
                    } catch (NullPointerException e) {
                        res.put(r.getUltimaLocalizacao(), new LinkedHashSet<>(Collections.singleton(r.getCodRobo())));
                    }
                });
        return res;
    }

    public boolean existeRobo(String codRobo) {
        return this.robos.containsKey(codRobo);
    }
}
