package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.UsuarioDAO;
import br.com.fiap.zenwork.model.Usuario;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cadastro") // A URL que o HTML vai chamar
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); // <--- ADICIONE ESTA LINHA AQUI NO TOPO
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // 1. Ler o JSON enviado pelo JavaScript do cadastro.html
            // Usamos a classe Usuario (POJO) para desserializar o JSON
            Usuario usuario = gson.fromJson(request.getReader(), Usuario.class);

            // 2. Validar dados (simples)
            if (usuario.getNome() == null || usuario.getEmail() == null || usuario.getSenha() == null ||
                usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()) {
                throw new Exception("Todos os campos são obrigatórios.");
            }
            
            // 3. O usuário é sempre 'não-admin' (is_admin = false) no cadastro
            usuario.setAdmin(false);

            // 4. Chamar o DAO para salvar no banco
            UsuarioDAO dao = new UsuarioDAO();
            dao.salvar(usuario);

            // 5. Enviar resposta de sucesso
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Conta criada com sucesso! Você será redirecionado para o login.");
            out.print(gson.toJson(jsonResponse));

        } catch (SQLException e) {
            // Erro de banco (ex: e-mail duplicado)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Erro 400
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro no banco de dados. O e-mail já pode estar em uso.");
            out.print(gson.toJson(jsonResponse));
            e.printStackTrace();
        } catch (Exception e) {
            // Erro geral (ex: campos vazios)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Erro 400
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro: " + e.getMessage());
            out.print(gson.toJson(jsonResponse));
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}