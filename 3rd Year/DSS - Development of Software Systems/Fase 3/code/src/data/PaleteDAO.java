package data;

import business.subStock.EmArmazenamento;
import business.subStock.EmTransporte;
import business.subStock.EstadoPalete;
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

public class PaleteDAO implements Map<String, Palete> {
    public final Connection conn = DAOConfig.getConnection();
    Integer codigoMat;

    public PaleteDAO(Integer codigoMat) {
        DatabaseTables.mkTPalete();
        this.codigoMat = codigoMat;
    }

    public EstadoPalete getEstado(String key) {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM EstadoPalete WHERE codigoPalete='" + key + "'");
            if (rs.next()) {  // A chave existe na tabela
                String zona = rs.getString("zona");
                String robo = rs.getString("robo");
                int prateleira = rs.getInt("prateleira");

                if (robo != null) return new EmTransporte(robo);
                if (zona != null) return new EmArmazenamento(zona, prateleira);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }


    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Palete " + (codigoMat == null ? "" : " WHERE MateriaID=" + codigoMat));
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
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM Palete WHERE codigoPalete='" + key.toString() + "'" + (codigoMat == null ? "" : " AND MateriaID=" + codigoMat));
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
            ResultSet rs = stm.executeQuery("SELECT * FROM Palete WHERE codigoPalete='" + key + "'" + (codigoMat == null ? "" : " AND MateriaID=" + codigoMat));
            if (rs.next()) {  // A chave existe na tabela
                String codigo = rs.getString("codigoPalete");
                String descricao = rs.getString("descricao");
                return new Palete((codigoMat==null? 0:codigoMat), codigo, descricao, this.getEstado(codigo));
            }
        } catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }


    @Override
    public Palete put(String key, Palete value) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO Palete VALUES ('" + value.getCodigoPalete() + "', '" + value.getDescricao() + "', '" + codigoMat + "') " +
                            "ON DUPLICATE KEY UPDATE codigoPalete=VALUES(codigoPalete), Descricao=VALUES(Descricao), MateriaID = VALUES(MateriaID)");

            String sqlquerypiece = "NULL ,NULL ,NULL ";
            if (value.getEstado() instanceof EmArmazenamento) {
                EmArmazenamento res = (EmArmazenamento) value.getEstado();
                sqlquerypiece = "'" + res.getZona() + "' ,NULL ," + res.getPrateleira();
            }
            if (value.getEstado() instanceof EmTransporte)
                sqlquerypiece = "NULL ,'" + ((EmTransporte) value.getEstado()).getRobo() + "' ,NULL ";

            stm.executeUpdate(
                    "INSERT INTO EstadoPalete VALUES ('" + value.getCodigoPalete() + "'," + sqlquerypiece + ") " +
                            "ON DUPLICATE KEY UPDATE codigoPalete=VALUES(codigoPalete), zona=VALUES(zona), robo = VALUES(robo), prateleira = VALUES(prateleira)");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    @Override
    public Palete remove(Object key) {
        Palete t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Palete WHERE codigoPalete='" + key + "'" + (codigoMat == null ? "" : " AND MateriaID=" + codigoMat));
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Palete> m) {
        for (Palete u : m.values()) {
            this.put(u.getCodigoPalete(), u);
        }
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            if (codigoMat != null) stm.executeUpdate("DELETE FROM Palete WHERE MateriaID=" + codigoMat);
            else stm.executeUpdate("TRUNCATE Palete");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<String> keySet() {

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM Palete" + (codigoMat == null ? "" : " AND MateriaID=" + codigoMat));
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
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM Palete" + (codigoMat == null ? "" : " WHERE MateriaID=" + codigoMat));
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
            ResultSet rs = stm.executeQuery("SELECT codigoPalete FROM Palete" + (codigoMat == null ? "" : " WHERE MateriaID=" + codigoMat));
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
