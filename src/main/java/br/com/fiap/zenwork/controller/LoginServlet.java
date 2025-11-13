package br.com.fiap.zenwork.controller;

// Não precisamos mais do DAO!
// import br.com.fiap.zenwork.dao.UsuarioDAO; 
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
// Não precisamos mais do SQLException
// import java.sql.SQLException; 
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet que controla o fluxo de Login (SIMPLIFICADO).
 * Ele não consulta o banco, apenas cria um usuário "mock"
 * com base no "role" (papel) solicitado.
 */
@WebServlet("/login") 
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        
        // Lê o JSON enviado (ex: {"role": "user"})
        Map<String, String> loginData = gson.fromJson(request.getReader(), Map.class);
        String role = loginData.get("tipo");

        Map<String, Object> jsonResponse = new HashMap<>();
        Usuario usuario = null;

        if ("admin".equals(role)) {
            // Se o role for "admin", cria um usuário admin mock
            usuario = new Usuario();
            usuario.setIdUsuario(99); // ID Fixo para o admin
            usuario.setNome("Maria (Admin)");
            usuario.setEmail("admin@zen.work");
            usuario.setAdmin(true);
            
        } else {
            // Para qualquer outro role, cria um usuário funcionário mock
            usuario = new Usuario();
            usuario.setIdUsuario(1); // ID Fixo para o usuário
            usuario.setNome("João Silva");
            usuario.setEmail("joao@zen.work");
            usuario.setAdmin(false);
        }

        // --- A Lógica da Sessão continua a mesma ---
        
        // Cria uma Sessão no backend
        HttpSession session = request.getSession();
        session.setAttribute("usuarioLogado", usuario); // Armazena o objeto Usuario na sessão
        session.setAttribute("idUsuario", usuario.getIdUsuario()); 
        session.setAttribute("isAdmin", usuario.isAdmin()); 

        // Envia o objeto 'usuario' de volta para o JavaScript
        jsonResponse.put("status", "success");
        jsonResponse.put("isAdmin", usuario.isAdmin());
        jsonResponse.put("usuario", usuario); 
        
        // Envia a resposta de sucesso
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}