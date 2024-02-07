package data;

import business.subStock.Prateleira;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrateleiraDAO implements Map<Integer, Prateleira> {
    public final Connection conn = DAOConfig.getConnection();
    public String zona_chave;

    public PrateleiraDAO(String zona_chave) {
        DatabaseTables.mkTPrateleira();
        this.zona_chave = zona_chave;
    }

    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Prateleira" + (zona_chave == null ? "" : " WHERE zona_codigo = '" + zona_chave + "'"));
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Prateleira WHERE Codigo=" + key + (zona_chave == null ? "" : " AND zona_codigo = '" + zona_chave + "'"));
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Prateleira u = (Prateleira) value;
        return this.containsKey(u.getCodigo());
    }

    @Override
    public Prateleira get(Object key) {
        Prateleira t = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Prateleira WHERE Codigo=" + key + (zona_chave == null ? "" : " AND zona_codigo = '" + zona_chave + "'"));
            if (rs.next()) {  // A chave existe na tabela

                int codigo = rs.getInt("Codigo");
                int capacidade = rs.getInt("capacidade");
                int disponibilidade = rs.getInt("Disponibilidade");
                float custo = rs.getFloat("CustoAcesso");

                t = new Prateleira(codigo, custo, disponibilidade, capacidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    //Codigo varchar(45) NOT NULL PRIMARY KEY," +
    //                            "CustoAcesso float NOT NULL," +
    //                            "Disponibilidade int(4) NOT NULL," +
    //                            "capacidade int(4)  NOT NULL," +
    //                            "zona_codigo varchar(45) NULL," +

    @Override
    public Prateleira put(Integer key, Prateleira value) {
        Prateleira u = null;
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO Prateleira VALUES (" + value.getCodigo() + ", " + value.getCustoAcesso() + ", " + value.getDisponibilidade() +
                            ", " + value.getCapacidade() + (zona_chave == null ? ",NULL " : ", '" + zona_chave + "'") + ") " +
                            "ON DUPLICATE KEY UPDATE Codigo=VALUES(Codigo), CustoAcesso=VALUES(CustoAcesso), Disponibilidade= VALUES(Disponibilidade) ,capacidade=VALUES(capacidade), zona_codigo=VALUES(zona_codigo)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return u;
    }

    @Override
    public Prateleira remove(Object key) {
        Prateleira t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Prateleira WHERE Codigo=" + key + (zona_chave == null ? "" : " AND zona_codigo = '" + zona_chave + "'"));
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Prateleira> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            if (zona_chave == null)
                stm.executeUpdate("TRUNCATE Prateleira");
            else stm.executeUpdate("DELETE FROM Prateleira WHERE zona_codigo = '" + zona_chave + "'");

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Integer> keySet() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Prateleira" + (zona_chave == null ? "" : " WHERE zona_codigo = '" + zona_chave + "'"));
            Set<Integer> res = new HashSet<>();
            while (rs.next()) {
                res.add(rs.getInt("Codigo"));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Collection<Prateleira> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Prateleira" + (zona_chave == null ? "" : " WHERE zona_codigo = '" + zona_chave + "'"));
            Collection<Prateleira> col = new HashSet<>();
            while (rs.next()) {
                col.add(this.get(rs.getInt("Codigo")));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<Integer, Prateleira>> entrySet() {
        try {
            int aux;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Prateleira" + (zona_chave == null ? "" : " WHERE zona_codigo = '" + zona_chave + "'"));
            Set<Entry<Integer, Prateleira>> col = new HashSet<>();
            while (rs.next()) {
                aux = rs.getInt("Codigo");
                col.add(new AbstractMap.SimpleEntry<>(aux, this.get(aux)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
}
