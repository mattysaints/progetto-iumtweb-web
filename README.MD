# PROGETTO IUM TWEB 2
Cose da fare
## Modello
### Database schema
- utenti (nome, password, admin)
- docenti (nome, cognome)
- corsi (titolo)
- storico prenotazioni (docente, utente, corso, giorno, ora)
- associazioni corso-docente (corso, docente)

Le ripetizioni disponibili sono ricavate da una query (prendendo lo storico)
### DAO
- controllare che un utente esista nel db
- recuperare ripetizioni disponibili (eventualmente filtrando per giorno, ora, corso)
- cancellare una ripetizione
- aggiungere una o più ripetizioni
- segnare una ripetizione come effettuata
- visualizzare le ripetizioni prenotate:
    - se vengono rimossi docenti o corsi lo storico deve rimanere
    - storico:
        - prenotazioni effettuate
        - prenotazioni disdette
        - prenotazioni attive (da effettuare)
- visualizzare le prenotazioni attive, effettuate e cancellate (ricerca per cliente)
- inserire corsi
- rimuovere corsi
- inserire docenti
- rimuovere docenti
- aggiungere associazioni corso-docente
- rimuovere associazioni corso-docente

## Controller

### Servlet
1. controllo se un utente è registrato (ed eventualmente se è amministratore) controllo della sessione utente
2. visualizza lista ripetizioni disponibili (json)
3. prenotare, disdire e segnare come effetuata la prenotazione
4. visualizzare lo storico di un utente e visualizzare tutte le prenotazioni attive (admin)
5. inserire e rimuovere corsi e docenti (admin)