START TRANSACTION;
SET autocommit = 0;
DELETE FROM ripetizioni.prenotazione;
DELETE FROM ripetizioni.insegnamento;
DELETE FROM ripetizioni.giorno;
DELETE FROM ripetizioni.utente;
DELETE FROM ripetizioni.slot;
DELETE FROM ripetizioni.docente;
DELETE FROM ripetizioni.corso;
COMMIT;

START TRANSACTION;
SET autocommit = 0;
DROP TABLE IF EXISTS ripetizioni.prenotazione;
DROP TABLE IF EXISTS ripetizioni.insegnamento;
DROP TABLE IF EXISTS ripetizioni.giorno;
DROP TABLE IF EXISTS ripetizioni.utente;
DROP TABLE IF EXISTS ripetizioni.slot;
DROP TABLE IF EXISTS ripetizioni.docente;
DROP TABLE IF EXISTS ripetizioni.corso;
COMMIT;