package project.servlet.model;

import java.sql.*;
import java.util.ArrayList;

public class DAO {

    private final String url = "jdbc:mysql://localhost:3306/";
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
            PreparedStatement prepStat = conn1.prepareStatement("DELETE FROM corso WHERE titolo = ?;");
            prepStat.setString(1, corso.getTitolo());
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
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO Docente VALUES (?,?);");
            prepStat.setString(1, docente.getNome());
            prepStat.setString(2, docente.getCognome());
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
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO insegnamento VALUES (?,?);");
            prepStat.setInt(1, findIdDoc(docente));
            prepStat.setString(2, corso.getTitolo());
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
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM insegnamento WHERE docente = ? and corso = ?;");
            prepStat.setInt(1, findIdDoc(docente));
            prepStat.setString(2, corso.getTitolo());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int findIdDoc(Docente docente){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT id from Docente where nome = "+docente.getNome()+" and cognome ="+docente.getCognome()+";");
            if(rs.next() == false)
                System.out.println("Docente non trovato nel database");
            else
                return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Prenotazione> ripetizioniDisp (){
        Connection conn1 = null;
        ArrayList<Prenotazione> ripetizioni_disp = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
                String QUERY = "SELECT DISTINCT d.nome, d.cognome, i.corso, g.giorno, s.ora\n" +
                        "FROM ripetizioni.insegnamento i JOIN ripetizioni.docente d ON i.docente=d.id, slot s, giorno g\n" +
                        "WHERE (i.docente, i.corso, s.ora, g.giorno) NOT IN (\n" +
                        "    SELECT docente, corso, ora, giorno\n" +
                        "    FROM ripetizioni.prenotazione\n" +
                        "    WHERE stato='attiva'\n" +
                        "    );";
                Statement st = conn1.createStatement();
                ResultSet rs = st.executeQuery(QUERY);
                while (rs.next()){
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    String TitoloC = rs.getString("corso");
                    Docente docente = new Docente(nome,cognome);
                    Corso corso = new Corso(TitoloC);
                    Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                    Slot ora = Slot.fromInt(rs.getInt("ora"));
                    ripetizioni_disp.add(new Prenotazione(docente,corso,null,ora,giorno,Stato.ATTIVA));
                }
                return ripetizioni_disp;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ripetizioni_disp;
    }

    public boolean cancelRip(Prenotazione prenotazione){ //todo cancellare/disdire ripetizione
        return true;
    }

    public boolean addRip(Prenotazione prenotazione){ //todo aggiungere/effettuata ripetizione
        return true;
    }



}
