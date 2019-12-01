package project.servlet.model;

import java.util.ArrayList;
import java.util.List;

public class Utente {

    private String account;
    private String password;
    private String ruolo;

    public Utente(String account, String password, String ruolo) {
        this.account = account;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getRuolo() {
        return ruolo;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Utente o = (Utente)obj;
        return this.account.equals(o.getAccount()) && this.password.equals(o.getPassword()) && this.ruolo.equals(o.getRuolo());
    }

}
