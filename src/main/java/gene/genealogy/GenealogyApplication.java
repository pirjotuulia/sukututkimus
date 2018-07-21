package gene.genealogy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("gene")
public class GenealogyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenealogyApplication.class, args);
    }
}
