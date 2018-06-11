package s2013105040.photomap;

import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    FlickrLoader flickrLoader(){
        return new FlickrLoader();
    }
    @Bean
    HandleDatabase handleDatabase(){
        return new HandleDatabase();
    }

}
