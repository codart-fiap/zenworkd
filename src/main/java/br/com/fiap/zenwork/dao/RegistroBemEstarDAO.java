package br.com.fiap.zenwork.dao;

import br.com.fiap.zenwork.model.RegistroBemEstar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RegistroBemEstarDAO {

    private Connection connection;

    public RegistroBemEstarDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void salvar(RegistroBemEstar registro) throws SQLException {
        String sql = "INSERT INTO RegistroBemEstar (id_usuario, emocao_selecionada, comentario, data_registro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, registro.getIdUsuario());
            stmt.setString(2, registro.getEmocaoSelecionada());
            stmt.setString(3, registro.getComentario());
            stmt.setTimestamp(4, Timestamp.valueOf(registro.getDataRegistro()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao salvar check-in", e);
        }
    }

    public List<RegistroBemEstar> listarPorUsuario(int idUsuario) {
        return listarGenerico("SELECT * FROM RegistroBemEstar WHERE id_usuario = ? ORDER BY data_registro DESC", idUsuario);
    }

    // *** NOVO MÉTODO PARA O DASHBOARD DO ADMIN ***
    public List<RegistroBemEstar> listarTodosRecentes() {
        // Pega os últimos 50 registros de qualquer usuário para gerar estatísticas
        // No Oracle 12c+ usa-se "FETCH FIRST", mas para compatibilidade usamos ROWNUM ou limit no Java
        return listarGenerico("SELECT * FROM RegistroBemEstar ORDER BY data_registro DESC", null);
    }

    private List<RegistroBemEstar> listarGenerico(String sql, Integer idUsuario) {
        List<RegistroBemEstar> registros = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (idUsuario != null) {
                stmt.setInt(1, idUsuario);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegistroBemEstar r = new RegistroBemEstar();
                    r.setIdRegistro(rs.getInt("id_registro"));
                    r.setIdUsuario(rs.getInt("id_usuario"));
                    Timestamp ts = rs.getTimestamp("data_registro");
                    if (ts != null) r.setDataRegistro(ts.toLocalDateTime());
                    r.setEmocaoSelecionada(rs.getString("emocao_selecionada"));
                    r.setComentario(rs.getString("comentario"));
                    registros.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar: " + e.getMessage());
        }
        return registros;
    }
}