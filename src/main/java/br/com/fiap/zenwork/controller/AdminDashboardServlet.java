
package br.com.fiap.zenwork.controller;

import br.com.fiap.zenwork.dao.RegistroBemEstarDAO;
import br.com.fiap.zenwork.model.RegistroBemEstar;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

 // Mantenha esta por segurança

        try {
            RegistroBemEstarDAO dao = new RegistroBemEstarDAO();
            List<RegistroBemEstar> registros = dao.listarTodosRecentes();

            // Calcular Estatísticas Reais
            int totalCheckins = registros.size();
            int feliz = 0, neutro = 0, cansado = 0, estressado = 0;

            for (RegistroBemEstar r : registros) {
                String emocao = r.getEmocaoSelecionada();
                if ("Feliz".equals(emocao)) feliz++;
                else if ("Neutro".equals(emocao)) neutro++;
                else if ("Cansado".equals(emocao)) cansado++;
                else if ("Estressado".equals(emocao)) estressado++;
            }

            // Montar o JSON que o Chart.js espera
            Map<String, Object> data = new HashMap<>();
            
            // Stats Cards
            List<Map<String, Object>> stats = new ArrayList<>();
            stats.add(criarStat("Total Check-ins", String.valueOf(totalCheckins), "Geral"));
            stats.add(criarStat("Nível Estresse", String.valueOf(estressado), "Alertas"));
            data.put("stats", stats);

            // Dados para o Gráfico de Rosca (Sentimento)
            Map<String, Object> sentimento = new HashMap<>();
            sentimento.put("labels", new String[]{"Feliz", "Neutro", "Cansado", "Estressado"});
            sentimento.put("data", new int[]{feliz, neutro, cansado, estressado});
            data.put("sentimento", sentimento);

            // Dados simulados para Tendência (apenas para preencher o gráfico de linha)
            Map<String, Object> tendencia = new HashMap<>();
            tendencia.put("labels", new String[]{"Seg", "Ter", "Qua", "Qui", "Sex"});
            tendencia.put("data", new double[]{3.5, 3.8, 3.2, 4.0, 3.9});
            data.put("tendencia", tendencia);
            
            // Tópicos (fixos por enquanto)
            data.put("topicos", new String[]{"Prazos", "Reuniões", "Feedback"});

            new Gson().toJson(data, resp.getWriter());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    private Map<String, Object> criarStat(String label, String value, String trend) {
        Map<String, Object> map = new HashMap<>();
        map.put("label", label);
        map.put("value", value);
        map.put("trend", trend);
        return map;
    }
}
