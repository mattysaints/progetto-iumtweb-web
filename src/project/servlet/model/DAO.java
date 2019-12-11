package project.servlet.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAO {

    private final static String url = "jdbc:mysql://localhost:3306/ripetizioni";
    private final static String user = "root";
    private final static String password = "";

    public void registraDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public static  Utente queryUtente(Utente utente) {
        Connection conn1 = null;
        Utente result = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT FROM utente WHERE account = "+utente.getAccount()+";");
            if(rs.next())
                result = new Utente(rs.getString("account"), rs.getString("password"), rs.getBoolean("admin"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean inserisciCorso(Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO corso VALUES (?);");
            prepStat.setString(1, corso.getTitolo());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean rimuoviCorso(Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStat = conn1.prepareStatement("DELETE FROM corso WHERE titolo = ?;");
            prepStat.setString(1, corso.getTitolo());
            prepStat.executeUpdate();
            System.out.println(corso + " eliminato correttamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean inserisciDocente(Docente docente) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO Docente VALUES (?,?,?);");
            prepStat.setString(1, UUID.randomUUID().toString());
            prepStat.setString(2, docente.getNome());
            prepStat.setString(3, docente.getCognome());
            prepStat.executeUpdate();
            System.out.println("Aggiunto Docente alla Lista");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean rimuoviDocente(Docente docente) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM docente WHERE nome = ? and cognome=?;");
            prepStat.setString(1,docente.getNome());
            prepStat.setString(2,docente.getCognome());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean inserisciInsegnamento(Docente docente, Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO insegnamento VALUES (?,?);");
            prepStat.setInt(1, trovaIdDoc(docente));
            prepStat.setString(2, corso.getTitolo());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean rimuoviInsegnamento(Docente docente, Corso corso) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("DELETE  FROM insegnamento WHERE docente = ? and corso = ?;");
            prepStat.setInt(1, trovaIdDoc(docente));
            prepStat.setString(2, corso.getTitolo());
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String trovaIdDocente(Docente docente){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            Statement st = conn1.createStatement();
            //ResultSet rs = st.executeQuery("SELECT id from Docente where nome = "+docente.getNome()+" and cognome ="+docente.getCognome()+";");
            PreparedStatement prepStat = conn1.prepareStatement("SELECT id from Docente where nome = ? and cognome = ?;");
            prepStat.setString(1,docente.getNome());
            prepStat.setString(2,docente.getCognome());
            ResultSet rs = prepStat.executeQuery();
            if(rs.next() == false)
                System.out.println("Docente non trovato nel database");
            else
                return rs.getString("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Prenotazione> ripetizioniDisponibili(){
        Connection conn1 = null;
        ArrayList<Prenotazione> ripetizioni_disp = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
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
                    ripetizioni_disp.add(new Prenotazione(docente,corso,null,ora,giorno,Stato.attiva));
                }
                return ripetizioni_disp;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ripetizioni_disp;
    }

    public static boolean disdireRip(Prenotazione prenotazione){ //segnare come disdetta una ripetizione
        Connection conn1 = null;
        try {
            PreparedStatement prepStat = conn1.prepareStatement("UPDATE prenotazione SET stato = ? WHERE id = ?;");
            prepStat.setString(1,"disdetta");
            prepStat.setInt(2,trovaIdPren(prenotazione));
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean prenotareRip(Prenotazione prenotazione, Utente utente){ //segnare come effettuata una ripetizione
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            PreparedStatement prepStat = conn1.prepareStatement("UPDATE prenotazione SET stato = ?  and utente = ? WHERE id = ?;");
            prepStat.setString(1,"effettuata");
            prepStat.setString(2,utente.getAccount());
            prepStat.setInt(3, trovaIdPren(prenotazione));
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean aggiungereRipetizione(Prenotazione prenotazione){ //aggiungire tupla rip segnata come attiva
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            PreparedStatement prepStat = conn1.prepareStatement("INSERT INTO prenotazione VALUES (?,?,?,?,?,?,?,?);");
            prepStat.setInt(1,trovaIdPren(prenotazione));
            prepStat.setInt(2,trovaIdDoc(prenotazione.getDocente()));
            prepStat.setString(3,prenotazione.getCorso().getTitolo());
            prepStat.setString(4,prenotazione.getUtente().getAccount());
            prepStat.setInt(4, Integer.parseInt(prenotazione.getSlot().toString()));
            prepStat.setString(5, prenotazione.getGiorno().toString());
            prepStat.setString(6, "attiva");
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int trovaIdPren(Prenotazione prenotazione){ //docente corso account ora giorno
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT id from prenotazione where docente="+trovaIdDoc(prenotazione.getDocente())+" and utente="+prenotazione.getUtente()+" and ora="+prenotazione.getSlot().toString()+" and giorno="+prenotazione.getGiorno().toString()+" and corso="+prenotazione.getCorso().getTitolo()+";");
            if(rs.next() == false)
                System.out.println("Prenotazione non trovata nel database");
            else
                return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Docente trovaDoc_byId(int idDoc){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT nome,cognome from Docente where id="+idDoc+";");
            if(rs.next() == false)
                System.out.println("Docente non trovato nel database");
            else
                return new Docente(rs.getString("nome"),rs.getString("cognome"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Utente trovaUtente(String utente){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT account,password,admin from utente where account= "+utente+";");
            if(rs.next() == false)
                System.out.println("Docente non trovato nel database");
            else
                return new Utente(utente,rs.getString("password"),rs.getBoolean("admin"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean listRip_Utente(Utente utente){ //visualizzare le ripetizioni utente
        Connection conn1 = null;
        ArrayList<Prenotazione> ripetizioni_pren = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT id,docente,corso,giorno,ora,stato from prenotazione where utente="+utente.getAccount()+";");
            while (rs.next()){
                int id = rs.getInt("id");
                int docente = rs.getInt("docente");
                Corso corso = new Corso(rs.getString("corso"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                Stato stato = Stato.valueOf(rs.getString("stato"));
                ripetizioni_pren.add(new Prenotazione(trovaDoc_byId(docente),corso,utente,ora,giorno,stato));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean listRipPren(){ //visualizzare le ripetizioni prenotate
        Connection conn1 = null;
        ArrayList<Prenotazione> ripetizioni_pren = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT id,docente,corso,giorno,ora from prenotazione where stato="+Stato.effettuata+";");
            while (rs.next()){
                int id = rs.getInt("id");
                int docente = rs.getInt("docente");
                String utente = rs.getString("utente");
                Corso corso = new Corso(rs.getString("corso"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                ripetizioni_pren.add(new Prenotazione(trovaDoc_byId(docente),corso,trovaUtente(utente),ora,giorno,Stato.effettuata));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
