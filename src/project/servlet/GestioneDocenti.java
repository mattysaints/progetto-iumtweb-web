package project.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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

@WebServlet(name = "GestioneDocenti", urlPatterns = {"/GestioneDocenti"})
public class GestioneDocenti extends HttpServlet {
   private static final Gson json = new Gson();
   @Override
   public void init() throws ServletException {
      super.init();
      DAO.registerDriver();
   }
   /**
    *
    * @param request parametro "op" a scelta tra: "inserire", "eliminare", "visualizzare"; parametro "docente": oggetto json (nell'ultimo caso null)
    * @param response stampa nei primi due casi un bool, nell'ultimo l'oggetto json richiesto
    * @throws ServletException
    * @throws IOException
    */
   private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String op = (String) request.getParameter("op");
      Docente docente = json.fromJson(request.getParameter("docente"), Docente.class);
      PrintWriter out = response.getWriter();
      response.setContentType("application/json");
      JsonObject rispostaJson = new JsonObject();
      JsonElement docenti = null;
      boolean corretto = true;
      switch (op) {
         case "inserire":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("")) {
               corretto=false;
            } else {
               corretto = DAO.insertDocente(docente);
            }
            break;
         case "eliminare":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("")) {
               corretto = false;
            } else {
               corretto = DAO.deleteDocente(docente);
            }
            break;
         case "visualizzare":
            List<Docente> doc = DAO.getDocenti();
            docenti = json.toJsonTree(doc, new TypeToken<List<Docente>>(){}.getType());
            break;
         default:
            throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
      }
      rispostaJson.addProperty("successo", corretto);
      rispostaJson.add("docenti", docenti);
      out.print(rispostaJson);
      out.flush();
      out.close();
   }

   // <editor-fold defaultstate="collapsed" desc=" - Metodi HttpServlet - " >
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
   }
   // </editor-fold>
}
