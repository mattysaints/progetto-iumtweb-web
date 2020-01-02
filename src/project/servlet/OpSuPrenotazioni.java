package project.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.DAO;
import project.servlet.model.Prenotazione;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
   private static final Gson gson = new Gson();
   @Override
   public void init() throws ServletException {
      super.init();
      DAO.registerDriver();
   }

   /**
    * stampa un bool se le richieste sono state eseguite correttamente
    * @param request parametri: "ops" lista di op tra "prenotare", "disdire" ed "effettuare"; "prenotazioni" assegnare array di oggetti json corrispondenti su cui effettuare le operazioni
    * @param response
    * @throws ServletException
    * @throws IOException
    */
   private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // HttpSession session = request.getSession(false);
      // if (session == null)
      // return;

      String tmp = request.getParameter("ops");
      String tmp2 = request.getParameter("prenotazioni");
      List<String> ops = gson.fromJson(tmp, new TypeToken<List<String>>() {
      }.getType());
      List<Prenotazione> prenotazioni = gson.fromJson(tmp2, new TypeToken<List<Prenotazione>>() {
      }.getType());

      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      boolean correct = true;

      for (int i = 0; i < prenotazioni.size(); i++) {
         String op = ops.get(i);
         Prenotazione prenot = prenotazioni.get(i);
         switch (op) {
            case "prenotare":
               correct &= DAO.insertPrenotazione(prenot);
               break;
            case "disdire":
               correct &= DAO.deletePrenotazione(prenot);
               break;
            case "effettuare":
               correct &= DAO.setPrenotazioneEffettuata(prenot);
               break;
            default:
               throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
         }
         out.print(correct);
         out.flush();
         out.close();
      }
   }


   // <editor-fold defaultstate="collapsed" desc="- Metodi HttpServlet -">
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
   }
   //   </editor-fold>

}
