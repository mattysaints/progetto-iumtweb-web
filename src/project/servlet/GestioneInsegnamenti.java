package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.Corso;
import project.servlet.model.DAO;
import project.servlet.model.Docente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GestioneInsegnamenti", urlPatterns = {"/GestioneInsegnamenti"})
public class GestioneInsegnamenti extends HttpServlet {
   private static final Gson json = new Gson();
   @Override
   public void init() throws ServletException {
      super.init();
      DAO.registerDriver();
   }

   /**
    * visualizza la lista dei corsi insegnati da un docente, opure ne inserisce uno o lo elimina
    * @param request parametro "op" a scelta tra: "inserire", "eliminare", "visualizzare"; parametro "docente": oggetto json (nell'ultimo caso null)
    * @param response
    * @throws ServletException
    * @throws IOException
    */
   private void esegui(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String op = (String) request.getParameter("op");
      Docente docente = json.fromJson(request.getParameter("docente"), Docente.class);
      Corso corso = json.fromJson(request.getParameter("corso"), Corso.class);
      PrintWriter out = response.getWriter();
      response.setContentType("application/json");
      JsonObject rispostaJson = new JsonObject();
      JsonElement corsi = null;
      boolean corretto = true;
      switch (op) {
         case "inserire":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("") || corso== null || corso.getTitolo().equals("")) {
               corretto=false;
            } else {
               corretto = DAO.insertInsegnamento(docente,corso);
            }
            break;
         case "eliminare":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("") || corso== null || corso.getTitolo().equals("")) {
               corretto = false;
            } else {
               corretto = DAO.deleteInsegnamento(docente,corso);
            }
            break;
         case "visualizzare":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("")) {
               corretto = false;
            } else {
               corsi = json.toJsonTree(DAO.getCorsiInsegnatiDa(docente), new TypeToken<List<Corso>>() {}.getType());
            }
            break;
         default:
            throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
      }
      rispostaJson.addProperty("successo", corretto);
      rispostaJson.add("corsi", corsi);
      out.print(rispostaJson);
      out.flush();
      out.close();
   }
   //<editor-fold defaultstate="collapsed" desc="Metodi Http Servlet" >
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      esegui(request,response);
   }
   //</editor-fold>
}
