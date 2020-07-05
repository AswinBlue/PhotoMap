package s2013105040.photomap;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;
import java.util.List;

public interface PhotoRepository extends CrudRepository<PhotoInfo,Long> {
        ArrayList<PhotoInfo> findByLatAndLng(float lat,float lng);
        ArrayList<PhotoInfo> findByTitleContaining(String str);
        ArrayList<PhotoInfo> findByContentContaining(String str);
        ArrayList<PhotoInfo> findBySourceContaining(String str);
        ArrayList<PhotoInfo> findByPlaceContaining(String str);
}
