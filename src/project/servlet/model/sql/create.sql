BEGIN;

CREATE TABLE utente (
    account varchar(30),
    password varchar(30) NOT NULL,
    admin bool NOT NULL,
    CONSTRAINT pk_utente PRIMARY KEY (account)
);

CREATE TABLE corso (
    titolo varchar(30),
    CONSTRAINT pk_corso PRIMARY KEY (titolo)
);

CREATE TABLE docente (
    id char(36), -- NOTA: 36 caratteri perchè cosi possiamo utilizzare UUID.randomUUID() per generare un id univoco
    nome varchar(30) NOT NULL,
    cognome varchar(30) NOT NULL,
    CONSTRAINT pk_docente PRIMARY KEY (id),
    CONSTRAINT u_ UNIQUE (nome, cognome)
);

CREATE TABLE insegnamento (
    docente char(36),
    corso varchar(30),
    CONSTRAINT pk_insegnamento PRIMARY KEY (docente, corso),
    CONSTRAINT fk_docente FOREIGN KEY (docente) REFERENCES docente(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_corso FOREIGN KEY (corso) REFERENCES corso(titolo) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE prenotazione (
    docente char(36),
    corso varchar(30),
    utente varchar(30),
    --OraInizio
    --OraFine
    --Giorno
    CONSTRAINT pk_prenotazione PRIMARY KEY (docente,corso,utente),
    CONSTRAINT fk_docente FOREIGN KEY (docente) REFERENCES docente(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_corso FOREIGN KEY (corso) REFERENCES corso(titolo) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_utente FOREIGN KEY (utente) REFERENCES utente(account) ON DELETE CASCADE ON UPDATE CASCADE,
);





-- TODO finire



