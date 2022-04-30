package data;


import business.subStock.MateriaPrima;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MateriaPrimaDAO implements Map<Integer, MateriaPrima> {
    private static MateriaPrimaDAO singleton = null;
    public final Connection conn = DAOConfig.getConnection();


    public MateriaPrimaDAO() {
        DatabaseTables.mkTMateriaPrima();
    }

    public static MateriaPrimaDAO getInstance() {
        if (MateriaPrimaDAO.singleton == null)
            MateriaPrimaDAO.singleton = new MateriaPrimaDAO();

        return MateriaPrimaDAO.singleton;
    }


    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM MateriaPrima");
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
            ResultSet rs = stm.executeQuery("SELECT ID FROM MateriaPrima WHERE ID='" + key.toString() + "'");
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        MateriaPrima u = (MateriaPrima) value;
        return this.containsKey(u.getId());
    }

    @Override
    public MateriaPrima get(Object key) {
        MateriaPrima t = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM MateriaPrima WHERE ID='" + key + "'");
            if (rs.next()) {  // A chave existe na tabela
                int ID = rs.getInt("ID");
                String nome = rs.getString("Nome");

                t = new MateriaPrima(ID, nome);
            }

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public MateriaPrima put(Integer key, MateriaPrima value) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO MateriaPrima VALUES ('" + value.getId() + "', '" + value.getNome() + "' ) " +
                            "ON DUPLICATE KEY UPDATE ID=VALUES(ID), Nome=VALUES(Nome)");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    @Override
    public MateriaPrima remove(Object key) {
        MateriaPrima t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM MateriaPrima WHERE ID='" + key + "'");
            stm.executeUpdate("DELETE FROM Palete WHERE MateriaID='" + key + "'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends MateriaPrima> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("TRUNCATE MateriaPrima");
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
            ResultSet rs = stm.executeQuery("SELECT ID FROM MateriaPrima");
            Set<Integer> res = new HashSet<>();
            while (rs.next()) {
                res.add(rs.getInt("ID"));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Collection<MateriaPrima> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT ID FROM MateriaPrima");
            Collection<MateriaPrima> col = new HashSet<>();
            while (rs.next()) {
                col.add(this.get(rs.getInt("ID")));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<Integer, MateriaPrima>> entrySet() {
        try {
            Integer aux;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT ID FROM MateriaPrima");
            Set<Entry<Integer, MateriaPrima>> col = new HashSet<>();
            while (rs.next()) {
                aux = rs.getInt("ID");
                col.add(new AbstractMap.SimpleEntry<>(aux, this.get(aux)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
}
