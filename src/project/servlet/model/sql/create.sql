START TRANSACTION ;
SET autocommit = 0;

CREATE TABLE utente (
    account VARCHAR(30),
    password VARCHAR(30) NOT NULL,
    admin BOOL NOT NULL,
    CONSTRAINT pk_utente PRIMARY KEY (account)
);

CREATE TABLE corso (
    titolo VARCHAR(30),
    CONSTRAINT pk_corso PRIMARY KEY (titolo)
);

CREATE TABLE docente (
    id CHAR(36), -- NOTA: 36 caratteri perchè cosi possiamo utilizzare UUID.randomUUID() per generare un id univoco
    nome VARCHAR(30) NOT NULL,
    cognome VARCHAR(30) NOT NULL,
    CONSTRAINT pk_docente PRIMARY KEY (id),
    CONSTRAINT un_docente UNIQUE (nome, cognome)
);

CREATE TABLE insegnamento (
    docente CHAR(36),
    corso VARCHAR(30),
    CONSTRAINT pk_insegnamento PRIMARY KEY (docente, corso),
    CONSTRAINT fk_insegnamento_docente FOREIGN KEY (docente) REFERENCES docente(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_insegnamento_corso FOREIGN KEY (corso) REFERENCES corso(titolo) ON DELETE CASCADE ON UPDATE CASCADE
);

-- creata per facilitare le query
CREATE TABLE slot (
    ora SMALLINT,
    CONSTRAINT pk_slot PRIMARY KEY (ora),
    CONSTRAINT ck_slot CHECK (ora >= 15 AND ora < 19)
);
INSERT INTO slot VALUES (15);
INSERT INTO slot VALUES (16);
INSERT INTO slot VALUES (17);
INSERT INTO slot VALUES (18);

-- creata per facilitare le query
CREATE TABLE giorno (
    giorno CHAR(3),
    CONSTRAINT pk_giorno PRIMARY KEY (giorno)
);
INSERT INTO giorno VALUES ('lun');
INSERT INTO giorno VALUES ('mar');
INSERT INTO giorno VALUES ('mer');
INSERT INTO giorno VALUES ('gio');
INSERT INTO giorno VALUES ('ven');

CREATE TABLE prenotazione (
    id CHAR(36),
    docente CHAR(36),
    corso VARCHAR(30),
    utente VARCHAR(30),
    ora SMALLINT NOT NULL,
    giorno CHAR(5) NOT NULL,
    stato ENUM ('attiva', 'effettuata', 'disdetta') NOT NULL DEFAULT 'attiva',
    CONSTRAINT pk_prenotazione PRIMARY KEY (id),
    CONSTRAINT fk_prenotazione_insegnamento FOREIGN KEY (docente, corso) REFERENCES insegnamento(docente, corso) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_prenotazione_utente FOREIGN KEY (utente) REFERENCES utente(account) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_prenotazione_ora FOREIGN KEY (ora) REFERENCES slot(ora) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_prenotazione_giorno FOREIGN KEY (giorno) REFERENCES giorno(giorno) ON DELETE NO ACTION ON UPDATE NO ACTION
);

COMMIT;


