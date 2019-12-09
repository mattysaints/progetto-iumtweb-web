package project.servlet.model;

import java.util.ArrayList;
import java.util.List;

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

}
