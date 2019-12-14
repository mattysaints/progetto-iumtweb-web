package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
/**
 * Si possono fare 3 operazioni:
 * - prenotare
 * - disdire
 * - effettuare
 * una ripetizione.
 * Questo verrà discriminato da una variabile nella request (op).
 *
 */
@WebServlet(name = "OpSuPrenotazioni", urlPatterns = {"/OpSuPrenotazioni"})
public class OpSuPrenotazioni extends HttpServlet {
   private static final Gson json = new Gson();
   @Override
   public void init() throws ServletException {
      super.init();
      DAO.registerDriver();
   }

   /**
    * stampa un bool se la richiesta è stata eseguita correttamente
    * @param request parametri: "op" assegnare uno tra "prenotare", "disdire" ed "effettuare"; "prenotazione" assegnare oggetto json corrispondente
    * @param response
    * @throws ServletException
    * @throws IOException
    */
   private void esegui(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      if (session == null) {
         RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("Login");
         requestDispatcher.include(request, response);
      }
      String op = (String) request.getAttribute("op");
      Prenotazione prenot = json.fromJson(request.getParameter("prenotazione"), Prenotazione.class);
      PrintWriter out = response.getWriter();
      boolean correct = false;
      switch (op) {
         case "prenotare":
            correct = DAO.insertPrenotazione(prenot);
            break;
         case "disdire":
            correct = DAO.deletePrenotazione(prenot);
            break;
         case "effettuare":
            correct = DAO.setPrenotazioneEffettuata(prenot);
            break;
         default:
            throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
      }
      out.print(correct);
      out.flush();
      out.close();
   }

   // <editor-fold defaultstate="collapsed" desc="- Metodi HttpServlet -">
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }    //   </editor-fold>



}
