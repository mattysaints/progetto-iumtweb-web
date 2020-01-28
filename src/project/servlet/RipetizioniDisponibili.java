package project.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.DAO;
import project.servlet.model.Prenotazione;
import project.servlet.model.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet che restituisce sullo stream di output la lista delle ripetizioni disponibili in formato Json.
 * NOTE:
 * - Non richiede parametri
 * - Se l'utente non è autenticato inoltra la richiesta alla servlet di Login
 */
@WebServlet(name = "RipetizioniDisponibili", urlPatterns = {"/RipetizioniDisponibili"})
public class RipetizioniDisponibili extends HttpServlet {
    private static final Gson gson = new Gson();
    @Override
    public void init() throws ServletException {
        super.init();
        DAO.registerDriver();
    }

    /**
     * Stampa sullo stream di output la lista di ripetizioni prenotabili in formato Json
     *
     * NOTE:
     * - la servlet inoltra la richiesta alla servlet di login se la sessione non esiste
     * - la conversione in Json tramite la classe Gson sfrutta la classe Type per non perdere il tipo generico della lista
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String account = request.getParameter("account");
        List<Prenotazione> disponibili = DAO.getRipetizioniDisponibili(new Utente(account, null, null));
        Type type = new TypeToken<List<Prenotazione>>(){}.getType();
        String jsonObject = gson.toJson(disponibili, type);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
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
