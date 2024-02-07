package business.subStock;


import business.InfoPaletes;

public class Palete {
    private String codigoPalete, descricao;
    private Boolean Disponibilidade = true;
    private EstadoPalete estado;
    private int materia;

    public Palete(int materia, String codigoPalete, String descricao, EstadoPalete estado) {
        this.codigoPalete = codigoPalete;
        this.descricao = descricao;
        this.estado = estado;
        this.materia = materia;
    }

    public int getMateria() {
        return materia;
    }

    public void setMateria(int materia) {
        this.materia = materia;
    }

    public String getCodigoPalete() {
        return codigoPalete;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCodigoPalete(String codigoPalete) {
        this.codigoPalete = codigoPalete;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getDisponibilidade() {
        return Disponibilidade;
    }

    public void Reservar() {
        Disponibilidade = false;
    }

    public EstadoPalete getEstado() {
        return estado;
    }

    public void setEstado(EstadoPalete estado) {
        this.estado = estado;
    }

    public InfoPaletes getInfo(String materia) {
        InfoPaletes res = new InfoPaletes();
        if (this.estado instanceof EmArmazenamento) {
            res.setCodPalete(this.codigoPalete);
            res.setDisponibilidade(this.Disponibilidade);
            res.setPrateleira(((EmArmazenamento) this.estado).getPrateleira());
            res.setZona(((EmArmazenamento) this.estado).getZona());
            res.setRobo("");
            res.setMateriaPrima(materia);
        } else {
            res.setCodPalete(this.codigoPalete);
            res.setDisponibilidade(this.Disponibilidade);
            res.setRobo(((EmTransporte) this.estado).getLocation());
            res.setZona("");
            res.setPrateleira(0);
            res.setMateriaPrima(materia);
        }
        return res;
    }


}