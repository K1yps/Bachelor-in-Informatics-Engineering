package data;

import business.subStock.Palete;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrateleiraHasPaleteDAO extends PaleteDAO implements Map<String, Palete> {
    public final Connection conn = DAOConfig.getConnection();
    private final Integer codPrat;

    public PrateleiraHasPaleteDAO(Integer codPrat) {
        super(null);
        DatabaseTables.mkTPrateleiraHasPalete();
        this.codPrat = codPrat;
    }

    private Palete getPalete(ResultSet rs) throws SQLException {
        String codigo = rs.getString("codigoPalete");
        String descricao = rs.getString("descricao");
        int materia = rs.getInt("MateriaID");
        return new Palete(materia, codigo, descricao, super.getEstado(codigo));
    }

    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM PrateleiraHasPalete" + (codPrat == null ? "" : " WHERE CodigoPrat =" + codPrat));
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
            ResultSet rs = stm.executeQuery("SELECT codigo FROM PrateleiraHasPalete WHERE  codigoPalete = '" + key.toString() + "'" + (codPrat == null ? "" : " AND CodigoPrat =" + codPrat));
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Palete u = (Palete) value;
        return this.containsKey(u.getCodigoPalete());
    }

    @Override
    public Palete get(Object key) {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Palete AS P INNER JOIN ( SELECT codigoPalete FROM PrateleiraHasPalete WHERE codigoPalete='" + key + "'" + (codPrat == null ? "" : "  AND CodigoPrat =" + codPrat) +
                    " ) AS PP ON P.codigoPalete = PP.codigoPalete");
            if (rs.next())
                return getPalete(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    @Override
    public Palete put(String key, Palete value) {
        Palete u = get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO PrateleiraHasPalete VALUES ('" + value.getCodigoPalete() + "', " + this.codPrat + ") " +
                            "ON DUPLICATE KEY UPDATE codigoPalete=VALUES(codigoPalete), codigoPrat=VALUES(codigoPrat)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return u;
    }

    @Override
    public Palete remove(Object key) {
        Palete t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM PrateleiraHasPalete WHERE codigoPalete='" + key + "'" + (codPrat == null ? "" : " AND CodigoPrat =" + codPrat));
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Palete> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            if (codPrat == null)
                stm.executeUpdate("TRUNCATE PrateleiraHasPalete");
            else stm.executeUpdate("DELETE FROM PrateleiraHasPalete WHERE CodigoPrat =" + codPrat);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<String> keySet() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM PrateleiraHasPalete" + (codPrat == null ? "" : " AND CodigoPrat =" + codPrat));
            Set<String> res = new HashSet<>();
            while (rs.next()) {
                res.add(rs.getString("codigoPalete"));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Collection<Palete> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM PrateleiraHasPalete" + (codPrat == null ? "" : " WHERE CodigoPrat =" + codPrat));
            Collection<Palete> col = new HashSet<>();
            while (rs.next()) {
                col.add(this.get(rs.getString("codigoPalete")));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<String, Palete>> entrySet() {
        try {
            String aux;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM PrateleiraHasPalete" + (codPrat == null ? "" : " WHERE CodigoPrat =" + codPrat));
            Set<Entry<String, Palete>> col = new HashSet<>();
            while (rs.next()) {
                aux = rs.getString("codigoPalete");
                col.add(new AbstractMap.SimpleEntry<>(aux, this.get(aux)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

}
