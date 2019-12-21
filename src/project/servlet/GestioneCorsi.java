package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.Corso;
import project.servlet.model.DAO;

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
    private static final Gson gson = new Gson();

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
        HttpSession session = request.getSession(false);
        if (session == null) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/loginPage.html");
            requestDispatcher.forward(request, response);
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        boolean admin = (boolean) session.getAttribute("admin");

        JsonObject jsonResponse = new JsonObject();
        JsonElement data = null;
        boolean success = false;

        if(admin){
            String op = request.getParameter("op");
            String corso = request.getParameter("corso");
            switch (op) {
                case "inserire":
                    if (corso != null)
                        success = DAO.insertCorso(new Corso(corso));
                    break;
                case "eliminare":
                    if (corso != null)
                        success = DAO.deleteCorso(new Corso(corso));
                    break;
                case "visualizzare":
                    List<Corso> corsi = DAO.getCorsi();
                    success = true;
                    data = gson.toJsonTree(corsi, new TypeToken<List<Corso>>(){}.getType());
            }

            if(success)
                jsonResponse.addProperty("result", "success");
            else
                jsonResponse.addProperty("result", "failure");
            jsonResponse.add("data", data);
        } else
            jsonResponse.addProperty("result", "failure");

        out.print(jsonResponse);
        out.flush();
        out.close();
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
