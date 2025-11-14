package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.UsuarioDAO;
import br.com.fiap.zenwork.model.Usuario;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // 1. Ler o JSON vindo do login.html
            Map<String, String> loginData = gson.fromJson(request.getReader(), Map.class);
            String email = loginData.get("email");
            String senha = loginData.get("senha");

            // 2. Chamar o DAO para consultar o banco
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.buscarPorEmailESenha(email, senha); // <-- USA O BANCO DE DADOS

            if (usuario != null) {
                // 3. SUCESSO!
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", usuario);

                // 4. Preparar resposta de sucesso
                jsonResponse.put("status", "success");
                jsonResponse.put("usuario", usuario); // Envia o objeto usuário para o JS
                
                // 5. Define o redirecionamento
                if(usuario.isAdmin()) {
                    jsonResponse.put("redirectUrl", "./admin-dashboard.html");
                } else {
                    jsonResponse.put("redirectUrl", "./dashboard.html");
                }
                
            } else {
                // 6. FALHA!
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "E-mail ou senha inválidos.");
            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
             response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}