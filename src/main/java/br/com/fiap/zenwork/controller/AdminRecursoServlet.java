package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.RecursoAjudaDAO;
import br.com.fiap.zenwork.model.RecursoAjuda;
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
import java.util.List;
import java.util.Map;

@WebServlet("/admin-recursos")
public class AdminRecursoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    // READ (Listar ou Buscar por ID)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("\"application/json; charset=UTF-8\"");
        resp.setCharacterEncoding("UTF-8");

 // Mantenha esta por segurança
        
        if (!isAdmin(req)) { 
            resp.setStatus(403); 
            return; 
        }

        try {
            RecursoAjudaDAO dao = new RecursoAjudaDAO();
            String id = req.getParameter("id");
            
            if (id != null) {
                // Buscar um recurso (para editar)
                RecursoAjuda r = dao.buscarPorId(Integer.parseInt(id));
                resp.getWriter().print(gson.toJson(r));
            } else {
                // Listar todos
                List<RecursoAjuda> lista = dao.listarTodos();
                resp.getWriter().print(gson.toJson(lista));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    // CREATE (Salvar Novo)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // <--- ADICIONE ESTA LINHA (Obrigatorio ser a primeira)
        processarSalvar(req, resp, false);
    }

    // UPDATE (Atualizar Existente)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // <--- ADICIONE ESTA LINHA
        processarSalvar(req, resp, true);
    }

    // DELETE (Apagar)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdmin(req)) { resp.setStatus(403); return; }
        
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            new RecursoAjudaDAO().deletar(id);
            enviarSucesso(resp, "Deletado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    // --- Helpers ---

    private void processarSalvar(HttpServletRequest req, HttpServletResponse resp, boolean isUpdate) throws IOException {
        if (!isAdmin(req)) { resp.setStatus(403); return; }
        
        try {
            RecursoAjuda recurso = gson.fromJson(req.getReader(), RecursoAjuda.class);
            RecursoAjudaDAO dao = new RecursoAjudaDAO();
            
            if (isUpdate) {
                dao.atualizar(recurso);
            } else {
                dao.salvar(recurso);
            }
            enviarSucesso(resp, "Salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        return u != null && u.isAdmin();
    }

    private void enviarSucesso(HttpServletResponse resp, String msg) throws IOException {
        resp.setContentType("application/json");
        Map<String, String> map = new HashMap<>();
        map.put("status", "success");
        map.put("message", msg);
        resp.getWriter().print(gson.toJson(map));
    }
}