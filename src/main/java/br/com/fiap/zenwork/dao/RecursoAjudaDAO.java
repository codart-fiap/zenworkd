package br.com.fiap.zenwork.dao;

import br.com.fiap.zenwork.model.RecursoAjuda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para a entidade RecursoAjuda.
 * Contém a lógica de CRUD completo para o Admin.
 */
public class RecursoAjudaDAO {

    private Connection connection;

    public RecursoAjudaDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    /**
     * C - (Create) Salva um novo recurso no banco.
     */
    public void salvar(RecursoAjuda recurso) throws SQLException {
        String sql = "INSERT INTO RecursoAjuda (titulo, descricao, link_externo, categoria) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, recurso.getTitulo());
            stmt.setString(2, recurso.getDescricao());
            stmt.setString(3, recurso.getLinkExterno());
            stmt.setString(4, recurso.getCategoria());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar recurso: " + e.getMessage());
            throw new SQLException("Erro ao salvar recurso", e);
        }
    }

    /**
     * R - (Read) Lista TODOS os recursos de ajuda do banco.
     */
    public List<RecursoAjuda> listarTodos() {
        List<RecursoAjuda> recursos = new ArrayList<>();
        String sql = "SELECT * FROM RecursoAjuda ORDER BY categoria, titulo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                recursos.add(mapearResultSetParaRecurso(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar recursos: " + e.getMessage());
        }
        return recursos;
    }

    /**
     * R - (Read) Busca um único recurso pelo seu ID.
     */
    public RecursoAjuda buscarPorId(int id) {
        String sql = "SELECT * FROM RecursoAjuda WHERE id_recurso = ?";
        RecursoAjuda recurso = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    recurso = mapearResultSetParaRecurso(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar recurso por ID: " + e.getMessage());
        }
        return recurso;
    }

    /**
     * U - (Update) Atualiza um recurso existente no banco.
     */
    public void atualizar(RecursoAjuda recurso) throws SQLException {
        String sql = "UPDATE RecursoAjuda SET titulo = ?, descricao = ?, link_externo = ?, categoria = ? WHERE id_recurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, recurso.getTitulo());
            stmt.setString(2, recurso.getDescricao());
            stmt.setString(3, recurso.getLinkExterno());
            stmt.setString(4, recurso.getCategoria());
            stmt.setInt(5, recurso.getIdRecurso());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar recurso: " + e.getMessage());
            throw new SQLException("Erro ao atualizar recurso", e);
        }
    }

    /**
     * D - (Delete) Deleta um recurso do banco pelo ID.
     */
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM RecursoAjuda WHERE id_recurso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar recurso: " + e.getMessage());
            throw new SQLException("Erro ao deletar recurso", e);
        }
    }

    /**
     * Método helper para evitar repetição de código.
     * Converte uma linha do ResultSet em um objeto RecursoAjuda.
     */
    private RecursoAjuda mapearResultSetParaRecurso(ResultSet rs) throws SQLException {
        RecursoAjuda recurso = new RecursoAjuda();
        recurso.setIdRecurso(rs.getInt("id_recurso"));
        recurso.setTitulo(rs.getString("titulo"));
        recurso.setDescricao(rs.getString("descricao"));
        recurso.setLinkExterno(rs.getString("link_externo"));
        recurso.setCategoria(rs.getString("categoria"));
        return recurso;
    }
}