package project.servlet.model;

import java.sql.*;

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

    public boolean containsUtente(Utente utente) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT FROM utente WHERE account = "+utente.getAccount()+";");
            if(rs.next() == false){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertCo(Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO corso VALUES (?);");
            prepStat.setString(1, corso.getTitolo());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeCo(Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM corso WHERE titoloC = ?;");
            prepStat.setString(1, corso.getTitoloC());
            prepStat.executeUpdate();
            System.out.println(corso + " eliminato correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertDoc(Docente docente) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO Docente VALUES (?,?,?);");
            prepStat.setString(1, docente.getNome());
            prepStat.setString(2, docente.getCognome());
            prepStat.setInt(3,docente.getId());
            prepStat.executeUpdate();
            System.out.println("Aggiunto Docente alla Lista");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeDoc(Docente docente) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM docente WHERE nome = "+docente.getNome()+" and cognome="+docente.getCognome()+";");
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertInse(Docente docente, Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO afferenza VALUES (?,?);");
            prepStat.setInt(1, docente.getId());
            prepStat.setString(2, corso.getTitoloC());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean removeInse(Docente docente, Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM afferenza WHERE idDoc = ? and titoloC = ?;;");
            prepStat.setInt(1, docente.getId());
            prepStat.setString(2, corso.getTitoloC());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }





}
