package project.servlet.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAO {
    private final static String URL = "jdbc:mysql://localhost:3306/ripetizioni";
    private final static String USER = "root";
    private final static String PASSWORD = "";

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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ripetizioni.utente WHERE BINARY account =? AND BINARY password =?;");
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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement update = connection.prepareStatement("UPDATE ripetizioni.prenotazione SET stato='disdetta' WHERE corso=? AND stato='attiva';");
            update.setString(1, corso.getTitolo());
            update.executeUpdate();

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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            //disdico prima tutte le prenotazioni con il docente
            PreparedStatement update = connection.prepareStatement("UPDATE ripetizioni.prenotazione JOIN ripetizioni.docente ON prenotazione.docente=docente.id SET stato='disdetta' WHERE nome=? AND cognome=? AND stato='attiva';");
            update.setString(1, docente.getNome());
            update.setString(2, docente.getCognome());
            update.executeUpdate();
            //infine elimino il docente
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ripetizioni.docente WHERE nome=? and cognome=?;");
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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement update = connection.prepareStatement("UPDATE ripetizioni.prenotazione " +
                    "JOIN ripetizioni.docente ON prenotazione.docente=docente.id " +
                    "SET stato='disdetta' WHERE nome=? AND cognome=? AND corso=? AND stato='attiva';");
            update.setString(1, docente.getNome());
            update.setString(2, docente.getCognome());
            update.setString(3, corso.getTitolo());
            update.executeUpdate();

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

    /**
     * Estrae la lista di ripetizioni disponibili dal database
     * @param utente, se null le restituisco tutte
     * @return lista delle ripetizioni disponibili
     */
    public static List<Prenotazione> getRipetizioniDisponibili(Utente utente) {
        Connection connection = null;
        List<Prenotazione> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "SELECT DISTINCT d.nome, d.cognome, i.corso, g.giorno, s.ora " +
                    "FROM ripetizioni.insegnamento i JOIN ripetizioni.docente d ON i.docente=d.id, slot s, giorno g " +
                    "WHERE ((i.docente, i.corso, s.ora, g.giorno) NOT IN (" +
                    "    SELECT docente, corso, ora, giorno " +
                    "    FROM ripetizioni.prenotazione p" +
                    "    WHERE p.stato='attiva' or p.stato='effettuata')) ";

            Statement st = connection.createStatement();
            ResultSet rs;
            if(utente.getAccount()!=null) {
                //aggiunta per evitare che l'utente prenoti per se stesso due pren lo stesso giorno e ora
                String queryPlusUtente = "AND ((s.ora, g.giorno) NOT IN (" +
                      "    SELECT ora, giorno " +
                      "    FROM ripetizioni.prenotazione p" +
                      "    WHERE p.stato='attiva' and  p.utente='" + utente.getAccount() + "'));";
                rs = st.executeQuery(query + queryPlusUtente);
            }
            else
                rs = st.executeQuery(query);
            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                Corso corso = new Corso(rs.getString("corso"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                result.add(new Prenotazione(docente, corso, null, ora, giorno, null));
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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement("UPDATE ripetizioni.prenotazione SET stato = 'disdetta' WHERE id = ( " +
                    "SELECT id " +
                    "FROM ripetizioni.prenotazione " +
                    "WHERE docente= (" +
                    "   SELECT id " +
                    "   FROM ripetizioni.docente " +
                    "   WHERE docente.nome=? AND docente.cognome=?"  +
                    ") AND corso=? AND utente=? AND ora=? AND giorno=? AND stato='attiva'" +
                    ");");
            statement.setString(1, prenotazione.getDocente().getNome());
            statement.setString(2, prenotazione.getDocente().getCognome());
            statement.setString(3,prenotazione.getCorso().getTitolo());
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
     * Contrassegna una prenotazione come effettuata, se nel database esiste
     *
     * @param prenotazione: prenotazione da modificare
     * @return true se l'operazione è stata effettuata con successo
     */
    public static boolean setPrenotazioneEffettuata(Prenotazione prenotazione){
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
        PreparedStatement statement = null;
        if(prenotazione.getDocente()!=null && prenotazione.getUtente()!=null && prenotazione.getSlot()!= null && prenotazione.getGiorno()!=null && prenotazione.getCorso()!=null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                boolean error = false;
                //verifico che l'utente non abbia già una prenotazione a quell'ora e giorno
                List<Prenotazione> storico = getStoricoPrenotazioni(prenotazione.getUtente());
                for (Prenotazione p : storico) {
                    if(p.getStato()==Stato.ATTIVA)
                        error = p.getGiorno() == prenotazione.getGiorno() && p.getSlot() == prenotazione.getSlot();
                }
                //verifico che il docente non abbia già una prenotazione a quell'ora e giorno
                if(!error)
                    error = !docenteDisponibile(connection, statement,prenotazione.getDocente(),prenotazione.getGiorno(), prenotazione.getSlot());
                //inserisco la prenotazione
                if(!error) {
                    statement = connection.prepareStatement("INSERT INTO ripetizioni.prenotazione (id, docente, corso, utente, ora, giorno, stato) " +
                          "SELECT ?, insegnamento.docente, insegnamento.corso, ?, ?, ?, 'attiva' " +
                          "FROM ripetizioni.docente JOIN ripetizioni.insegnamento ON docente.id = insegnamento.docente " +
                          "WHERE docente.nome=? AND docente.cognome=? AND insegnamento.corso=?;");
                    statement.setString(1, UUID.randomUUID().toString());
                    statement.setString(2, prenotazione.getUtente().getAccount());
                    statement.setInt(3, prenotazione.getSlot().getValue());
                    statement.setString(4, prenotazione.getGiorno().toString());
                    statement.setString(5, prenotazione.getDocente().getNome());
                    statement.setString(6, prenotazione.getDocente().getCognome());
                    statement.setString(7, prenotazione.getCorso().getTitolo());
                    result = statement.executeUpdate() == 1;
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
        }
        return result;
    }

    /**
     * restituisce true se il docente è libero quel giorno a quell'ora
     *
     * @param connection
     * @param statement
     * @param docente
     * @param giorno
     * @param slot
     * @return
     */
    private static boolean docenteDisponibile(Connection connection, PreparedStatement statement, Docente docente, Giorno giorno, Slot slot) throws SQLException {
        boolean result = false;
            statement = connection.prepareStatement("SELECT docente.nome, docente.cognome " +
                  "FROM ripetizioni.prenotazione JOIN ripetizioni.docente ON prenotazione.docente = docente.id " +
                  "WHERE docente.cognome=? and docente.nome and giorno=? and ora=?;");
            statement.setString(1, docente.getCognome());
            statement.setString(2, docente.getNome());
            statement.setString(3, giorno.name().toLowerCase());
            statement.setString(1, Integer.toString(slot.getValue()));
            ResultSet rs = statement.executeQuery();

            if(!rs.next()) //se non ha corrispondenza
                result=true;
        return result;
    }

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
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT docente.nome, docente.cognome, corso, utente, giorno, ora, stato " +
                  "FROM ripetizioni.prenotazione left join ripetizioni.docente ON prenotazione.docente = docente.id " +
                    "WHERE utente=?;");
            statement.setString(1, utente.getAccount());
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                Corso corso = new Corso(rs.getString("corso"));
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Stato stato = Stato.fromString(rs.getString("stato"));
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

    /**
     * Restituisce la lista delle prenotazioni nello storico di tutti gli utenti
     *
     * @return lista delle prenotazioni (degli utenti si estrae solo il nome utente)
     */
    public static List<Prenotazione> getStoricoPrenotazioni() {
        Connection connection = null;
        List<Prenotazione> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT docente.nome, docente.cognome, corso, utente, giorno, ora, stato " +
                  "FROM ripetizioni.prenotazione left join ripetizioni.docente ON prenotazione.docente = docente.id;");

            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                Corso corso = new Corso(rs.getString("corso"));
                Utente utente = new Utente(rs.getString("utente"), null, null);
                Slot ora = Slot.fromInt(rs.getInt("ora"));
                Giorno giorno = Giorno.fromString(rs.getString("giorno"));
                Stato stato = Stato.fromString(rs.getString("stato"));

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

    /**
     * @return lista di docenti nel database
     */
    public static List<Docente> getDocenti() {
        Connection connection = null;
        List<Docente> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ripetizioni.docente;");
            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                if(docente.getCognome()!=null || docente.getNome()!=null)
                    result.add(docente);
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
     * @return lista di corsi nel database
     */
    public static List<Corso> getCorsi() {
        Connection connection = null;
        List<Corso> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ripetizioni.corso;");
            while (rs.next()){
                Corso corso = new Corso(rs.getString("titolo"));
                result.add(corso);
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
     * @return Lista dei corsi tenuti da un docente
     */
    public static List<Corso> getCorsiInsegnatiDa(Docente docente) {
        Connection connection = null;
        List<Corso> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT corso FROM " +
                    "ripetizioni.insegnamento JOIN ripetizioni.docente ON insegnamento.docente = docente.id " +
                    "where docente.nome=? AND docente.cognome=?;");
            statement.setString(1,docente.getNome());
            statement.setString(2,docente.getCognome());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Corso corso = new Corso(rs.getString("corso"));
                result.add(corso);
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
     * @return Lista dei docenti che tengono un corso
     */
    public static List<Docente> getDocentiCheInsegnano(Corso corso) {
        Connection connection = null;
        List<Docente> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT docente.nome,docente.cognome FROM " +
                    "ripetizioni.insegnamento JOIN ripetizioni.docente ON insegnamento.docente = docente.id " +
                    "where insegnamento.corso=?;");
            statement.setString(1,corso.getTitolo());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Docente docente = new Docente(rs.getString("nome"), rs.getString("cognome"));
                result.add(docente);
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
