package gene.genealogy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gene.domain.Henkilo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GeneController {
    @Value("${welcome.message:test}")
    private String message;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "index";
    }

    @RequestMapping("/hakutulos")
    @ResponseBody
    public String haku(Map<String, Object> model) {
        List<Henkilo> henkilot = new ArrayList<>();
        henkilot.add(new Henkilo("Pirjo", "Leppänen", LocalDate.of(1974, 2, 10)));
        model.put("henkilot", henkilot);
        return "hakutulos";
    }
}
