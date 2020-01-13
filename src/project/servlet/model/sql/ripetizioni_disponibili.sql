SELECT DISTINCT d.nome, d.cognome, i.corso, g.giorno, s.ora
FROM ripetizioni.insegnamento i JOIN ripetizioni.docente d ON i.docente=d.id, slot s, giorno g
WHERE (i.docente, i.corso, s.ora, g.giorno) NOT IN (
    SELECT docente, corso, ora, giorno
    FROM ripetizioni.prenotazione
    WHERE stato='attiva'
    ) and (d.nome IS NOT NULL OR d.cognome IS NOT NULL);
