package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.RegistroBemEstarDAO;
import br.com.fiap.zenwork.model.RegistroBemEstar;
import br.com.fiap.zenwork.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Adaptador para o GSON entender LocalDateTime (importante!)
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value != null) {
            out.value(value.toString()); 
        } else {
            out.nullValue();
        }
    }
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString().replace("Z", ""));
    }
}
// Fim do Adaptador

@WebServlet("/historico")
public class HistoricoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        // Configura o Gson para formatar LocalDateTime corretamente
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        this.gson = gsonBuilder.create();
    }

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

            // 2. Pegar o ID do usuário da SESSÃO
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            
            // 3. Chamar o DAO para buscar o histórico
            RegistroBemEstarDAO dao = new RegistroBemEstarDAO();
            List<RegistroBemEstar> historico = dao.listarPorUsuario(usuarioLogado.getIdUsuario());

            // 4. Enviar a lista de histórico como JSON
            out.print(gson.toJson(historico)); 

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