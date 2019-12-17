package project.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import project.servlet.model.DAO;
import project.servlet.model.Docente;

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
      HttpSession session = request.getSession(false);
      if (session == null) {
         RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/loginPage.html");
         requestDispatcher.include(request, response);
      }
      assert session != null;
      boolean admin = (boolean) session.getAttribute("admin");
      if(admin) {
         String op = (String) request.getAttribute("op");
         Docente docente = json.fromJson(request.getParameter("docente"), Docente.class);
         PrintWriter out = response.getWriter();
         switch (op) {
            case "inserire":
               if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("")) {
                  out.print(false);
               } else {
                  boolean corretto = DAO.insertDocente(docente);
                  out.print(corretto);
               }
               break;
            case "eliminare":
               if(docente==null || docente.getCognome().equals("") || docente.getNome().equals("")) {
                  out.print(false);
               } else {
                  boolean corretto = DAO.deleteDocente(docente);
                  out.print(corretto);
               }
               break;
            case "visualizzare":
               response.setContentType("application/json");
               List<Docente> doc = DAO.getDocenti();
               String jsonDoc = json.toJson(doc, new TypeToken<List<Docente>>(){}.getType());
               out.print(jsonDoc);
               break;
            default:
               throw new ServletException("L'operazione richiesta non è tra quelle servite (scegliere tra 'prenotare', 'disdire', 'effettuare')");
         }
         out.flush();
         out.close();
      } else {
         //non hai i permessi di admin
         throw new ServletException("Non hai i permessi di amministratore!");
      }


/*
      COSI FUNZIONA: ho provato con meno roba per semplificare

      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      List<Docente> doc = DAO.getDocenti();
      String jsonDoc = json.toJson(doc, new TypeToken<List<Docente>>(){}.getType());
      out.print(jsonDoc);

 */

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
