package gene.genealogy;

import gene.domain.Puumaja;
import gene.domain.Puunrakentaja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan("gene")
public class GenealogyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenealogyApplication.class, args);
    }

    @Bean
    public CommandLineRunner kokeilu(@Autowired Puunrakentaja pr){
        return args -> {
//            List<Integer> isalinja = pr.palautaIsalinja(23);
//            System.out.println("haettu");
//            System.out.println(isalinja);
//            List<Puumaja> puumajat = pr.luoPuuYlhaalta(isalinja);
//            System.out.println(puumajat);
        };
    }
}
