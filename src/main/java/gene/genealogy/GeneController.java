package gene.genealogy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import gene.domain.Henkilo;
import gene.domain.HenkiloDAO;
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

    public GeneController(@Autowired HenkiloDAO henkiloDAO) {
        this.henkiloDAO = henkiloDAO;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        henkilot = henkiloDAO.kaikkiHenkilot();
        model.addAttribute("henkilot", henkilot);
        return "index";
    }

    @RequestMapping(value = "/lisatty", method = RequestMethod.POST)
    public String lisaa(@RequestParam String etunimi, String sukunimi, String syntymaaika, Model model) {
        Henkilo lisattava = new Henkilo(etunimi, sukunimi, LocalDate.parse(syntymaaika));
        lisattava = henkiloDAO.lisaaHenkilo(lisattava);
        model.addAttribute("lisatty", lisattava);
        return "lisatty";
    }

    @RequestMapping(value = "/henkilokortti/{id}", method = RequestMethod.GET)
    public String naytaHenkilo(@PathVariable String id, Model model) {
        Henkilo henkilo = henkiloDAO.haeHenkiloIdlla(id);
        henkiloDAO.lisaaVanhemmatHenkiloina(henkilo);
        model.addAttribute("henkilo", henkilo);
        return "henkilokortti";
    }

    @RequestMapping(value = "/henkilokortti/{id}", method = RequestMethod.POST)
    public String lisaaTietoja(@PathVariable String id, @RequestParam Map<String, String> arvot, Model model) {
        Henkilo paivitettava = henkiloDAO.haeHenkiloIdlla(id);
        paivitettava = henkiloDAO.paivitaHenkilo(paivitettava, arvot);
        model.addAttribute("lisatty", paivitettava);
        return "redirect:/henkilokortti/{id}";
    }

    @RequestMapping(value = "/paivita/{id}", method = RequestMethod.GET)
    public String naytaPaivita(@PathVariable String id, Model model) {
        Henkilo henkilo = henkiloDAO.haeHenkiloIdlla(id);
        henkiloDAO.lisaaVanhemmatHenkiloina(henkilo);
        model.addAttribute("henkilo", henkilo);
        return "paivita";
    }

    @RequestMapping(value = "/paivita/{id}", method = RequestMethod.POST)
    public String paivita(@PathVariable String id, @RequestParam Map<String, String> paivitettavat, Model model) {
        Henkilo paivitettava = henkiloDAO.haeHenkiloIdlla(id);
        paivitettava = henkiloDAO.paivitaHenkilo(paivitettava, paivitettavat);
        model.addAttribute("lisatty", paivitettava);
        return "redirect:/henkilokortti/{id}";
    }

    @RequestMapping("/hakutulos")
    public String haku(@RequestParam String hakusana, Model model) {
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
}
