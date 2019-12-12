package project.servlet.model;

import java.util.Objects;

public class Utente {

    private String account;
    private String password;
    private boolean admin;

    public Utente(String account, String password, Boolean admin) {
        this.account = account;
        this.password = password;
        this.admin = admin;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", admin='" + admin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Utente utente = (Utente) o;
        return admin == utente.admin &&
              Objects.equals(account, utente.account) &&
              Objects.equals(password, utente.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account);
    }
}
