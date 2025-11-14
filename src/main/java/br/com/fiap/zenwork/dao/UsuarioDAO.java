package br.com.fiap.zenwork.dao;

import br.com.fiap.zenwork.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DAO (Data Access Object) para a entidade Usuario.
 * Contém a lógica de CRUD (Create, Read, Update, Delete) para o usuário.
 * Cumpre o requisito de persistência de dados (JDBC).
 */
public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() throws SQLException {
        // Obtém a conexão do ConnectionFactory (que já configuramos para Oracle)
        this.connection = ConnectionFactory.getConnection();
    }

    /**
     * Salva um novo usuário no banco de dados.
     * Usado pelo CadastroServlet.
     *
     * @param usuario O objeto Usuario (POJO) com nome, email e senha.
     */
    public void salvar(Usuario usuario) throws SQLException {
        // O Oracle não precisa do 'is_admin' (default 0) nem do 'id_usuario' (GENERATED)
        String sql = "INSERT INTO Usuario (nome, email, senha) VALUES (?, ?, ?)";
         
         try (PreparedStatement stmt = connection.prepareStatement(sql)) {
             stmt.setString(1, usuario.getNome());
             stmt.setString(2, usuario.getEmail());
             stmt.setString(3, usuario.getSenha()); // No mundo real, usaríamos hash
             
             stmt.executeUpdate();
             
         } catch (SQLException e) {
             System.err.println("Erro ao salvar usuário: " + e.getMessage());
             // Lança a exceção para que o Servlet possa tratá-la (ex: email duplicado)
             throw new SQLException("Erro ao salvar usuário no banco de dados.", e);
         }
    }


    /**
     * Busca um usuário no banco de dados pelo email e senha.
     * Este é o método principal para o LoginServlet.
     *
     * @param email O email digitado pelo usuário.
     * @param senha A senha digitada pelo usuário.
     * @return Um objeto Usuario se o login for bem-sucedido, ou null se não for.
     */
    public Usuario buscarPorEmailESenha(String email, String senha) {
        // Lembre-se que o banco Oracle é case-sensitive
        String sql = "SELECT * FROM Usuario WHERE email = ? AND senha = ?";
        Usuario usuario = null;

        // try-with-resources garante que o PreparedStatement e o ResultSet sejam fechados
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Define os parâmetros da query
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                // Verifica se encontrou um resultado
                if (rs.next()) {
                    // Cria o objeto Usuario com os dados do banco
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    
                    // No Oracle, 0 é FALSE e 1 é TRUE
                    usuario.setAdmin(rs.getInt("is_admin") == 1); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração
        }
        
        return usuario; // Retorna o usuário (se encontrado) ou null
    }

}