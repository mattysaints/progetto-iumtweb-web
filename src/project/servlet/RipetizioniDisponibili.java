package project.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.DAO;
import project.servlet.model.Prenotazione;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Servlet che restituisce sullo stream di output la lista delle ripetizioni disponibili in formato Json.
 * NOTE:
 * - Non richiede parametri
 * - Se l'utente non è autenticato inoltra la richiesta alla servlet di Login
 */
@WebServlet(name = "RipetizioniDisponibili", urlPatterns = {"/progetto_ium_tweb2/RipetizioniDisponibili"})
public class RipetizioniDisponibili extends HttpServlet {

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
        if (session == null) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("Login");
            requestDispatcher.include(request, response);
        }

        List<Prenotazione> ripetizioniDisp = DAO.ripetizioniDisp();
        Gson gson = new Gson();
        Type type = new TypeToken<>(){}.getType();
        String jsonObject = gson.toJson(ripetizioniDisp, type);

        PrintWriter out = response.getWriter();
        out.println(jsonObject);
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
