package project.servlet.model;

import java.time.DayOfWeek;
import java.util.UUID;

enum Giorno {
    LUN,
    MAR,
    MERC,
    GIO,
    VEN
}

enum SLOT{
    SLOT1(1), //15-16
    SLOT2(2), //16-17
    SLOT3(3), //17-18
    SLOT4(4); //18-19

    private final int value;

    SLOT(final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}

enum Stato {
    EFFETTUATA,
    ATTIVA,
    DISDETTA
}

public class Prenotazione {

    private int id;
    private Docente docente;
    private Corso corso;
    private Utente utente;
    private SLOT slot;
    private Giorno giorno;
    private Stato stato;

    public Prenotazione(Docente docente, Corso corso, Utente utente, SLOT slot, Giorno giorno, Stato stato) {
        this.docente = docente;
        this.corso = corso;
        this.utente = utente;
        this.slot = slot;
        this.giorno = giorno;
        this.stato = stato;
        this.id = Integer.parseInt(UUID.randomUUID().toString());
    }

    public Docente getDocente() { return docente; }

    public Corso getCorso() { return corso; }

    public Utente getUtente() { return utente; }

    public SLOT getSlot() { return slot; }

    public Giorno getGiorno() { return giorno; }

    public Stato getStato() { return stato; }

    public int getId() { return id; }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "docente=" + docente +
                ", corso=" + corso +
                ", utente=" + utente +
                ", slot=" + slot +
                ", giorno=" + giorno +
                ", stato=" + stato +
                '}';
    }
}
