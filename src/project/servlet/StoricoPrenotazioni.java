package project.servlet;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import project.servlet.model.DAO;
import project.servlet.model.Prenotazione;
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
import java.util.List;

@WebServlet(name = "StoricoPrenotazioni", urlPatterns = {"/progetto_ium_tweb2/StoricoPrenotazioni"})
public class StoricoPrenotazioni extends HttpServlet {
   private static final Gson json = new Gson();
   @Override
   public void init() throws ServletException {
      super.init();
      DAO.registerDriver();
   }
   /**
    * si possono effettuare due tipi di richieste:
    * - storico generale (di tutti gli utenti)
    * - storico di un utente in particolare
    *
    * Questo verrà discriminato da un campo nella richiesta (utente: se null si intende la richiesta di storico generale)
    */
   /**
    *
    * @param request parametro: "utente" in formato json, se null si intende lo storico generale
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
      Utente u = json.fromJson((JsonElement)request.getAttribute("utente"), Utente.class);
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      List<Prenotazione> storico;
      if(u==null){
         //restituisco storico generale
          storico = DAO.getStoricoPrenotazioni();

      } else {
         //restituisco storico utente
         storico = DAO.listRip_Utente(u);
      }
      String stor = json.toJson(storico);
      out.print(stor);
      out.flush();
      out.close();
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }


}
