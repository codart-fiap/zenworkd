package br.com.fiap.zenwork.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carrega o driver JDBC da Oracle
                Class.forName("oracle.jdbc.driver.OracleDriver");
                
                // Carrega as propriedades do banco de dados
                Properties props = new Properties();
                try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (input == null) {
                        System.err.println("Desculpe, não foi possível encontrar o arquivo db.properties");
                        throw new SQLException("Arquivo db.properties não encontrado no classpath");
                    }
                    props.load(input);
                }

                // Obtém as propriedades
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");
                
                // Estabelece a conexão
                connection = DriverManager.getConnection(url, user, password);
                
                System.out.println("Conexão com o banco Oracle estabelecida com sucesso!");

            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC da Oracle não encontrado!");
                throw new SQLException("Driver JDBC da Oracle não encontrado", e);
            } catch (Exception e) { // Mudado para Exception para pegar o IOException do props.load
                System.err.println("Erro ao conectar ao banco Oracle: " + e.getMessage());
                throw new SQLException("Erro ao conectar ao banco Oracle", e);
            }
        }
        return connection;
    }
    
   

    
    // (O método closeConnection() continua o mesmo)
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}