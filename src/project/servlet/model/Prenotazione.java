package project.servlet.model;

import java.time.DayOfWeek;

enum Giorno {
    Lun,
    Mar,
    Merc,
    Giovedì,
    Venerdì
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

public class Prenotazione {

    private int idDoc;
    private String idCorso;
    private String idUtente;
    private SLOT slot;
    private Giorno giorno;

    public Prenotazione(int idDoc, String idCorso, String idUtente, SLOT slot, Giorno giorno) {
        this.idDoc = idDoc;
        this.idCorso = idCorso;
        this.idUtente = idUtente;
        this.slot = slot;
        this.giorno = giorno;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "idDoc=" + idDoc +
                ", idCorso='" + idCorso + '\'' +
                ", idUtente='" + idUtente + '\'' +
                ", slot=" + slot +
                ", giorno=" + giorno +
                '}';
    }
}
