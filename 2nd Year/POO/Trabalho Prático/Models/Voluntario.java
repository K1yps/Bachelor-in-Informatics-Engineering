package Models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Escreva a descrição da classe Models.Voluntario aqui.
 *
 * @author (seu nome)
 * @version (número de versão ou data)
 */


public class Voluntario extends Agente implements Serializable {

    private Encomenda e; //e==null significa que está livre para entregar
    private StatsEntrega st;//st==null significa que a encomenta nao existe

    public Voluntario(String nome, String cod, double clas, int t, double x, double y, double r, boolean med, boolean d) {
        super(nome, cod, clas, t, x, y, r, med, d);
    }

    public boolean solicitar_transporte(Encomenda x, Utilizador u, Loja l) {
        if (!encomenda_in_range(u, l)) return false; //encomenda fora do raio de acao
        else {
            if (this.e == null && getDisponibilidade()) { //voluntario livre para fazer a encomenda
                e = x.clone();
                st = new StatsEntrega(LocalDateTime.now());
                return true;
            } else return false; //voluntario ocupado
        }
    }

    public boolean has_order_in_name(String user_code) {
        try {
            return this.e.getUser().equals(user_code);
        } catch (NullPointerException e){
            return false;
        }
    }

    public void remove_encomenda(String user_code, LocalDateTime hora_chegada) {
        st.setHora_saida(hora_chegada);
        this.add_encomenda_to_historico(e,st);
        this.e=null;
        this.st=null;
    }
}
