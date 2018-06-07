package s2013105040.photomap;

import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;
import java.util.List;

public interface PhotoRepository extends CrudRepository<PhotoInfo,Long> {
        ArrayList<PhotoInfo> findByLatAndLng(float lat,float lng);
        ArrayList<PhotoInfo> findByAlbum(String album);
}
