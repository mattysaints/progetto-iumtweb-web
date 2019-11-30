SELECT DISTINCT d.nome, d.cognome, i.corso, g.giorno, s.ora
FROM insegnamento i JOIN docente d ON i.docente=d.id, slot s, giorno g
WHERE (i.docente, i.corso, s.ora, g.giorno) NOT IN (
    SELECT docente, corso, ora, giorno
    FROM prenotazione
    WHERE stato='attiva'
    );