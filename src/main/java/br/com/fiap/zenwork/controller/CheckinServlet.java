package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.RegistroBemEstarDAO;
import br.com.fiap.zenwork.model.RegistroBemEstar;
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
import java.time.LocalDateTime; // Importante
import java.util.HashMap;
import java.util.Map;

@WebServlet("/checkin")
public class CheckinServlet extends HttpServlet {
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
            // 1. Verificar se o usuário está logado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuarioLogado") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Usuário não autenticado. Faça login novamente.");
                out.print(gson.toJson(jsonResponse));
                out.flush();
                return;
            }

            // 2. Pegar o usuário da SESSÃO
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

            // 3. Ler o JSON enviado pelo JavaScript
            Map<String, String> checkinData = gson.fromJson(request.getReader(), Map.class);

            // 4. Criar o objeto de modelo (POJO)
            RegistroBemEstar registro = new RegistroBemEstar();
            registro.setIdUsuario(usuarioLogado.getIdUsuario());
            registro.setEmocaoSelecionada(checkinData.get("emocao"));
            registro.setComentario(checkinData.get("comentario"));
            registro.setDataRegistro(LocalDateTime.now()); // Define a data/hora atual

            // 5. Salvar no banco usando o DAO
            RegistroBemEstarDAO dao = new RegistroBemEstarDAO();
            dao.salvar(registro);

            // 6. Enviar resposta de sucesso
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Check-in salvo com sucesso!");
            out.print(gson.toJson(jsonResponse));

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