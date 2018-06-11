package s2013105040.photomap;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;

@SpringBootApplication
public class Booter  implements CommandLineRunner {
    @Autowired
    HandleDatabase hd = new HandleDatabase();

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Booter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //hd.dropDatabase();
        hd.createDatabase();
    }
}
