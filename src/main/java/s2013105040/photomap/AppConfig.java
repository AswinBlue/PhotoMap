package s2013105040.photomap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    FlickrLoader flickrLoader(){
        return new FlickrLoader();
    }
    @Bean
    PhotosLoaded photosLoaded(){
        return new PhotosLoaded();
    }

}
