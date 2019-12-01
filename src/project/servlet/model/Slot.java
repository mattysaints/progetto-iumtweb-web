package project.servlet.model;

enum Slot{
    //Per utilizzare il nome di una enumerazione (ad esempio per la coincidenza con i nomi nel database) usare il campo name
    //es. Slot.SLOT1.name --> LUN
    //Mentre per visualizzare il risultato nell'interfaccia grafica useremo il metodo toString

    SLOT1("15-16"),
    SLOT2("16-17"),
    SLOT3("17-18"),
    SLOT4("18-19");

    private final String value;

    Slot(String newValue) {
        value = newValue;
    }

    @Override
    public String toString() {
        return "Orario: " + value;
    }
    //public int getValue() { return value; }
}
