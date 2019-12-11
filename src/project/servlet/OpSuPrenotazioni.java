package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import project.servlet.model.DAO;
import project.servlet.model.Prenotazione;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "OpSuPrenotazioni", urlPatterns = {"/progetto_ium_tweb2/OpSuPrenotazioni"})
public class OpSuPrenotazioni extends HttpServlet {
   /**
    * Si possono fare 3 operazioni:
    * - prenotare
    * - disdire
    * - effettuare
    * una ripetizione.
    * Questo verrà discriminato da una variabile nella request (op).
    *
    *
    */

   /**
    *
    * @param request parametri: "op" assegnare uno tra "prenotare", "disdire" ed "effettuare"; "prenotazione" assegnare oggetto json corrispondente
    * @param response
    * @throws ServletException
    * @throws IOException
    */
   private void esegui(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String op = (String) request.getAttribute("op");
      Gson json = new Gson();
      Prenotazione prenot = json.fromJson((JsonElement) request.getAttribute("prenotazione"), Prenotazione.class);
      DAO.registerDriver();
      PrintWriter out = response.getWriter();
      boolean correct = false;
      switch (op) {
         case "prenotare":
            correct = DAO.addRip(prenot);
            break;
         case "disdire":
            correct = DAO.disdireRip(prenot);
            break;
         case "effettuare":
            correct = DAO.makeRip(prenot);
            break;
         default:
            throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
      }
      out.print(correct);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
}
