package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.Corso;
import project.servlet.model.DAO;
import project.servlet.model.Docente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GestioneCorsi", urlPatterns = {"/GestioneCorsi"})
public class GestioneCorsi extends HttpServlet {
    private static final Gson json = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        DAO.registerDriver();
    }
    /**
     *
     * @param request parametro "op" a scelta tra: "inserire", "eliminare", "visualizzare"; parametro "corso": oggetto json (nell'ultimo caso null)
     * @param response stampa nei primi due casi un bool, nell'ultimo l'oggetto json richiesto
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = (String) request.getParameter("op");
        Corso corso = json.fromJson(request.getParameter("corso"), Corso.class);
        PrintWriter out = response.getWriter();
        switch (op) {
            case "inserire":
                if(corso==null || corso.getTitolo().equals("")) {
                    out.print(false);
                } else {
                    boolean corretto = DAO.insertCorso(corso);
                    out.print(corretto);
                }
                break;
            case "eliminare":
                if(corso==null || corso.getTitolo().equals("")) {
                    out.print(false);
                } else {
                    boolean corretto = DAO.deleteCorso(corso);
                    out.print(corretto);
                }
                break;
            case "visualizzare":
                response.setContentType("application/json");
                List<Corso> cor = DAO.getCorsi();
                String jsonDoc = json.toJson(cor, new TypeToken<List<Corso>>(){}.getType());
                out.print(jsonDoc);
                break;
            default:
                throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
        }
        out.flush();
        out.close();
        /*response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        List<Corso> corsi = DAO.getCorsi();
        JsonElement data = gson.toJsonTree(corsi, new TypeToken<List<Corso>>(){}.getType());;
        jsonResponse.addProperty("result", "success");
        jsonResponse.add("data", data);
        out.print(jsonResponse);
        out.flush();
        out.close();*/
    }
    // <editor-fold defaultstate="collapsed" desc="- Metodi HttpServlet -">
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    // </editor-fold>
}
