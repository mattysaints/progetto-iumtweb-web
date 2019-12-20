package project.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Logout", urlPatterns = {"/Logout"})
public class Logout extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null)
            throw new ServletException("Login non effettuato");

        session.invalidate();
        getServletContext().getRequestDispatcher("/loginPage.html").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="-   Metodi HttpServlet   -">
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    // </editor-fold>
}
