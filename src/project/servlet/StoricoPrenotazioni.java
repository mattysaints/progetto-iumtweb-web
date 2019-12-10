package project.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StoricoPrenotazioni")
public class StoricoPrenotazioni extends HttpServlet {

   /**
    * si possono effettuare due tipi di richieste:
    * - storico generale (di tutti gli utenti)
    * - storico di un utente in particolare
    *
    * Questo verrà discriminato da un campo nella richiesta (utente: se null si intende la richiesta di storico generale)
    */

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
