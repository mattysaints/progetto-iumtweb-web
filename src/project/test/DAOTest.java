package project.test;

import project.servlet.model.Corso;
import project.servlet.model.DAO;
import project.servlet.model.Docente;
import project.servlet.model.Utente;

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
        System.out.println(DAO.listRipStato("effettuata"));
        System.out.println(DAO.getCorsi());*/

        Docente docente = new Docente("Mario","Rossi");
        System.out.println(DAO.getCorsiInsegnatiDa(docente));
        Corso corso = new Corso("Matematica");
        System.out.println(DAO.getDocentiCheInsegnano(corso));
        Utente utente = new Utente("Alex97","alex#",false);

        /*Docente docente = new Docente("Mario","Rossi");
        Corso corso = new Corso("Matematica");
        Utente utente = new Utente("Alex97","alex#",false);
        Slot slot = SLOT3;
        Giorno giorno = Giorno.MAR;
        Prenotazione prenotazione = new Prenotazione(docente,corso,utente,slot,giorno,Stato.attiva);
        System.out.println(DAO.deletePrenotazione(prenotazione));*/

    }
}
