package data;


import business.subStock.Prateleira;
import business.subStock.Vertice;
import business.subStock.Zona;
import business.subStock.ZonaArmazenamento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ZonaDAO implements Map<String, Zona> {
    private static ZonaDAO singleton = null;
    public final Connection conn = DAOConfig.getConnection();

    public ZonaDAO() {
        DatabaseTables.mkTZona();
    }

    public static ZonaDAO getInstance() {
        if (ZonaDAO.singleton == null)
            ZonaDAO.singleton = new ZonaDAO();
        return ZonaDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Zona");
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
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Zona WHERE Codigo='" + key.toString() + "'");
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Zona u = (Zona) value;
        return this.containsKey(u.getCodigo());
    }

    @Override
    public Zona get(Object key) {
        ZonaArmazenamento t = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Zona WHERE Codigo='" + key + "'");
            if (rs.next()) {  // A chave existe na tabela
                String zcod = rs.getString("Codigo");
                int nv = rs.getInt("numVertice");
                float custo = rs.getFloat("custoVertice");

                Vertice tRes = new Vertice(nv, null, custo);
                tRes.setZona(t = new ZonaArmazenamento(zcod, tRes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public Zona put(String key, Zona value) {
        Zona u = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO Zona VALUES ('" + value.getCodigo() + "', '" + value.getVertice().getNumero() + "','" + value.getVertice().getCusto() + "' )" +
                            "ON DUPLICATE KEY UPDATE Codigo=VALUES(Codigo), numVertice=VALUES(numVertice), custoVertice = VALUES(custoVertice)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return u;
    }

    @Override
    public Zona remove(Object key) {
        Zona t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Zona WHERE Codigo='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Zona> zonas) {
        zonas.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("TRUNCATE Zona");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<String> keySet() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Zona");
            Set<String> res = new HashSet<>();
            while (rs.next()) {
                res.add(rs.getString("Codigo"));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Collection<Zona> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Zona");
            Collection<Zona> col = new HashSet<>();
            while (rs.next()) {
                col.add(this.get(rs.getString("Codigo")));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<String, Zona>> entrySet() {
        try {
            String aux;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Zona");
            Set<Entry<String, Zona>> col = new HashSet<>();
            while (rs.next()) {
                aux = rs.getString("Codigo");
                col.add(new AbstractMap.SimpleEntry<>(aux, this.get(aux)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

}

