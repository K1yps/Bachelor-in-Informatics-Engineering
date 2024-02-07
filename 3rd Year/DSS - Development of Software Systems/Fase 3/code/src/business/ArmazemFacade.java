package business;

import business.subRobos.RoboInvalidoException;
import business.subRobos.SubRoboFacade;
import business.subStaff.GestorInvalidoException;
import business.subStaff.SubStaffFacade;
import business.subStock.Exceptions.*;
import business.subStock.SubStockFacade;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


public class ArmazemFacade implements ArmazemI {
    private final subStockI Stock = new SubStockFacade();
    private final subStaffI Staff = new SubStaffFacade();
    private final subRoboI Robos = new SubRoboFacade();

    public ArmazemFacade() {
        try {
            this.adicionarZona("Cima", 20);
            this.adicionarZona("Baixo", 20);
            this.adicionarAresta("ZonaDescarga", "Cima", 2);
            this.adicionarAresta("ZonaDescarga", "Baixo", 7);
            this.adicionarAresta("Cima", "Baixo", 5);
            this.adicionarPrateleira("Cima", 11, 4.0F, 1);
            this.adicionarPrateleira("Cima", 12, 8.0F, 1);
            this.adicionarPrateleira("Cima", 13, 12.0F, 1);
            this.adicionarPrateleira("Cima", 14, 16.0F, 1);
            this.adicionarPrateleira("Cima", 15, 20.0F, 1);
            this.adicionarPrateleira("Baixo", 16, 4.0F, 1);
            this.adicionarPrateleira("Baixo", 17, 8.0F, 1);
            this.adicionarPrateleira("Baixo", 18, 12.0F, 1);
            this.adicionarPrateleira("Baixo", 19, 16.0F, 1);
            this.adicionarPrateleira("Baixo", 10, 20.0F, 1);
            this.addRobo("Rumba", "Aspirador", 0);
        } catch (Exception ignored) {
        }

    }

    public void adicionarAresta(String zonaOrigem, String zonaDestino, float custo) throws ZonaInvalidaException {
        Stock.adicionarAresta(zonaOrigem, zonaDestino, custo);
    }

    public void addRobo(String codRobo, String modelo, int ultimaLocalizacao) {
        this.Robos.addRobo(codRobo, modelo, ultimaLocalizacao);
        this.clearZonaDeDescarga();
    }

    public void adicionarPrateleira(String zona, int codigo, Float custo, int capacidade) throws PrateleiraJaExisteException, ZonaInvalidaException {
        Stock.adicionarPrateleira(zona, codigo, custo, capacidade);
        this.clearZonaDeDescarga();
    }

    public void adicionarZona(String codigoZona, float custoAcesso) throws ZonaJaExisteException {
        Stock.adicionarZona(codigoZona, custoAcesso);
    }


    @Override
    public void registarPalete(String codigo) {
        String[] res = codigo.split("-");

        int materia = 0;
        try {
            materia = Integer.parseInt(res[1]);
        } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
        }
        Stock.registarPalete(res[0], materia);
        this.clearZonaDeDescarga();
    }

    public void notificacaoCarga(String codigoRobo) throws RoboInvalidoException {
        String codPalete = Robos.notificacaoCarga(codigoRobo); //dá update ao estado do robo
        try {
            Stock.retirarPalete(codPalete, codigoRobo);
        } catch (EstadoPaleteInvalidoException ignored) {
        }
        this.clearZonaDeDescarga();

    }

    public void notificacaoDescarga(String codigoRobo)
            throws RoboInvalidoException, PrateleiraSemEspacoException, PrateleiraInvalidaException, ZonaInvalidaException {
        Map.Entry<String, Map.Entry<Integer, Integer>> res = Robos.notificacaoDescarga(codigoRobo);
        String zona = Stock.getCodigoZona(res.getValue().getKey());

        Stock.armazenarPalete(res.getKey(), zona, res.getValue().getValue());
        this.clearZonaDeDescarga();
    }

    public void registarConsulta(String codGestor) throws GestorInvalidoException {
        Staff.registarConsulta(codGestor);
    }

    //Fara os transportes das paletes, a ser envocado cada vez que haja uma atualizaçao nos robos/paletes
    //ou seja metodo notificaçao carga/descarga e registo de paletes/robos
    //pq paletes podem estar a espera de robos ou prateleiras
    public void clearZonaDeDescarga() {

        String targetPalete = Stock.getPaleteFromDropOff();
        if (targetPalete == null)
            return;                                         //Se não existir paletes a transportar sai

        Map<Integer, Set<String>> robos = Robos.mapaRoboDisponiveis();
        if (robos.size() < 1) return;                                             //Se não existir robos disponiveis sai

        Map.Entry<String, Integer> zonaPrateleira = Stock.findPrateleiraLivre();
        if (zonaPrateleira == null)
            return;                                       //Se não existir prateleiras disponiveis sai

        PathInfo caminho;
        try {
            caminho = Stock.pathThroughZonaDescarga(robos.keySet(), zonaPrateleira.getKey());
        } catch (ZonaInvalidaException e) {
            return;
        }
        String robo;
        try {
            robo = robos.get(caminho.getStart()).iterator().next();
        } catch (NoSuchElementException e) {
            return;
        }
        enviarOrdemTransporte(robo, zonaPrateleira.getValue(), targetPalete, caminho.getLoadingzone(), caminho.getDropoffzone(), caminho.getPath());
    }

    public void enviarOrdemTransporte(String codRobo, int codprateleira, String codpalete, int vcarga, int vdescarga, List<Integer> percursoTotal) {
        try {
            Robos.enviaOrdem(codRobo, codprateleira, codpalete, vcarga, vdescarga, percursoTotal);
        } catch (RoboInvalidoException ignored) {
        }
    }

    public List<InfoPaletes> getStock() {
        return Stock.getInfoPaletes();
    }

    public void addMatPrima(int id, String nome) {
        Stock.addMatPrima(id, nome);
    }

    public boolean existeZona(String codZona) {
        return Stock.existeZona(codZona);
    }

    public boolean existeRobo(String codRobo) {
        return Robos.existeRobo(codRobo);
    }

    public boolean existePrateleira(int codPrateleira) {
        return Stock.existePrateleira(codPrateleira);
    }

}
