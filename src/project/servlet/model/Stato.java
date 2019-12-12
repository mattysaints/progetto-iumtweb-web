package project.servlet.model;

enum Stato {
    //Per utilizzare il nome di una enumerazione (ad esempio per la coincidenza con i nomi nel database) usare il campo name
    //es. Slot.SLOT1.name --> LUN
    //Mentre per visualizzare il risultato nell'interfaccia grafica useremo il metodo toString

    effettuata,
    attiva, //prenotata
    disdetta;

    @Override
    public String toString() {
        return "Stato: " + this.name();
    }

}
