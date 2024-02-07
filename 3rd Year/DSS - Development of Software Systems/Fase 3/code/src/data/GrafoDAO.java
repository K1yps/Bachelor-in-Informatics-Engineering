package data;

import business.subStock.Aresta;
import business.subStock.Vertice;
import business.subStock.ZonaArmazenamento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class GrafoDAO implements Map<Vertice, List<Aresta>> {
    private static GrafoDAO singleton = null;
    public final Connection conn = DAOConfig.getConnection();

    public GrafoDAO() {
        DatabaseTables.mkTAresta();
    }

    public static GrafoDAO getInstance() {
        if (GrafoDAO.singleton == null)
            GrafoDAO.singleton = new GrafoDAO();

        return GrafoDAO.singleton;
    }

    private Vertice getCurrentVertice(ResultSet rs) {
        try {
            String zcod = rs.getString("Codigo");
            int nv = rs.getInt("numVertice");
            float custo = rs.getFloat("custoVertice");

            Vertice tRes = new Vertice(nv, null, custo);
            tRes.setZona(new ZonaArmazenamento(zcod, tRes));
            return tRes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
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
            ResultSet rs = stm.executeQuery("SELECT numVertice FROM Zona WHERE numVertice=" + ((Vertice) key).getNumero());
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Aresta u = ((Collection<Aresta>) value).iterator().next();
        return this.containsKey(u.getOrigem());
    }

    @Override
    public List<Aresta> get(Object key) {
        Vertice v = (Vertice) key;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Zona AS ZN INNER JOIN (SELECT * FROM Aresta WHERE verticeOrigem = " +
                    (v).getNumero() + ") AS AR ON AR.verticeOrigem = ZN.numVertice");
            List<Aresta> res = new ArrayList<>();
            while (rs.next()) {  // A chave existe na tabela
                String ID = rs.getString("ID");
                float custo = rs.getFloat("custo");
                int vertDest = rs.getInt("verticeDestino");
                String codZon = rs.getString("Codigo");

                Vertice dest = new Vertice(vertDest, null, custo);
                dest.setZona(new ZonaArmazenamento(codZon, dest));

                res.add(new Aresta(ID, custo, v, dest));
            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public List<Aresta> put(Vertice key, List<Aresta> value) {
        List<Aresta> res = this.get(key);
        int k = key.getNumero();
        try {
            Statement stm = conn.createStatement();
            value.forEach(
                    s -> {
                        try {
                            stm.executeUpdate(
                                    "INSERT INTO Aresta VALUES ('" + s.getId() + "', '" + s.getCusto() + "', '" + s.getOrigem().getNumero() + "','" + s.getDestino().getNumero() + "' ) " +
                                            "ON DUPLICATE KEY UPDATE ID=VALUES(ID), Custo=VALUES(Custo), verticeOrigem = VALUES(verticeOrigem), verticeDestino = VALUES(verticeDestino)");
                        } catch (SQLException e) {
                            throw new NullPointerException(e.getMessage());
                        }
                    });

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }


    @Override
    public List<Aresta> remove(Object key) {
        List<Aresta> res = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Aresta WHERE verticeOrigem ='" + ((Vertice) key).getNumero() + "'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public void putAll(Map<? extends Vertice, ? extends List<Aresta>> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("TRUNCATE Aresta");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    //Codigo varchar(45) NOT NULL PRIMARY KEY," +
    //                    "numVertice INTEGER NOT NULL," +
    //                    "custoVertice FLOAT NOT NULL)" +

    @Override
    public Set<Vertice> keySet() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Zona");
            Set<Vertice> res = new HashSet<>();
            while (rs.next())
                res.add(getCurrentVertice(rs));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Collection<List<Aresta>> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Zona");
            Collection<List<Aresta>> col = new HashSet<>();
            while (rs.next())
                col.add(this.get(getCurrentVertice(rs)));
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<Vertice, List<Aresta>>> entrySet() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Codigo FROM Zona");
            Set<Entry<Vertice, List<Aresta>>> col = new HashSet<>();
            while (rs.next()) {
                Vertice k = getCurrentVertice(rs);
                col.add(new AbstractMap.SimpleEntry<>(k, this.get(k)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


}
