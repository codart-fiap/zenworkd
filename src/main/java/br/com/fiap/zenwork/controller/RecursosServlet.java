package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.RecursoAjudaDAO;
import br.com.fiap.zenwork.model.RecursoAjuda;
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
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/recursos")
public class RecursosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // 1. Verificar se o usuário está logado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuarioLogado") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Usuário não autenticado.");
                out.print(gson.toJson(jsonResponse));
                out.flush();
                return;
            }
            
            // 2. Chamar o DAO para buscar TODOS os recursos
            RecursoAjudaDAO dao = new RecursoAjudaDAO();
            List<RecursoAjuda> recursos = dao.listarTodos();

            // 3. Enviar a lista de recursos como JSON
            out.print(gson.toJson(recursos)); 

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro no banco de dados: " + e.getMessage());
            out.print(gson.toJson(jsonResponse));
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro: " + e.getMessage());
            out.print(gson.toJson(jsonResponse));
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}
