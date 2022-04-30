package business;

/**
 * Classe para transporte de informaçao de paletes entre a vista e armazemfacade e subsistema de stock
 */
public class InfoPaletes {
    /*public Map<String,Object> parametros = new LinkedHashMap<>();

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) { this.parametros = parametros; }
¨¨*/
    private String codPalete;
    private Boolean disponibilidade;
    private String robo;
    private String zona;
    private String materia_prima;
    private int prateleira;

    public String getCodPalete() {
        return codPalete;
    }

    public String getMateriaPrima() {
        return this.materia_prima;
    }

    public void setMateriaPrima(String materia_prima) {
        this.materia_prima = materia_prima;
    }

    public void setCodPalete(String codPalete) {
        this.codPalete = codPalete;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public int getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(int prateleira) {
        this.prateleira = prateleira;
    }

    public String getRobo() {
        return robo;
    }

    public void setRobo(String robo) {
        this.robo = robo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Código Palete: ").append(this.getCodPalete()).append("\n");
        sb.append("Matéria Prima: ").append(this.getMateriaPrima()).append("\n");
        sb.append("Disponibilidade: ").append(this.getDisponibilidade()).append("\n");
        if (!this.robo.equals("")) {
            sb.append("Robô: ").append(this.getRobo()).append("\n");
        }
        else {
            sb.append("Zona: ").append(this.getZona()).append("\n");
            sb.append("Prateleira: ").append(this.getPrateleira()).append("\n\n");
        }

        return sb.toString();
    }

}