package data;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTables {

    public static final Connection conn = DAOConfig.getConnection();

    public static void mkTMateriaPrima() {
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS MateriaPrima (" +
                            "ID INT NOT NULL PRIMARY KEY," +
                            "Nome varchar(45) NOT NULL)");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void mkTZona() {
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Zona (" +
                            "Codigo varchar(45) NOT NULL PRIMARY KEY," +
                            "numVertice INT NOT NULL," +
                            "custoVertice FLOAT NOT NULL," +
                            "KEY `numVertice` (`numVertice`));"
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void mkTAresta() {
        mkTZona();
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Aresta (" +
                            "ID varchar(45) NOT NULL PRIMARY KEY," +
                            "custo float NOT NULL," +
                            "verticeOrigem INT NOT NULL," +
                            "verticeDestino INT NOT NULL," +
                            "FOREIGN KEY(verticeOrigem) REFERENCES Zona(numVertice)," +
                            "FOREIGN KEY(verticeDestino) REFERENCES Zona(numVertice))");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void mkTPalete() {
        mkTMateriaPrima();
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Palete (" +
                            "codigoPalete varchar(45) NOT NULL PRIMARY KEY," +
                            "descricao varchar(45) NOT NULL," +
                            "MateriaID INT NOT NULL," +
                            "FOREIGN KEY(MateriaID) REFERENCES MateriaPrima(ID));");

            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS EstadoPalete (" +
                            "codigoPalete varchar(45) NOT NULL PRIMARY KEY," +
                            "zona varchar(45) DEFAULT NULL," +
                            "robo varchar(45) DEFAULT NULL," +
                            "prateleira INT DEFAULT NULL," +
                            "FOREIGN KEY(codigoPalete) REFERENCES Palete(codigoPalete))");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void mkTPrateleira() {
        mkTZona();
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Prateleira (" +
                            "Codigo INT NOT NULL PRIMARY KEY," +
                            "CustoAcesso float NOT NULL," +
                            "Disponibilidade INT NOT NULL," +
                            "capacidade INT  NOT NULL," +
                            "zona_codigo varchar(45) NULL," +
                            "FOREIGN KEY(zona_codigo) REFERENCES Zona(Codigo)) ");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void mkTPrateleiraHasPalete() {
        mkTPrateleira();
        mkTPalete();
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS PrateleiraHasPalete (" +
                            "codigoPalete varchar(45) NOT NULL PRIMARY KEY ," +
                            "CodigoPrat INT NOT NULL," +
                            "FOREIGN KEY(codigoPalete) REFERENCES Palete(codigoPalete)," +
                            "FOREIGN KEY(CodigoPrat) REFERENCES Prateleira(Codigo))");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void mkTRobo() {
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Robo (" +
                            "codigoRobo varchar(90) NOT NULL PRIMARY KEY," +
                            "modelo varchar(225) NOT NULL," +
                            "codUltimaLocalizacao INT NOT NULL," +
                            "vazio TINYINT NOT NULL )");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void mkTOrdemTransporte() {
        mkTRobo();
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS OrdemTransporte (" +
                            "codigoRobo varchar(90) NOT NULL PRIMARY KEY," +
                            "codPrateleiraDestino INT NOT NULL," +
                            "palete varchar(45) NOT NULL," +
                            "origem INT NOT NULL," +
                            "destino INT NOT NULL," +
                            "FOREIGN KEY(codigoRobo) REFERENCES Robo(codigoRobo));");

            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Percurso (" +
                            "codigoRobo varchar(90) NOT NULL," +
                            "vertice INT NOT NULL," +
                            "FOREIGN KEY(codigoRobo) REFERENCES OrdemTransporte(codigoRobo));");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
