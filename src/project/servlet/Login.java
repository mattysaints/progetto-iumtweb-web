package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;
import project.servlet.model.DAO;
import project.servlet.model.Utente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Controlla se un utente è registrato e in caso positivo inizializza la sessione
 *
 * Parametri della request:
 *  - username: username dell'utente che ha fatto il login
 *  - password: password utente
 *
 *  Stampa (JSON):
 *   - failure se username e password non corrispondono a nessun utente registrato
 *   - success l'utente è registrato
 *
 *  Parametri della sessione:
 *  - username: username dell'utente associato alla sessione
 *  - admin: true se l'utente è admin
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    private static final Gson gson = new Gson();
    /**
     * Inizializza la servlet
     *
     * @throws ServletException: override
     */
    @Override
    public void init() throws ServletException {
        super.init();
        DAO.registerDriver();
    }

    /**
     * Controlla se l'utente è registrato
     * - in caso negativo stampa null
     * - in caso positivo salva la sessione utente (utente, isAdmin) e la stampa l'id della sessione
     *
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @throws IOException: override
     * @throws ServletException: override
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Gson gson = new Gson();

        Utente utente = DAO.queryUtente(new Utente(username, password, null));
        if (utente == null)
            out.print(gson.toJson("failure"));
        else {
            HttpSession session = request.getSession();
            session.setAttribute("username", utente.getAccount());
            session.setAttribute("admin", utente.isAdmin());
            out.print(gson.toJson("success"));
        }
        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc=" - Metodi HttpServlet - " >

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    // </editor-fold>
}
