package data;

import business.subStaff.Gestor;
import business.subStaff.Utilizador;
import business.subStock.Palete;

import java.sql.*;
import java.util.*;

public class UtilizadorDAO implements Map<String, Utilizador> {
    private static UtilizadorDAO singleton = null;
    public final Connection conn = DAOConfig.getConnection();

    public UtilizadorDAO() {
        try {
            Statement stm = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Utilizador (" +
                    "Username varchar(45) NOT NULL PRIMARY KEY," +
                    "Nome varchar(45) NOT NULL," +
                    "codAcesso varchar(20) NOT NULL)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static UtilizadorDAO getInstance() {
        if (UtilizadorDAO.singleton == null)
            UtilizadorDAO.singleton = new UtilizadorDAO();

        return UtilizadorDAO.singleton;
    }


    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Utilizador");
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
            ResultSet rs = stm.executeQuery("SELECT Username FROM Utilizador WHERE Username='" + key.toString() + "'");
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Utilizador u = (Utilizador) value;
        return this.containsKey(u.getUsername());
    }

    @Override
    public Utilizador get(Object key) {
        Utilizador t = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador WHERE Username='" + key + "'");
            if (rs.next()) {  // A chave existe na tabela

                String nome = rs.getString("Nome");
                String posto = rs.getString("Cargo");
                String codAcesso = rs.getString("codAcesso");

                t = new Gestor((String) key, nome, posto, codAcesso);

            }

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }


    @Override
    public Utilizador put(String key, Utilizador value) {
        Utilizador u = null;
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO Utilizador VALUES ('" + value.getUsername() + "', '" + value.getNome() + "', '" + value.getCodAcesso() + "', NULL) " +
                            "ON DUPLICATE KEY UPDATE Username = VALUES(Username), Nome=VALUES(Nome), CodAcesso=VALUES(CodAcesso)");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return u;

    }

    @Override
    public Utilizador remove(Object key) {
        Utilizador t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Utilizador WHERE Username='" + key + "'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Utilizador> users) {
        users.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("TRUNCATE Utilizador");
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
            ResultSet rs = stm.executeQuery("SELECT Username FROM Utilizador");
            Set<String> res = new HashSet<>();
            while (rs.next()) {
                res.add(rs.getString("Username"));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Collection<Utilizador> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Username FROM Utilizador");
            Collection<Utilizador> col = new HashSet<>();
            while (rs.next()) {
                col.add(this.get(rs.getString("Username")));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<String, Utilizador>> entrySet() {
        try {
            String aux;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Username FROM Utilizador");
            Set<Entry<String, Utilizador>> col = new HashSet<>();
            while (rs.next()) {
                aux = rs.getString("Username");
                col.add(new AbstractMap.SimpleEntry<>(aux, this.get(aux)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
}

