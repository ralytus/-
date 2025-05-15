package org.kvantoriumproject.kvantoriumproject;

import java.time.LocalDate;

public class Privychka {
    private String nazvanie;
    private String chastota;
    private LocalDate dataNachala;
    private boolean vypolneno;

    public Privychka(String nazvanie, String chastota, LocalDate dataNachala, boolean vypolneno) {
        this.nazvanie = nazvanie;
        this.chastota = chastota;
        this.dataNachala = dataNachala;
        this.vypolneno = vypolneno;
    }

    public String getNazvanie() { return nazvanie; }
    public String getChastota() { return chastota; }
    public LocalDate getDataNachala() { return dataNachala; }
    public boolean isVypolneno() { return vypolneno; }
    public void setVypolneno(boolean vypolneno) { this.vypolneno = vypolneno; }
}