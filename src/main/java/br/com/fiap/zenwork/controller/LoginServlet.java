package br.com.fiap.zenwork.controller;

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
import java.util.HashMap;
import java.util.Map;

// *** IMPORTANTE: Verifique se você está usando 'javax' e não 'jakarta' ***
// Se você trocou o POM para 'jakarta', troque os 'javax' abaixo.

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            // 1. Decodificar o JSON vindo do frontend
            Map<String, String> loginData = gson.fromJson(request.getReader(), Map.class);
            String tipo = loginData.get("tipo"); // "usuario" ou "admin"

            Usuario usuario = null;

            // 2. Lógica de "Mock" (Login Falso) - Como você pediu
            if ("admin".equals(tipo)) {
                usuario = new Usuario();
                usuario.setIdUsuario(1); // ID Fixo para admin
                usuario.setNome("Maria Silva (Admin)");
                usuario.setEmail("admin@zen.work"); // Email de admin
                usuario.setAdmin(true); // *** IMPORTANTE ***
            } else if ("usuario".equals(tipo)) {
                usuario = new Usuario();
                usuario.setIdUsuario(2); // ID Fixo para funcionário
                usuario.setNome("João");
                usuario.setEmail("joao@zen.work"); // Email de funcionário
                usuario.setAdmin(false); // *** IMPORTANTE ***
            }

            if (usuario != null) {
                // 3. Criar a Sessão
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", usuario);

                // 4. Preparar a Resposta JSON
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("status", "success");
                responseData.put("usuario", usuario);
                
                // *** A MÁGICA ESTÁ AQUI ***
                // Define para qual página o frontend deve redirecionar
                if(usuario.isAdmin()) {
                    responseData.put("redirectUrl", "./admin-dashboard.html");
                } else {
                    responseData.put("redirectUrl", "./dashboard.html");
                }

                out.print(gson.toJson(responseData));
            } else {
                throw new Exception("Tipo de usuário inválido");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorData = new HashMap<>();
            errorData.put("status", "error");
            errorData.put("message", "Erro no servidor: " + e.getMessage());
            out.print(gson.toJson(errorData));
        } finally {
            out.flush();
        }
    }
}