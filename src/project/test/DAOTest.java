package project.test;

import project.servlet.model.*;

public class DAOTest {
    public static void main(String[] args) {
        DAO.registerDriver();
        Utente utente = new Utente("Davide","dado1",false);
        System.out.println(DAO.contieneUtente(utente));
        Docente docente = new Docente("Nome","Cognome");
        System.out.println(DAO.inserisciDoc(docente));
        //System.out.println(DAO.rimuoviDoc(docente));
        Corso corso = new Corso("Corso");
        System.out.println(DAO.inserisciCo(corso));
        //System.out.println(DAO.rimuoviCo(corso));
        System.out.println(DAO.trovaIdDoc(docente));
        System.out.println(DAO.inserisciInse(docente,corso));
        //System.out.println(DAO.rimuoviInse(docente,corso));
        //System.out.println(DAO.ripetizioniDisp());
        String id_doc = "385cbfac-0cbd-4c08-9177-67409007b201";
        System.out.println(DAO.trovaDoc_byId(id_doc);
    }
}
