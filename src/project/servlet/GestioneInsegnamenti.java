package project.servlet;

import com.google.gson.Gson;
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
      switch (op) {
         case "inserire":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("") || corso== null || corso.getTitolo().equals("")) {
               out.print(false);
            } else {
               boolean corretto = DAO.insertInsegnamento(docente,corso);
               out.print(corretto);
            }
            break;
         case "eliminare":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("") || corso== null || corso.getTitolo().equals("")) {
               out.print(false);
            } else {
               boolean corretto = DAO.deleteInsegnamento(docente,corso);
               out.print(corretto);
            }
            break;
         case "visualizzare":
            if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("")) {
               out.print(false);
            } else {
               response.setContentType("application/json");
               List<Corso> corsi = DAO.getCorsiInsegnatiDa(docente);
               String jsonDoc = json.toJson(corsi, new TypeToken<List<Corso>>() {
               }.getType());
               out.print(jsonDoc);
            }
            break;
         default:
            throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
      }
      out.flush();
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
