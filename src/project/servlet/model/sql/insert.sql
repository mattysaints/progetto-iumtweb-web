START TRANSACTION;
SET autocommit = 0;

INSERT INTO utente VALUES ('Davide', 'dado1', 1);
INSERT INTO utente VALUES ('Mattia', 'mattia1', 1);
INSERT INTO utente VALUES ('Marco', 'marco1', 1);
INSERT INTO utente VALUES ('Alex97', 'alex#', 0);
INSERT INTO utente VALUES ('Johnny', 'jjmaster', 0);
INSERT INTO utente VALUES ('Lucy_', 'lucyinthesky', 0);

COMMIT;
START TRANSACTION;
SET autocommit = 0;

INSERT INTO corso VALUES ('Matematica');
INSERT INTO corso VALUES ('Latino');
INSERT INTO corso VALUES ('Informatica');
INSERT INTO corso VALUES ('Inglese');
INSERT INTO corso VALUES ('Francese');

COMMIT;
START TRANSACTION;
SET autocommit = 0;

INSERT INTO docente VALUES ('5d9f2c63-aee8-4c58-81c2-cf0821baa9a5', 'Mario', 'Rossi');
INSERT INTO docente VALUES ('7162dbe1-599f-4c6f-a47b-d573af781250', 'Simona', 'Verdi');
INSERT INTO docente VALUES ('385cbfac-0cbd-4c08-9177-67409007b201', 'Andrea', 'Bianchi');
INSERT INTO docente VALUES ('d500b7ad-6a58-44ae-ab89-67a2dad3798e', 'Luca', 'Neri');
INSERT INTO docente VALUES ('2501c1ec-0da5-431b-a5e6-3b7ff634ef93', 'Angelo', 'Russo');

COMMIT;
START TRANSACTION;
SET autocommit = 0;

INSERT INTO insegnamento VALUES ((SELECT id FROM docente WHERE nome='Mario' AND cognome='Rossi'), 'Matematica');
INSERT INTO insegnamento VALUES ((SELECT id FROM docente WHERE nome='Mario' AND cognome='Rossi'), 'Informatica');
INSERT INTO insegnamento VALUES ((SELECT id FROM docente WHERE nome='Simona' AND cognome='Verdi'), 'Inglese');
INSERT INTO insegnamento VALUES ((SELECT id FROM docente WHERE nome='Simona' AND cognome='Verdi'), 'Francese');
INSERT INTO insegnamento VALUES ((SELECT id FROM docente WHERE nome='Andrea' AND cognome='Bianchi'), 'Matematica');

COMMIT;
START TRANSACTION;
SET autocommit = 0;

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT '8a4a607f-d75d-48ed-a1a8-a051627b7999', docente, corso, 'Davide', 15, 'lun', 'effettuata'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Mario' AND cognome='Rossi' AND insegnamento.corso='Matematica';

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT 'c5d66059-3524-43ad-805d-49f6de2fb78e', docente, corso, 'Mattia', 18, 'lun', 'attiva'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Simona' AND cognome='Verdi' AND insegnamento.corso='Inglese';

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT '2383d726-1c71-4fc5-9d72-f6528faf6666', docente, corso, 'Marco', 16, 'mar', 'disdetta'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Andrea' AND cognome='Bianchi' AND insegnamento.corso='Matematica';

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT '14700294-1053-4b05-886a-1d84d3a477a9', docente, corso, 'Alex97', 17, 'mar', 'effettuata'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Mario' AND cognome='Rossi' AND insegnamento.corso='Matematica';

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT '3599a01f-ba1b-4fa8-b4c8-3296e2d0163f', docente, corso, 'Alex97', 18, 'ven', 'attiva'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Mario' AND cognome='Rossi' AND insegnamento.corso='Matematica';

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT 'eb08befa-b959-42fc-862a-e9691f8aac80', docente, corso, 'Alex97', 15, 'gio', 'attiva'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Mario' AND cognome='Rossi' AND insegnamento.corso='Informatica';

INSERT INTO prenotazione (id, docente, corso, utente, ora, giorno, stato)
    SELECT '01563cb9-275e-44e4-ab21-60d2d1c17fe1', docente, corso, 'Lucy_', 18, 'mer', 'attiva'
    FROM insegnamento JOIN docente ON insegnamento.docente = docente.id
    WHERE nome='Simona' AND cognome='Verdi' AND insegnamento.corso='Francese';

COMMIT;