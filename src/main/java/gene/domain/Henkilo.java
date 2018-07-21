package gene.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Henkilo {
    private String etunimi;
    private String sukunimi;
    private LocalDate syntymaAika;
    private int id;
    private Integer aiti;
    private int isa;
    private Henkilo aitiHenkilo;
    private Henkilo isaHenkilo;

    public Henkilo() {
    }

    @Override
    public String toString() {
        return etunimi + " " + sukunimi + " " + syntymaAika.getDayOfMonth() + "." + syntymaAika.getMonthValue() + "." + syntymaAika.getYear() + ", id:" + id;
    }

    public Henkilo(String etunimi, String sukunimi, LocalDate syntymaAika) {
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.syntymaAika = syntymaAika;
    }

    public int getAiti() {
        return aiti;
    }

    public void setAiti(Integer aiti) {
        if (aiti != null) {
            this.aiti = aiti;
        }
    }

    public int getIsa() {
        return isa;
    }

    public void setIsa(Integer isa) {
        if (isa != null) {
            this.isa = isa;
        }
    }

    public Henkilo getAitiHenkilo() {
        return aitiHenkilo;
    }

    public void setAitiHenkilo(Henkilo aitiHenkilo) {
        this.aitiHenkilo = aitiHenkilo;
    }

    public Henkilo getIsaHenkilo() {
        return isaHenkilo;
    }

    public void setIsaHenkilo(Henkilo isaHenkilo) {
        this.isaHenkilo = isaHenkilo;
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
