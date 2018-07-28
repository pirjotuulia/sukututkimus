package gene.genealogy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import gene.domain.Henkilo;
import gene.domain.HenkiloDAO;
import gene.domain.Puumaja;
import gene.domain.Puunrakentaja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GeneController {
    @Value("${welcome.message:test}")
    private String message;
    private List<Henkilo> henkilot;
    private HenkiloDAO henkiloDAO;
    private Puunrakentaja pr;

    public GeneController(@Autowired HenkiloDAO henkiloDAO, Puunrakentaja pr) {
        this.henkiloDAO = henkiloDAO;
        this.pr = pr;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        henkilot = henkiloDAO.kaikkiHenkilot();
        model.addAttribute("henkilot", henkilot);
        return "index";
    }

    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String lisaaSivulle(Model model) {
        model.addAttribute("lisaysohje", "Alla voit lisätä tietokantaan uusia suvun jäseniä.");
        return "lisatty";
    }

    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String lisaa(@RequestParam String etunimi, String sukunimi, String syntymaaika, Model model) {
        Henkilo lisattava = new Henkilo(etunimi, sukunimi, LocalDate.parse(syntymaaika));
        boolean lisatty = henkiloDAO.lisaaHenkilo(lisattava);
        model.addAttribute("onnistui", lisatty);
        if (lisatty) {
            model.addAttribute("lisatty", lisattava);
        } else {
            model.addAttribute("virhe", "Lisääminen ei onnistunut, koska henkilö on jo tietokannassa.");
            model.addAttribute("loytynyt", henkiloDAO.haeHenkilo(lisattava));
        }
        return "lisatty";
    }

    @RequestMapping(value = "/henkilokortti/{id}", method = RequestMethod.GET)
    public String naytaHenkilo(@PathVariable String id, Model model) {
        Henkilo henkilo = henkiloDAO.kaikkiTiedotIdlla(id);
        model.addAttribute("henkilo", henkilo);
        return "henkilokortti";
    }

    @RequestMapping(value = "/henkilokortti/{id}", method = RequestMethod.POST)
    public String lisaaTietoja(@PathVariable String id, @RequestParam Map<String, String> arvot, Model model) {
        Henkilo paivitettava = henkiloDAO.paivitaHenkilo(id, arvot);
        model.addAttribute("lisatty", paivitettava);
        return "redirect:/henkilokortti/{id}";
    }

    @RequestMapping(value = "/paivita/{id}", method = RequestMethod.GET)
    public String naytaPaivita(@PathVariable String id, Model model) {
        Henkilo henkilo = henkiloDAO.kaikkiTiedotIdlla(id);
        model.addAttribute("henkilo", henkilo);
        return "paivita";
    }

    @RequestMapping(value = "/paivita/{id}", method = RequestMethod.POST)
    public String paivita(@PathVariable String id, @RequestParam Map<String, String> paivitettavat, Model model) {
        Henkilo paivitettava = henkiloDAO.paivitaHenkilo(id, paivitettavat);
        model.addAttribute("lisatty", paivitettava);
        return "redirect:/henkilokortti/{id}";
    }

    @RequestMapping(value="/haku", method = RequestMethod.GET)
    public String haku(@RequestParam(required = false) String hakusana, Model model) {
        if (hakusana== null) {
            hakusana="";
        }
        model.addAttribute("hakusana", hakusana);
        if (hakusana.contains("[0-9]")) {
//            haeSyntymaAjalla();
        } else {
            List<Henkilo> haetut = henkiloDAO.haetutHenkilot(hakusana);
            if (haetut.isEmpty()) {
                model.addAttribute("henkilot", "Hakuasi vastaavia henkilöitä ei löytynyt");
            } else {
                model.addAttribute("henkilot", haetut);
            }
        }
        return "hakutulos";
    }

    @RequestMapping(value = "/poista/{id}", method = RequestMethod.GET)
    public String poistoyritys(@PathVariable String id, Model model) {
        Henkilo henkilo = henkiloDAO.kaikkiTiedotIdlla(id);
        model.addAttribute("henkilo", henkilo);
        model.addAttribute("poistoyritys", true);
        return "paivita";
    }

    @RequestMapping(value = "/poista/{id}/varmistus", method = RequestMethod.GET)
    public String poista(@PathVariable String id, Model model) {
        henkiloDAO.poistaHenkilo(id);
        model.addAttribute("poistettu", "Henkilötiedot id:llä " + id + " on poistettu tietokannasta.");
        return "redirect:/haku?hakusana=";
    }

    @RequestMapping(value="/puu/{id}", method = RequestMethod.GET)
    public String naytaPuu(@PathVariable int id, Model model) {
        List<Integer> isalinja = pr.palautaIsalinja(id);
        List<Puumaja> puumajat = pr.luoPuuYlhaalta(isalinja);
        List<List<Puumaja>> puuListat = pr.puunSukupolvet(puumajat);
        Map<Puumaja, Henkilo> henkilotPuussa = henkiloDAO.henkilotPuussa(puuListat);
        model.addAttribute("puuListat", puuListat);
        model.addAttribute("henkilotPuussa", henkilotPuussa);
        return "puu";
    }
}
