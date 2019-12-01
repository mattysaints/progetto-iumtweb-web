package project.servlet.model;

import javax.print.Doc;

public class Insegnamento {

    private Docente doc;
    private Corso corso;

    public Insegnamento(Docente doc, Corso corso) {
        this.doc = doc;
        this.corso = corso;
    }

    public Docente getDoc() {
        return doc;
    }

    public Corso getCorso() {
        return corso;
    }
}
