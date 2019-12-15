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

    @Override
    public void init() throws ServletException {
        super.init();
        context = getServletContext();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("sessionExpired", true);
            getServletContext().getRequestDispatcher("/loginPage.html").include(request, response);
        }

        RequestDispatcher rd = null;
        String redirect = request.getParameter("redirect");

        if (redirect != null) {
            switch (redirect) {
                case "prenota":
                    rd = context.getRequestDispatcher("loginPage.html");
                    break;
                case "gestioneDocenti":
                    rd = context.getRequestDispatcher("gestioneDocenti.html");
                    break;
                case "gestioneCorsi" :
                    rd= context.getRequestDispatcher("gestioneCorsi.html");
                    break;
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
