package project.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "OpSuPrenotazioni")
public class OpSuPrenotazioni extends HttpServlet {
   /**
    * Si possono fare 3 operazioni:
    * - prenotare
    * - disdire
    * - effettuare
    * una prenotazione.
    *
    * Questo verrà discriminato da una variabile nella request (op)
    *
    */


   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}
