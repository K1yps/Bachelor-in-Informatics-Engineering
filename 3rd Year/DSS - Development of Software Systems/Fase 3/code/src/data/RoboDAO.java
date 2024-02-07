package data;

import java.sql.*;
import java.util.*;

import business.subRobos.OrdemTransporte;
import business.subRobos.Robo;

public class RoboDAO implements Map<String, Robo> {
    private static RoboDAO singleton = null;
    public final Connection conn = DAOConfig.getConnection();

    public RoboDAO() {
        DatabaseTables.mkTRobo();
        DatabaseTables.mkTOrdemTransporte();
    }

    public static RoboDAO getInstance() {
        if (RoboDAO.singleton == null)
            RoboDAO.singleton = new RoboDAO();

        return RoboDAO.singleton;
    }

    public OrdemTransporte getOrdemTransporte(String codigoRobo) {
        OrdemTransporte t = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet rsp = stm.executeQuery("SELECT * FROM Percurso WHERE codigoRobo='" + codigoRobo + "'");
            ResultSet rso = stm.executeQuery("SELECT * FROM OrdemTransporte WHERE codigoRobo='" + codigoRobo + "'");
            if (rso.next()) {  // A chave existe na tabela
                int codPrateleiraDestino = rso.getInt("codPrateleiraDestino");
                String palete = rso.getString("palete");
                int origem = rso.getInt("origem");
                int destino = rso.getInt("destino");
                List<Integer> percurso = new ArrayList<>();
                while (rsp.next()) {
                    percurso.add(rsp.getInt("vertice"));
                }
                t = new OrdemTransporte(codPrateleiraDestino, palete, origem, destino, percurso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    public OrdemTransporte putOrdemTransporte(String key, OrdemTransporte value) {
        OrdemTransporte t = this.getOrdemTransporte(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO OrdemTransporte VALUES ('" + key + "', '" + value.getCodPrateleiraDestino() + "', '" + value.getPalete() + "', '" + value.getOrigem() + "', '" + value.getDestino() + "' ) " +
                            "ON DUPLICATE KEY UPDATE codigoRobo=VALUES(codigoRobo), codPrateleiraDestino=VALUES(codPrateleiraDestino), palete=VALUES(palete), origem=VALUES(origem), destino=VALUES(destino) ");
            value.getPercursototal()
                    .forEach(s -> {
                        try {
                            stm.executeUpdate(
                                    "INSERT INTO Percurso VALUES ('" + key + "'," + s + ") " +
                                            "ON DUPLICATE KEY UPDATE codigoRobo=VALUES(codigoRobo), vertice=VALUES(vertice)");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }


    @Override
    public int size() {
        int i = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Robo");
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
            ResultSet rs = stm.executeQuery("SELECT codigoRobo FROM Robo WHERE codigoRobo='" + key.toString() + "'");
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Robo u = (Robo) value;
        return this.containsKey(u.getCodRobo());
    }


    @Override
    public Robo get(Object key) {
        Robo t = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Robo WHERE codigoRobo='" + key + "'");
            if (rs.next()) {  // A chave existe na tabela
                String codigo = rs.getString("codigoRobo");
                String modelo = rs.getString("modelo");
                int codUltimaLocalizacao = rs.getInt("codUltimaLocalizacao");
                Boolean b = rs.getBoolean("vazio");
                t = new Robo(codigo, modelo, b, codUltimaLocalizacao, this.getOrdemTransporte(codigo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }


    @Override
    public Robo put(String key, Robo value) {
        Robo u = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO Robo VALUES ('" + value.getCodRobo() + "', '" + value.getModelo() + "', '" + value.getCodigoLocalizacao() + "', " + (value.getVazio() ? 1 : 0) + ") " +
                            "ON DUPLICATE KEY UPDATE codigoRobo=VALUES(codigoRobo), modelo=VALUES(modelo), codUltimaLocalizacao=VALUES(codUltimaLocalizacao), vazio=VALUES(vazio) ");
            OrdemTransporte t;
            if ((t = value.getOrdem()) != null)
                this.putOrdemTransporte(key, t);
            else {
                stm.executeUpdate("DELETE FROM Percurso WHERE codigoRobo ='" + key + "'");
                stm.executeUpdate("DELETE FROM OrdemTransporte WHERE codigoRobo ='" + key + "'");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return u;
    }

    @Override
    public Robo remove(Object key) {
        Robo t = this.get(key);
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Robo WHERE codigoRobo='" + key + "'");
            stm.executeUpdate("DELETE FROM OrdemTransporte WHERE codigoRobo='" + key + "'");
            stm.executeUpdate("DELETE FROM Percurso WHERE codigoRobo='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Robo> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("TRUNCATE Robo");
            stm.executeUpdate("TRUNCATE OrdemTransporte");
            stm.executeUpdate("TRUNCATE Percurso");
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
            ResultSet rs = stm.executeQuery("SELECT codigoRobo FROM Robo");
            Set<String> res = new HashSet<>();
            while (rs.next()) {
                res.add(rs.getString("codigoRobo"));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    @Override
    public Collection<Robo> values() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codigoRobo FROM Robo");
            Collection<Robo> col = new HashSet<>();
            while (rs.next()) {
                col.add(this.get(rs.getString("codigoRobo")));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Map.Entry<String, Robo>> entrySet() {
        try {
            String aux;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codigoRobo FROM Robo");
            Set<Map.Entry<String, Robo>> col = new HashSet<>();
            while (rs.next()) {
                aux = rs.getString("codigoRobo");
                col.add(new AbstractMap.SimpleEntry<>(aux, this.get(aux)));
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
}
