package project.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet che reindirizza le pagine
 *
 * Parametro della request 'redirect': contiene l'identificativo della pagina da raggiungere
 */
@WebServlet(name = "Redirect", urlPatterns = {"/Redirect"})
public class Redirect extends HttpServlet {
    private ServletContext context;
    protected static final boolean DEBUG = false;

    @Override
    public void init() throws ServletException {
        super.init();
        context = getServletContext();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String redirect = request.getParameter("redirect");
        RequestDispatcher rd = null;
        Boolean admin=true;
        if(!DEBUG) {
            if (session == null) { //sessione scaduta
                request.setAttribute("sessionExpired", true);
                rd = context.getRequestDispatcher("/loginPage.html");
                rd.forward(request, response); //non si blocca qua
            }
            assert session != null;
            admin = (Boolean) session.getAttribute("admin");
        } else { //DEBUG
            session = request.getSession();
            session.setAttribute("admin", true);
        }

        if (redirect != null) {
            switch (redirect) {
                case "homepage":
                    rd = context.getRequestDispatcher("/homepage.html");
                    break;
                case "gestioneDocenti":
                    if(admin) {
                        rd = context.getRequestDispatcher("/gestioneDocenti.html");
                    } else {
                        //non hai i permessi di admin
                        throw new ServletException("Non hai i permessi di amministratore!");
                    }
                    break;
                case "gestioneCorsi" :
                    if(admin) {
                        rd = context.getRequestDispatcher("/gestioneCorsi.html");
                    } else {
                        //non hai i permessi di admin
                        throw new ServletException("Non hai i permessi di amministratore!");
                    }
                    break;
                case "storicoGenerale":
                    if(admin) {
                        rd = context.getRequestDispatcher("/storicoGenerale.html");
                    } else {
                        //non hai i permessi di admin
                        throw new ServletException("Non hai i permessi di amministratore!");
                    }
                    break;
                case "gestioneStorico":
                    rd = context.getRequestDispatcher("/gestioneStorico.html");
                    break;
                case "ripetizioniDisponibili":
                    rd = context.getRequestDispatcher("/ripetizioniDisponibili.html");
                default:
            }
            if (rd != null)
                rd.forward(request, response);
        }

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
