# PROGETTO IUM TWEB 2
Cose da fare

## Ultime cose da fare
- Controllare le proprie view che funzionino correttamente e non diano problemi, errori
- Aggiungere filtri a storico prenotazioni
- home android (dawoz)
- sistemare graficamente le view
- elinare dove possibile gli alert e inserire del testo nella view
- logout in android
- ospite android
- tweb storico prenotazioni. dentro il contenitore della select dovrebbe vedersi "Attiva"
- controllare che le pagine si vedano correttamente al cambiamento della dimensione dello schermo (minWeight, maxWeight)

## Modello

### Database schema
- utenti (nome, password, ruolo)
- docenti (nome, cognome)
- corsi (titolo)
- storico prenotazioni (docente, utente, corso, giorno, ora)
- associazioni corso-docente (corso, docente)

Le ripetizioni disponibili sono ricavate da una query (prendendo lo storico)

### DAO
Mattia:

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
Davide:
1. [Login] controllo se un utente è registrato (ed eventualmente se è amministratore) controllo della sessione utente

IMPORTANTE: ogni volta che un'altra servlet viene invocata deve controllare che la sessione esista (senza crearne una nuova):
- in caso positivo la sessione contiene le informazioni su nome utente e se è admin
- in caso negativo: la servlet deve fare la include() alla "LoginServlet"

2. [RipetizioniDisponibili] visualizza lista ripetizioni disponibili (json)

Marco:
3. [OpSuPrenotazioni] prenotare, disdire e segnare come effetuata la prenotazione 
4. [StoricoPrenotazioni] visualizzare lo storico di un utente (sia utente che admin) e visualizzare tutte le prenotazioni: attive, effettuate, cancellate (admin) 
5. [GestioneDocenti] inserire, eliminare e visualizzare tutti i docenti (admin)
6. [GestioneInsegnamenti] inserire, eliminare e visualizzare tutti gli insegnamenti (dato il docente o il corso)

Mattia:
5. [GestioneCorsi] inserire, eliminare e visualizzare tutti i corsi (admin)

## View

### Login
Marco:
- Pagina di login
- Gestione della sessione utente

Collegamenti:
- Homepage

### Homepage
Davide:
- Sia admin che utente (con piccole differenze)
- Contenuto di default (es. ripetizioni prenotate o immagine)

Collegamenti (solo utente):
- Prenotazione ripetizioni
- Storico delle prenotazioni

In aggiunta, collegamenti (solo admin):
- Pagina di gestione:
    - Gestione corsi
    - Gestione docenti
- Visualizzazione di tutto il calendario

### Prenotazione ripetizioni
Mattia: 
- Sia admin che utente
- Visualizzazione ripetizioni disponibili
- Prenotazione una per volta
- Filtri (giorno, ora, corso: opzionale)

### Gestione docenti
Marco:
- Solo admin
- inserire docenti
- visualizzare la lista di docenti
- rimuovere un docente

### Gestione corsi
Davide:
- Solo admin
- inserire corsi
- visualizzare lista corsi
- rimuovere un corso

### Gestione dello storico
Mattia:
- Sia utente che admin
- visualizzare lo storico
- cambiare lo stato di una prenotazione
- filtri (opzionale: vedi prenotazioni disponibili)

### Visualizzazione di tutte le prenotazioni del calendario
Marco:
- Solo admin
- visualizzazione elenco tutte ripetizioni attive, effettuate o cancellate

----------------------
# ANDROID

## Model
Davide:

Importare:
- Docente
- Giorno
- Insegnamento
- Prenotazione
- Slot
- Stato
- Utente

Richieste alle servlet (dati in formato Json):
- Richiesta storico
- Richiesta ripetizioni disponibili

## View

### Login
Davide:
- Pagina di login
- Gestione della sessione utente

Collegamenti:
- Homepage

### Homepage
Mattia:
- Contenuto di default (es. ripetizioni prenotate o immagine)

Collegamenti:
- Prenotazione ripetizioni
- Gestione dello storico

### Prenotazione ripetizioni
Marco:
- Tutti
- Visualizzazione ripetizioni disponibili
- Prenotazione una per volta
- Filtri (giorno, ora, corso: opzionale)

### Gestione dello storico
Mattia:
- visualizzare lo storico
- cambiare lo stato di una prenotazione
- filtri (opzionale: vedi prenotazioni disponibili)

