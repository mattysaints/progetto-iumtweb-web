package project.test;

import project.servlet.model.*;

public class DAOTest {
    public static void main(String[] args) {
        DAO.registerDriver();
        /*Utente utente = new Utente("Davide","dado1",false);
        System.out.println(DAO.queryUtente(utente));
        Docente docente = new Docente("Nome","Cognome");
        System.out.println(DAO.insertDocente(docente));
        //System.out.println(DAO.rimuoviDoc(docente));
        Corso corso = new Corso("Corso");
        System.out.println(DAO.insertCorso(corso));
        //System.out.println(DAO.rimuoviCo(corso));
        System.out.println(DAO.insertInsegnamento(docente, corso));
        //System.out.println(DAO.rimuoviInse(docente,corso));
        //System.out.println(DAO.ripetizioniDisp());
        System.out.println(DAO.getStoricoPrenotazioni());
        System.out.println(DAO.listRipStato("effettuata"));*/
        System.out.println(DAO.getCorsi());
    }
}
