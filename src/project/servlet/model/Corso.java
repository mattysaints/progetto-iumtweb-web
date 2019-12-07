package project.servlet.model;

public class Corso {

    private String titolo;

    public Corso(String titolo) {
        this.titolo = titolo;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "titoloC='" + titolo + '\'' +
                '}';
    }

    public String getTitolo() {
        return titolo;
    }
}