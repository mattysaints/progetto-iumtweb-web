package project.servlet;

package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import project.servlet.model.DAO;
import project.servlet.model.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "StoricoPrenotazioni")
public class StoricoPrenotazioni extends HttpServlet {

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
      Gson json = new Gson();
      Utente u = json.fromJson((JsonElement)request.getAttribute("utente"), Utente.class);
      DAO.registerDriver();
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      if(u==null){
         out.
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
}
