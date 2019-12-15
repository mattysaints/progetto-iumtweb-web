package project.servlet;

import com.google.gson.Gson;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("Login");
            requestDispatcher.include(request, response);
        }
        assert session != null;
        boolean admin = (boolean) session.getAttribute("admin");
        if(admin){
            String op = (String) request.getAttribute("op");
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
                    List<Corso> listC = DAO.getCorsi();
                    String jsonDoc = json.toJson(listC, Docente.class);
                    out.print(jsonDoc);
            }
            out.flush();
            out.close();
        } else {
            throw new ServletException("Non hai i permessi di amministratore!");
        }
    }
}
