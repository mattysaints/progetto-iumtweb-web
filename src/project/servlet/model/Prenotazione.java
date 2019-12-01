package project.servlet.model;

import java.time.DayOfWeek;
import java.util.UUID;

public class Prenotazione {

    private int id;
    private Docente docente;
    private Corso corso;
    private Utente utente;
    private Slot slot;
    private Giorno giorno;
    private Stato stato;

    public Prenotazione(Docente docente, Corso corso, Utente utente, Slot slot, Giorno giorno, Stato stato) {
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

    public Slot getSlot() { return slot; }

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
