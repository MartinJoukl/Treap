package org.example.Domain;

public class Municipality {
    private String nazevObce;
    private int inhabitants;

    public Municipality(String nazevObce, int inhabitants) {
        this.nazevObce = nazevObce;
        this.inhabitants = inhabitants;
    }

    public String getNazevObce() {
        return nazevObce;
    }

    public void setNazevObce(String nazevObce) {
        this.nazevObce = nazevObce;
    }

    public int getInhabitants() {
        return inhabitants;
    }

    public void setInhabitants(int inhabitants) {
        this.inhabitants = inhabitants;
    }
}
