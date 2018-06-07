package s2013105040.photomap;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Booter {
    public static void main(String[] args) {
        SpringApplication.run(Booter.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(Application.class);
}
/*
    @Bean
    public CommandLineRunner demo(PhotoRepository repository) {
        return (args) -> {
            // save a couple of customers
            //repository.save(new PhotoInfo("Jack", "Bauer"));
            //repository.save(new PhotoInfo("Chloe", "O'Brian"));
            //repository.save(new PhotoInfo("Kim", "Bauer"));
            //repository.save(new PhotoInfo("David", "Palmer"));
            //repository.save(new PhotoInfo("Michelle", "Dessler"));


        };
// fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        repository.findById(1L).ifPresent(customer -> {
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");
        });

        // fetch customers by last name
        log.info("Customer found with findByLastName('Bauer'):");
        log.info("--------------------------------------------");
        repository.findByLastName("Bauer").forEach(bauer -> {log.info(bauer.toString()); });
        // for (Customer bauer : repository.findByLastName("Bauer")) {
        //	 log.info(bauer.toString());
        // }
        log.info("");
    };
}

/*

    private static final Logger Log = LoggerFactory.getLogger(Booter.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... arg0) throws Exception {
        Log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
// Split up the array of whole names into an array of first/last names
        List<Object[]> splitupNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
                        .stream()
                        .map(name -> name.split(" "))
                        .collect(Collectors.toList());
// Use a Java 8 stream to print out each tuple of the list
        splitupNames.forEach(name -> Log.info(String.format("Inserting customer record for %s %s",
                name[0], name[1])));
// Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?, ?)",
                splitupNames);
        Log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new
                        Object[]{"Josh"},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
                        rs.getString("last_name")
                )).forEach(customer -> Log.info(customer.toString()));
    }
*/
