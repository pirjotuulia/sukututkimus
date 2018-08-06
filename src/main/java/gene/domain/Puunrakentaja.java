package gene.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Puunrakentaja {
    private HenkiloDAO henkiloDAO;

    public Puunrakentaja(@Autowired HenkiloDAO henkiloDAO) {
        this.henkiloDAO = henkiloDAO;
    }

    public List<Integer> palautaIsalinja(int id) {
        List<Integer> isalinja = new ArrayList<>();
        Integer linja = (Integer) id;
        while ((Integer) linja != null) {
            isalinja.add(linja);
            linja = henkiloDAO.haeVanhempi(linja, "isa");
        }
        return isalinja;
    }

    public List<Integer> palautaAitilinja(int id) {
        List<Integer> aitilinja = new ArrayList<>();
        Integer linja = (Integer) id;
        while ((Integer) linja != null) {
            aitilinja.add(linja);
            linja = henkiloDAO.haeVanhempi(linja, "aiti");
        }
        return aitilinja;
    }

    public List<Puumaja> luoPuuYlhaalta(List<Integer> ylalinja) {
        List<Puumaja> puumajat = new ArrayList<>();
        for (int i = ylalinja.size() - 1; i >= 0; i--) {//runko yksi sukupolvi kerrallaan
            //vanhempi
            Henkilo vanhempi = henkiloDAO.kaikkiTiedotIdlla(String.valueOf(ylalinja.get(i)));
            Puumaja vanhemmanPuumaja = onkoJoPuussa(vanhempi.getId(), puumajat);
            if (vanhemmanPuumaja == null) {
                vanhemmanPuumaja = new Puumaja(vanhempi.getId(), 0 - i, 0, 0, 0);//vanhempi (sukuun kuuluva)
                puumajat.add(vanhemmanPuumaja);
            }
            //puoliso
            if (vanhempi.puoliso()) {
                Puumaja puolisonPuumaja = onkoJoPuussa(vanhempi.getPuoliso(), puumajat);
                if (puolisonPuumaja == null) {
                    puolisonPuumaja = new Puumaja(vanhempi.getPuoliso(), 0 - i, 1, 0, 0);
                    int puolisonIndeksi = (puumajat.indexOf(vanhemmanPuumaja)) + 1;
                    puumajat.add(puolisonIndeksi, puolisonPuumaja);// vanhempi (sukuun naitu)
                }
            }
            if (i > 0) {
                int linjanSeuraavanId = ylalinja.get(i - 1);
                lisaaLapsetPuuhun(puumajat, vanhemmanPuumaja, linjanSeuraavanId);
            }
        }
        return puumajat;
    }

    private Puumaja onkoJoPuussa(int id, List<Puumaja> puumajat) {
        Optional<Puumaja> onkoJoPuussa = puumajat.stream().filter(p -> p.getId() == id).findAny();
        Puumaja puumaja = null;
        if (onkoJoPuussa.isPresent()) {
            puumaja = onkoJoPuussa.get();
        }
        return puumaja;
    }

    public void lisaaLapsetPuuhun(List<Puumaja> puumajat, Puumaja p, int linjanSeuraavanId) {
        if (p.getId() > 0) {
            List<Henkilo> lapset = henkiloDAO.haeLapset(String.valueOf(p.getId()));
            if (lapset.size() == 0) {
                return;
            }
            if (p.onNollasolu()) {
                int nollaLinjanIndeksi = -1;
                for (Henkilo lapsi : lapset) {
                    if (lapsi.getId() == linjanSeuraavanId) {
                        nollaLinjanIndeksi = lapset.indexOf(lapsi);
                    }
                }
                int sisarus = nollaLinjanIndeksi;
                for (int j = 0; j < lapset.size(); j++) {
                    if (onkoJoPuussa(lapset.get(j).getId(), puumajat) == null) {
                        puumajat.add(new Puumaja(lapset.get(j).getId(), p.getSukupolvi() + 1, 0, j - nollaLinjanIndeksi, p.getPystylinja()));//lapset
                    }
                    if (lapset.get(j).puoliso()&&onkoJoPuussa(lapset.get(j).getPuoliso(), puumajat) == null) {
                        puumajat.add(new Puumaja(lapset.get(j).getPuoliso(), p.getSukupolvi() + 1, 1, 0, p.getPystylinja()));
                    }
                }
            }
        }
    }

    public List<List<Puumaja>> puunSukupolvet(List<Puumaja> puumajat) {
        int eka = etsiMinTaiMaxSukupolvi(puumajat, "min").orElse(0);
        int vika = etsiMinTaiMaxSukupolvi(puumajat, "max").orElse(0);
        int sukupolvia = vika - eka + 1;
        int adapteri = -(0 - eka);
        List<List<Puumaja>> puuListat = new ArrayList<>();
        for (int i = 0; i < sukupolvia; i++) {
            puuListat.add(new ArrayList<>());
        }
        puumajat.stream().forEach(p -> {
            int listanIndeksi = (p.getSukupolvi() - adapteri);
            puuListat.get(listanIndeksi).add(p);
        });
        keskitaPuulistat(puuListat);
        return puuListat;
    }

    private List<List<Puumaja>> keskitaPuulistat(List<List<Puumaja>> puuListat) {
        int suurinNollasolunIndeksi = selvitäSuurinNollasolunIndeksi(puuListat);
        int tavoiteltuListanMitta = suurinNollasolunIndeksi * 2 + 1;
        int haluttuNollanIndeksi = tavoiteltuListanMitta / 2;
        List<Puumaja> pisin = puuListat.get(0);
        for (List<Puumaja> lista : puuListat) {
            if (lista.size() > pisin.size()) {
                pisin = lista;
            }
        }
        int pisimmanRivinNollanIndeksi = etsiRivinNollasolunIndeksi(pisin);
        while (pisimmanRivinNollanIndeksi != haluttuNollanIndeksi) {//jos nollasolu ei ole keskellä
            siirraNollasoluIndeksiin(pisin, pisimmanRivinNollanIndeksi, haluttuNollanIndeksi);
            pisimmanRivinNollanIndeksi = etsiRivinNollasolunIndeksi(pisin);
            haluttuNollanIndeksi = pisin.size() / 2;
        }
        for (List<Puumaja> lista : puuListat) {
            int rivinNollasolunIndeksi = etsiRivinNollasolunIndeksi(lista);
            siirraNollasoluIndeksiin(lista, rivinNollasolunIndeksi, haluttuNollanIndeksi);
            //TODO: toteuta se, että kaikkien listojen nollasolujen indeksit katsotaan ensin?
        }
        return puuListat;
    }

    private int selvitäSuurinNollasolunIndeksi(List<List<Puumaja>> puuListat) {
        int suurin = 0;
        for (List<Puumaja> lista : puuListat) {
            int nollasolunIndeksi = etsiRivinNollasolunIndeksi(lista);
            if (nollasolunIndeksi > suurin) {
                suurin = nollasolunIndeksi;
            }
        }
        return suurin;
    }

    private void siirraNollasoluIndeksiin(List<Puumaja> lista, int nykyinenNollanIndeksi, int haluttuNollanIndeksi) {
        int lisattavia = 0;
        if (nykyinenNollanIndeksi < haluttuNollanIndeksi) {
            lisattavia = haluttuNollanIndeksi - nykyinenNollanIndeksi;
            for (int i = 0; i < lisattavia; i++) {
                lista.add(0, new Puumaja());
            }
        } else if (nykyinenNollanIndeksi > haluttuNollanIndeksi) {
            lisattavia = (haluttuNollanIndeksi * 2 + 1) - lista.size();
            for (int i = 0; i < lisattavia; i++) {
                lista.add(new Puumaja());
            }
        }
    }

    private Puumaja etsiNollasolu(List<Puumaja> lista) {
        for (Puumaja p : lista) {
            if (p.onNollasolu()) {
                return p;
            }
        }
        return null;
    }

    private int etsiRivinNollasolunIndeksi(List<Puumaja> lista) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).onNollasolu()) {
                return i;
            }
        }
        return -1;
    }

    private OptionalInt etsiMinTaiMaxSukupolvi(List<Puumaja> puumajat, String paa) {
        if (paa.equals("min")) {
            return puumajat.stream().mapToInt(p -> p.getSukupolvi()).min();
        } else {
            return puumajat.stream().mapToInt(p -> p.getSukupolvi()).max();
        }
    }
}
