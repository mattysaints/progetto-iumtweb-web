package project.servlet.model;

public class Corso {

    private String titoloC;

    public Corso(String titoloC) {
        this.titoloC = titoloC;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "titoloC='" + titoloC + '\'' +
                '}';
    }

    public String getTitoloC() {
        return titoloC;
    }
}