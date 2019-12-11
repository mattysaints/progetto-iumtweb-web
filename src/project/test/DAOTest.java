package project.test;

import project.servlet.model.Corso;
import project.servlet.model.DAO;
import project.servlet.model.Docente;
import project.servlet.model.Utente;

public class DAOTest {
    public static void main(String[] args) {
        DAO dao = new DAO();
        dao.registraDriver();
        //System.out.println(dao.contieneUtente(new Utente("Alex97","alex#",false)));
        System.out.println(dao.inserisciCo(new Corso("ProvaIns")));
        System.out.println(dao.inserisciInse(new Docente("Luca","Neri"),new Corso("ProvaIns")));
    }
}
