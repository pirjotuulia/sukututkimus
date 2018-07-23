package gene.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Henkilo {
    private String etunimi;
    private String sukunimi;
    private LocalDate syntymaAika;
    private LocalDate kuolinAika;
    private int id;
    private Integer aiti;
    private Integer isa;
    private Integer puoliso;
    private Henkilo aitiHenkilo;
    private Henkilo isaHenkilo;
    private Henkilo puolisoHenkilo;
    private List<Henkilo> lapset;

    public Henkilo() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(etunimi + " " + sukunimi + " (id: "+ id + ")");
        return sb.toString();
    }

    public Henkilo(String etunimi, String sukunimi, LocalDate syntymaAika) {
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.syntymaAika = syntymaAika;
        this.lapset = new ArrayList<>();
    }

    public Integer getAiti() {
        if (aiti!=null) {
            return aiti;
        }
        return 0;
    }

    public void setAiti(Integer aiti) {
        if (aiti != null) {
            this.aiti = aiti;
        }
    }

    public Integer getIsa() {
        if (isa!=null) {
            return isa;
        }
        return 0;
    }

    public void setIsa(Integer isa) {
        if (isa != null) {
            this.isa = isa;
        }
    }

    public Integer getPuoliso() {
        if (puoliso!=null) {
            return puoliso;
        }
        return 0;
    }

    public void setPuoliso(Integer puoliso) {
        if (puoliso != null) {
            this.puoliso = puoliso;
        }
    }

    public Henkilo getPuolisoHenkilo() {
        if (puoliso != null) {
            return puolisoHenkilo;
        }
        return null;
    }

    public void setPuolisoHenkilo(Henkilo puolisoHenkilo) {
        this.puolisoHenkilo = puolisoHenkilo;
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

    public boolean isaTiedossa() {
        if (isa!=null) {
            return true;
        }
        return false;
    }

    public boolean aitiTiedossa() {
        if (aiti!=null) {
            return true;
        }
        return false;
    }

    public boolean puoliso() {
        if (puoliso!=null) {
            return true;
        }
        return false;
    }

    public List<Henkilo> getLapset() {
        return lapset;
    }

    public void setLapset(List<Henkilo> lapset) {
        this.lapset = lapset;
    }

    public LocalDate getKuolinAika() {
        return kuolinAika;
    }

    public void setKuolinAika(LocalDate kuolinAika) {
        this.kuolinAika = kuolinAika;
    }

    public String syntymaAikaToString() {
        StringBuilder sb = new StringBuilder("s. ");
        sb.append(syntymaAika.getDayOfMonth() + "." + syntymaAika.getMonthValue() + "." + syntymaAika.getYear());
        return sb.toString();
    }

    public String kuolinAikaToString() {
        StringBuilder sb = new StringBuilder("k. ");
        if (kuolinAika!=null) {
            sb.append(kuolinAika.getDayOfMonth() + "." + kuolinAika.getMonthValue() + "." + kuolinAika.getYear());
        } else {
            sb.append("-");
        }
        return sb.toString();
    }
}
