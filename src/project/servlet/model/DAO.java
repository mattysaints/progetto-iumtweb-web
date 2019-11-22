package project.servlet.model;

import java.sql.*;
import java.util.ArrayList;

public class DAO {
    private final String url = "jdbc:mysql://localhost/test";
    private final String user = "root";
    private final String password = "";
    private int idDoc;

    public void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public void inizializzaUtente() {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            PreparedStatement stmt = conn1.prepareStatement("CREATE TABLE `test`.`utente` ( `account` VARCHAR(20) NOT NULL, primary key(account), unique(account), `password` VARCHAR(20) NOT NULL , `ruolo` VARCHAR(20) NOT NULL ) ENGINE = InnoDB");
            stmt.executeUpdate();
            System.out.println("Table Utente Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inizializzaCorso() {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            PreparedStatement stmt = conn1.prepareStatement("CREATE TABLE `test`.`corso` ( `TitoloC` VARCHAR(15) NOT NULL , primary key(TitoloC), unique(TitoloC)) ENGINE = InnoDB;");
            stmt.executeUpdate();
            System.out.println("Table Utente Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inizializzaDocente() {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            PreparedStatement stmt = conn1.prepareStatement("CREATE TABLE `test`.`docente` ( `nome` VARCHAR (20) NOT NULL , `cognome` VARCHAR(20) NOT NULL , `idDoc` INT(6) NOT NULL, primary key(idDoc), unique(nome,cognome)) ENGINE = InnoDB;");
            stmt.executeUpdate();
            System.out.println("Table Utente Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inizializzaAfferenza() {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            PreparedStatement stmt = conn1.prepareStatement("CREATE TABLE `test`.`afferenza` ( `idDoc` INT(6) NOT NULL REFERENCES docente(idDoc), `titoloC` VARCHAR(20) NOT NULL REFERENCES corso(titoloC)) ENGINE = InnoDB;");
            stmt.executeUpdate();
            System.out.println("Table Utente Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inizializzaPrenotazione() {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            PreparedStatement stmt = conn1.prepareStatement("CREATE TABLE `test`.`prenotazione` ( `idDoc` INT(6) NOT NULL references docente(idDoc), `idCorso` VARCHAR(20) NOT NULL references corso(TitoloC), `idUtente` VARCHAR(20) NOT NULL references utente(account), `Ora_Inizio` INT(2) NOT NULL , `Ora_Fine` INT(2) NOT NULL , `Giorno` VARCHAR(15) NOT NULL, primary key(idDoc,idUtente,Ora_Inizio,Giorno)) ENGINE = InnoDB;");
            stmt.executeUpdate();
            System.out.println("Table Utente Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeTC(String corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM corso WHERE titoloC = ?;");
            prepStat.setString(1, corso);
            prepStat.executeUpdate();
            System.out.println(corso + " eliminato correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertTC(String corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO corso VALUES (?);");
            prepStat.setString(1, corso);
            prepStat.executeUpdate();
            System.out.println("Aggiunto Corso alla Lista");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean modifyTC(String corso, String newCorso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("UPDATE Corso SET TitoloC = ? WHERE titoloC = ?;");
            prepStat.setString(1, newCorso);
            prepStat.setString(2, corso);
            prepStat.executeUpdate();
            System.out.println("Titolo Corso " + corso + " in " + newCorso);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertDoc(String docN, String docC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO Docente VALUES (?,?,?);");
            prepStat.setString(1, docN);
            prepStat.setString(2, docC);
            idDoc = MaxInt() + 1;
            prepStat.setInt(3, idDoc);
            prepStat.executeUpdate();
            System.out.println("Aggiunto Docente alla Lista");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean modifyDoc(String docN, String docC, String newDocN, String newDocC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("UPDATE docente SET nome=?, cognome=? WHERE nome=? and cognome=?;");
            prepStat.setString(1, newDocN);
            prepStat.setString(2, newDocC);
            prepStat.setString(3, docN);
            prepStat.setString(4, docC);
            prepStat.executeUpdate();
            System.out.println("Moficato Docente " + docN + docC + " in " + newDocN + " " + newDocC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeDoc(String docN, String docC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM docente WHERE nome = ? and cognome = ?;");
            prepStat.setString(1, docN);
            prepStat.setString(2, docC);
            prepStat.executeUpdate();
            System.out.println("Rimosso Docente " + docN + " " + docC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean modifyAff(int idDoc, String titoloC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("UPDATE affernza SET titoloC = ? WHERE idDoc = ?;");
            prepStat.setString(1, titoloC);
            prepStat.setInt(2, idDoc);
            prepStat.executeUpdate();
            System.out.println("Cambiato corso di" + idDoc + " in " + titoloC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertAff(Docente doc, String titoloC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            String nomeDoc = doc.getNome();
            int idDoc = 0;
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("select distinct(idDoc) from docente where nome = '" + nomeDoc + "';");
            while (rs.next()) {
                idDoc = rs.getInt("idDoc");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO afferenza VALUES (?,?);");
            prepStat.setInt(1, idDoc);
            prepStat.setString(2, titoloC);
            prepStat.executeUpdate();
            System.out.println("Inserita afferenza : " + nomeDoc + "-" + titoloC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeAff(Docente doc, String titoloC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            String nomeDoc = doc.getNome();
            int idDoc = 0;
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("select distinct(idDoc) from docente where nome = '" + nomeDoc + "';");
            while (rs.next()) {
                idDoc = rs.getInt("idDoc");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM afferenza WHERE idDoc = ? and titoloC = ?;");
            prepStat.setString(2, titoloC);
            prepStat.setInt(1, idDoc);
            prepStat.executeUpdate();
            System.out.println("Afferenza di " + idDoc + " a " + titoloC + " è stata eliminata");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertRip(Docente doc, String titoloC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            String nomeDoc = doc.getNome();
            int idDoc = 0;
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("select distinct(idDoc) from docente where nome = '" + nomeDoc + "';");
            while (rs.next()) {
                idDoc = rs.getInt("idDoc");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM afferenza WHERE idDoc = ? and titoloC = ?;");
            prepStat.setInt(1, idDoc);
            prepStat.setString(2, titoloC);
            prepStat.executeUpdate();
            System.out.println("Ripetizione Aggiunta Correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeRip(Docente doc, String titoloC) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            String nomeDoc = doc.getNome();
            int idDoc = 0;
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("select distinct(idDoc) from docente where nome = '" + nomeDoc + "';");
            while (rs.next()) {
                idDoc = rs.getInt("idDoc");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM ripetizione WHERE idDoc = ? and titoloC = ?;");
            prepStat.setInt(1, idDoc);
            prepStat.setString(2, titoloC);
            prepStat.executeUpdate();
            System.out.println("Ripetizione Rimossa Correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertPren(Docente doc, String titoloC, String account, int beginTime, int endTime, String date) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            String nomeDoc = doc.getNome();
            int idDoc = 0;
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("select distinct(idDoc) from docente where nome = '" + nomeDoc + "';");
            while (rs.next()) {
                idDoc = rs.getInt("idDoc");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO prenotazione VALUES (?,?,?,?,?,?);");
            prepStat.setInt(1, idDoc);
            prepStat.setString(2, titoloC);
            prepStat.setString(3, account);
            prepStat.setInt(4, beginTime);
            prepStat.setInt(5, endTime);
            prepStat.setString(6, date);
            prepStat.executeUpdate();
            System.out.println("Prenotazione Aggiunta Correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removePren(Docente doc, String account, int beginTime, String date) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            String nomeDoc = doc.getNome();
            int idDoc = 0;
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("select distinct(idDoc) from docente where nome = '" + nomeDoc + "';");
            while (rs.next()) {
                idDoc = rs.getInt("idDoc");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM ripetizione WHERE idDoc = ? and idUtente = ? and Ora_Inizio = ? and Giorno = ?;");
            prepStat.setString(2, account);
            prepStat.setInt(1, idDoc);
            prepStat.setInt(3, beginTime);
            prepStat.setString(4, date);
            prepStat.executeUpdate();
            System.out.println("Prenotazione rimossa correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<Prenotazione> queryPren() {
        Connection conn1 = null;
        ArrayList<Prenotazione> lista = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM prenotazione");
            while (rs.next()) {
                int idDoc = rs.getInt("idDoc");
                String idCorso = rs.getString("idCorso");
                String idUtente = rs.getString("idUtente");
                int Ora_Inizio = rs.getInt("Ora_Inizio");
                int Ora_Fine = rs.getInt("Ora_Fine");
                String Giorno = rs.getString("Giorno");
                lista.add(new Prenotazione(idDoc,idCorso,idUtente,Ora_Inizio,Ora_Fine,Giorno));
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Utente> queryUtente() {
        Connection conn1 = null;
        ArrayList<Utente> lista = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `utente`");
            while (rs.next()) {
                String account = rs.getString("account");
                String pass = rs.getString("password");
                String ruolo = rs.getString("ruolo");
                lista.add(new Utente(account,pass,ruolo));
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public int MaxInt() {
        Connection conn1 = null;
        int maxIdDoc = 0;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(idDoc) from Docente");
            while (rs.next()) {
                maxIdDoc = rs.getInt(1);
            }
            return maxIdDoc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxIdDoc;
    }
}
