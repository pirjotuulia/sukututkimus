package gene.domain;

import java.time.LocalDate;

public class Henkilo {
    private String etunimi;
    private String sukunimi;
    private LocalDate syntymaAika;
    private int id;

    public Henkilo() {
    }

    public Henkilo(String etunimi, String sukunimi, LocalDate syntymaAika) {
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.syntymaAika = syntymaAika;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public LocalDate getSyntymaAika() {
        return syntymaAika;
    }

    public void setSyntymaAika(LocalDate syntymaAika) {
        this.syntymaAika = syntymaAika;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
