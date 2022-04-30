package business.subStaff;

import java.util.Map;

public class Gestor extends Utilizador {
    private String posto;
    private Map<Integer, Registo> registoConsultas = null;

    public Gestor(String username, String nome, String posto, String codAcesso) {
        super(username, nome, codAcesso);
        this.posto = posto;
    }

    public Gestor(Gestor input) {
        super(input.getUsername(), input.getNome(), input.getCodAcesso());
        this.posto = input.posto;
        this.registoConsultas = input.registoConsultas;
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public void registarConsulta() {
        registoConsultas.put(registoConsultas.size() + 1, new Registo());
    }

    @Override
    public Utilizador clone() {
        return new Gestor(this);
    }


}
