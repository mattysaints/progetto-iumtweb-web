package project.servlet.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAO {
    private final static String url = "jdbc:mysql://localhost:3306/ripetizioni";
    private final static String user = "root";
    private final static String password = "";

    /**
     * Registra il Driver JDBC
     */
    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    /**
     * Query al database dato un utente
     *
     * @param utente: utente da trovare nel database
     * @return l'oggetto utente se esiste (con campo admin ricavato), altrimenti null
     */
    public static Utente queryUtente(Utente utente) {
        Connection connection = null;
        Utente result = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ripetizioni.utente WHERE account =? AND password=?;");
            statement.setString(1, utente.getAccount());
            statement.setString(2, utente.getPassword());
            ResultSet rs = statement.executeQuery();
            if(rs.next())
                result = new Utente(rs.getString("account"), rs.getString("password"), rs.getBoolean("admin"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Inserisce un corso nel database
     *
     * @param corso: corso da aggiungere
     * @return true se l'operazione è avvenuta con successo
     */
    public static boolean insertCorso(Corso corso) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ripetizioni.corso VALUES (?);");
            statement.setString(1, corso.getTitolo());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Rimuove un corso dal database
     *
     * @param corso: corso da rimuovere
     * @return true se la rimozione è avvenuta correttamente
     */
    public static boolean deleteCorso(Corso corso) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ripetizioni.corso WHERE titolo = ?;");
            statement.setString(1, corso.getTitolo());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Inserisce un docente nel database
     *
     * @param docente: docente da inserire
     * @return true se l'operazione è avvenuta con successo
     */
    public static boolean insertDocente(Docente docente) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ripetizioni.docente VALUES (?,?,?);");
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, docente.getNome());
            statement.setString(3, docente.getCognome());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Rimuove un docente dal database
     *
     * @param docente: docente da rimuovere
     * @return true se la rimozione è avvenuta con successo
     */
    public static boolean deleteDocente(Docente docente) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("DELETE  FROM ripetizioni.docente WHERE nome=? and cognome=?;");
            statement.setString(1,docente.getNome());
            statement.setString(2,docente.getCognome());
            result = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Associa un docente e un corso nel database (oggetto Insegnamento)
     *
     * @param docente: docente da associare al corso
     * @param corso: corso che il docente insegna
     * @return true se l'operazione è avvenuta con successo
     */
    public static boolean insertInsegnamento(Docente docente, Corso corso) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ripetizioni.insegnamento (docente, corso) " +
                    "SELECT id, titolo " +
                    "FROM ripetizioni.docente, ripetizioni.corso " +
                    "WHERE docente.nome=? AND docente.cognome=? AND corso.titolo=?;");
            statement.setString(1, docente.getNome());
            statement.setString(2, docente.getCognome());
            statement.setString(3, corso.getTitolo());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Rimuove un insegnamento dal database dati il docente e il corso che insegna
     *
     * @param docente: docente associato
     * @param corso: corso associato
     * @return true se la rimozione è avvenuta con successo
     */
    public static boolean deleteInsegnamento(Docente docente, Corso corso) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ripetizioni.insegnamento WHERE docente = (" +
                    "SELECT docente.id " +
                    "FROM ripetizioni.docente " +
                    "WHERE nome=? AND cognome=?) AND corso = ?;");
            statement.setString(1, docente.getNome());
            statement.setString(2, docente.getCognome());
            statement.setString(3, corso.getTitolo());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
    SI PUO TROVARE CON SQL

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

     */

    /**
     * Estrae la lista di ripetizioni disponibili dal database
     *
     * @return lista delle ripetizioni disponibili
     */
    public static List<Prenotazione> getRipetizioniDisponibili() {
        Connection connection = null;
        List<Prenotazione> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT DISTINCT d.nome, d.cognome, i.corso, g.giorno, s.ora " +
                        "FROM ripetizioni.insegnamento i JOIN ripetizioni.docente d ON i.docente=d.id, slot s, giorno g " +
                        "WHERE (i.docente, i.corso, s.ora, g.giorno) NOT IN (" +
                        "    SELECT docente, corso, ora, giorno " +
                        "    FROM ripetizioni.prenotazione " +
                        "    WHERE stato='attiva'" +
                        "    );";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                Corso corso = new Corso(rs.getString("corso"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                result.add(new Prenotazione(docente, corso,null, ora, giorno, Stato.attiva));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Contrassegna una ripetizione nel database come disdetta
     *
     * @param prenotazione: prenotazione da disdire
     * @return true se l'operazione è avvenuta con successo
     */
    public static boolean deletePrenotazione(Prenotazione prenotazione){
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE ripetizioni.prenotazione SET stato = 'disdetta' WHERE id = ( " +
                    "SELECT id " +
                    "FROM ripetizioni.prenotazione " +
                    "WHERE docente=? AND corso=? AND utente=? AND ora=? AND giorno=? AND stato='attiva');");
            statement.setString(1, prenotazione.getDocente().getNome());
            statement.setString(2, prenotazione.getDocente().getCognome());
            statement.setString(3, prenotazione.getUtente().getAccount());
            statement.setString(4, prenotazione.getGiorno().toString());
            statement.setInt(5, prenotazione.getSlot().getValue());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Contrassegna una prenotazione come effettuata, se nel daatabase esiste
     *
     * @param prenotazione: prenotazione da modificare
     * @return true se l'operazione è stata effettuata con successo
     */
    public static boolean setPrenotazioneEffettuata(Prenotazione prenotazione){
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE ripetizioni.prenotazione SET stato ='effettuata' WHERE id = (" +
                    "SELECT id " +
                    "FROM ripetizioni.prenotazione " +
                    "WHERE docente=(" +
                    "   SELECT id " +
                    "   FROM ripetizioni.docente " +
                    "   WHERE docente.nome=? AND docente.cognome=?"  +
                    ") AND corso=? AND utente=? AND ora=? AND giorno=? AND stato='attiva'" +
                    ");");
            statement.setString(1, prenotazione.getDocente().getNome());
            statement.setString(2, prenotazione.getDocente().getCognome());
            statement.setString(3, prenotazione.getCorso().getTitolo());
            statement.setString(4, prenotazione.getUtente().getAccount());
            statement.setInt(5, prenotazione.getSlot().getValue());
            statement.setString(6, prenotazione.getGiorno().toString());
            result = statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Inserisce una prenotazione nel database, contrassegnata come attiva
     *
     * @param prenotazione: prenotazione da inserire
     */
    public static boolean insertPrenotazione(Prenotazione prenotazione){
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(url, user, password);
            // eventualmente si può sfruttare la query ripetizioni disponibili
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ripetizioni.prenotazione (id, docente, corso, utente, ora, giorno, stato) " +
                    "SELECT ?, insegnamento.docente, insegnamento.corso, ?, ?, ?, 'attiva' " +
                    "FROM ripetizioni.docente JOIN ripetizioni.insegnamento ON docente.id = insegnamento.docente " +
                    "WHERE docente.nome=? AND docente.cognome=? AND insegnamento.corso=?;");
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, prenotazione.getUtente().getAccount());
            statement.setInt(3, prenotazione.getSlot().getValue());
            statement.setString(4, prenotazione.getGiorno().toString());
            statement.setString(5, prenotazione.getDocente().getNome());
            statement.setString(6, prenotazione.getDocente().getCognome());
            statement.setString(6, prenotazione.getCorso().toString());
            result = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
    SI PUO FARE TRAMITE SQL
    ---------------------------------------------

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

     */

    /*
    SI PUO FARE TRAMITE SQL
    ---------------------------------------

    public static Docente trovaDoc_byId(String idDoc){
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

     */

    /*
    UGUALE A QUERY UTENTE
    --------------------------------------------

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
     */

    /**
     * Restituisce la lista di prenotazioni presenti nello storico dell'utente
     *
     * @param utente: denota lo storico da estrarre dal database
     * @return lista di oggetti prenotazione
     */
    public static List<Prenotazione> getStoricoPrenotazioni(Utente utente){
        Connection connection = null;
        List<Prenotazione> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT docente.nome, docente.cognome, corso, utente, giorno, ora, stato " +
                    "FROM ripetizioni.prenotazione JOIN ripetizioni.docente ON prenotazione.docente = docente.id " +
                    "WHERE utente=?;");
            statement.setString(1, utente.getAccount());
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                Corso corso = new Corso(rs.getString("corso"));
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Stato stato = Stato.valueOf(rs.getString("stato"));

                result.add(new Prenotazione(docente, corso, utente, ora, giorno, stato));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
          try {
             if (connection != null)
                connection.close();
          } catch (SQLException e) {
             e.printStackTrace();
          }
       }
        return result;
    }

    /*
    SERVE? SE SI DA CORREGGERE ALTRIMENTI DA ELIMINARE
    --------------------------------------------------------------

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
                String docente = rs.getInt("docente");
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

     */

    /**
     * Restituisce la lista delle prenotazioni nello storico di tutti gli utenti
     *
     * @return lista delle prenotazioni (degli utenti si estrae solo il nome utente)
     */
    public static List<Prenotazione> getStoricoPrenotazioni() {
        Connection connection = null;
        List<Prenotazione> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT docente.nome, docente.cognome, corso, utente, giorno, ora, stato " +
                    "FROM ripetizioni.prenotazione JOIN ripetizioni.docente ON prenotazione.docente = docente.id;");

            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                Corso corso = new Corso(rs.getString("corso"));
                Utente utente = new Utente(rs.getString("utente"), null, null);
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Stato stato = Stato.valueOf(rs.getString("stato"));

                result.add(new Prenotazione(docente, corso, utente, ora, giorno, stato));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
