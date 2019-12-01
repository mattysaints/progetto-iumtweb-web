package project.servlet.model;

import java.util.UUID;

public class Docente {
    private int id;
    private String nome;
    private String cognome;

    public Docente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = Integer.parseInt(UUID.randomUUID().toString());
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return "Docente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}
