package project.servlet;

import com.google.gson.Gson;
import project.servlet.model.DAO;
import project.servlet.model.Utente;

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
 *  Stampa:
 *   - null se l'utente non è registrato
 *   -l'oggetto session in formato Json
 *
 *  Parametri della sessione:
 *  - username: username dell'utente associato alla sessione
 *  - admin: true se l'utente è admin
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    /**
     * Inizializza la servlet
     *
     * @throws ServletException
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
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Utente utente = new DAO().queryUtente(new Utente(username, password, null));
        if (utente == null) {
            session.invalidate();
            out.println((String) null);
        } else {
            session.setAttribute("username", utente.getAccount());
            session.setAttribute("admin", utente.isAdmin());
            Gson gson = new Gson();
            String json = gson.toJson(session);
            out.println(json);
        }
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
