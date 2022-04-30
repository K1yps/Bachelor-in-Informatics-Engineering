package business.subRobos;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public class Robo {
    private String codRobo, modelo;
    private int ultimaLocalizacao;
    private Boolean vazio;
    private OrdemTransporte ordem = null;
    //private List<OrdemTransporte> registosErroOrdem = (


    public Robo(String codRobo, String modelo, int ultimaLocalizacao) {
        this.codRobo = codRobo;
        this.modelo = modelo;
        this.vazio = true;
        this.ultimaLocalizacao = ultimaLocalizacao;
    }

    public Robo(String codRobo, String modelo, Boolean vazio, int ultimaLocalizacao, OrdemTransporte o) {
        this.codRobo = codRobo;
        this.modelo = modelo;
        this.vazio = vazio;
        this.ultimaLocalizacao = ultimaLocalizacao;
        this.ordem = o;
    }

    public Robo(Robo input) {
        this.codRobo = input.codRobo;
        this.modelo = input.modelo;
        this.vazio = input.vazio;
        this.ultimaLocalizacao = input.ultimaLocalizacao;
        this.ordem = input.ordem.clone();
        //this.registosErroOrdem = input.registosErroOrdem;//msm DAO i think
    }

    public Boolean getVazio() {
        return vazio;
    }

    public String getCodRobo() {
        return codRobo;
    }

    public String getModelo() {
        return modelo;
    }

    public OrdemTransporte getOrdem() {
        return ordem;
    }

    public Integer getCodigoLocalizacao() {
        return this.ultimaLocalizacao;
    }

    public void setVazio(Boolean vazio) {
        this.vazio = vazio;
    }

    public void setCodRobo(String codRobo) {
        this.codRobo = codRobo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setOrdem(OrdemTransporte ordem) {
        this.ordem = ordem;
    }

    public int getUltimaLocalizacao() {
        return ultimaLocalizacao;
    }

    public void setUltimaLocalizacao(int ultimaLocalizacao) {
        this.ultimaLocalizacao = ultimaLocalizacao;
    }

    public String efetuarCarga() {
        this.setVazio(false);
        this.setUltimaLocalizacao(this.ordem.getOrigem());
        return this.ordem.getPalete();
    }

    public Map.Entry<String, Map.Entry<Integer, Integer>> efetuarDescarga() {
        this.setVazio(true);
        this.setUltimaLocalizacao(this.ordem.getDestino());
        OrdemTransporte old = this.ordem;
        this.ordem = null;
        return new AbstractMap.SimpleEntry<>(old.getPalete(), new AbstractMap.SimpleEntry<>(old.getDestino(), old.getCodPrateleiraDestino()));
    }

    public void atualizaOrdem(OrdemTransporte o) {
        this.ordem = o;
    }

    /*
    public void registarErroOrdem() {
        registosErroOrdem.add(ordem);
        ordem = null;
    }*/

    public String getPalete() {
        if (vazio) return null;
        return ordem.getPalete();
    }

    /*
    public List<OrdemTransporte> getRegistosErroOrdem() {
        return new ArrayList<>(registosErroOrdem);
    }*/

    public boolean isDisponivel() {
        return this.ordem == null;
    }

    public Robo clone() {
        return new Robo(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robo robo = (Robo) o;
        return codRobo.equals(robo.codRobo) &&
                Objects.equals(modelo, robo.modelo) &&
                vazio.equals(robo.vazio) &&
                Objects.equals(ultimaLocalizacao, robo.ultimaLocalizacao) &&
                Objects.equals(ordem, robo.ordem);// &&
        //Objects.equals(registosErroOrdem, robo.registosErroOrdem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codRobo, modelo, vazio, ultimaLocalizacao, ordem);//, registosErroOrdem);
    }
}
