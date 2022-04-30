package business.subRobos;

import java.util.ArrayList;
import java.util.List;

public class OrdemTransporte {
    private final int codPrateleiraDestino;
    private final String palete;
    private final int origem, destino;
    private final List<Integer> percursototal;

    public OrdemTransporte(int codPrateleiraDestino, String palete, int origem, int destino, List<Integer> percursoTotal) {
        this.codPrateleiraDestino = codPrateleiraDestino;
        this.palete = palete;
        this.origem = origem;
        this.destino = destino;
        this.percursototal = new ArrayList<>(percursoTotal);
    }

    public OrdemTransporte(OrdemTransporte input) {
        this.codPrateleiraDestino = input.codPrateleiraDestino;
        this.palete = input.palete;
        this.origem = input.origem;
        this.destino = input.destino;
        this.percursototal = new ArrayList<>(input.percursototal);
    }


    public int getCodPrateleiraDestino() {
        return codPrateleiraDestino;
    }

    public int getDestino() {
        return destino;
    }

    public int getOrigem() {
        return origem;
    }

    public String getPalete() {
        return palete;
    }

    public OrdemTransporte clone() {
        return new OrdemTransporte(this);
    }

    public List<Integer> getPercursototal() {
        return new ArrayList<>(percursototal);
    }

}
