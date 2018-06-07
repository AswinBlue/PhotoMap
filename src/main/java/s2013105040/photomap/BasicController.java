package s2013105040.photomap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Controller
public class BasicController {
/*
	@RequestMapping("/")
	public String index(Model model) {

		//model.addAttribute("name", "Photomap");

		return "photo";
	}
*/
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    PhotosLoaded photoLoaded = ctx.getBean(PhotosLoaded.class);
    FlickrLoader flickrLoader = ctx.getBean(FlickrLoader.class);

    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(BasicController.class);

    @RequestMapping("/")
    public ModelAndView index(ModelMap model) {
        ModelAndView view = new ModelAndView("photo");
        //view.setViewName("photo");

        if (photoLoaded != null) {
            try {
                model.addAttribute("photos", mapper.writeValueAsString(photoLoaded));

                log.info("loaded photos : " + mapper.writeValueAsString(photoLoaded));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //view.addObject("photos",photoLoaded);
        return view;
    }

    @RequestMapping("/saved")
    public ModelAndView saved() {
        ModelAndView view = new ModelAndView();
        view.setViewName("saved");
        view.addObject("name", "IMAGE_NAME");

        return view;
    }

    @RequestMapping("/save")
    public boolean savePhotos(PhotoRepository repository) {

        //repository.save();
        return true;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        view.addObject("name", "IMAGE_NAME");

        return view;
    }


    @RequestMapping(value = "/load/FB", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object loadFBPhotos(@RequestBody List<FacebookForm> data) {
        photoLoaded.setPhotos(new ArrayList<PhotoInfo>());

        for (FacebookForm i : data) {
            System.out.printf("photo : " + i.toString());

            PhotoInfo photo = new PhotoInfo();
            photo.setFrom(i.from);
            photo.setPlace(i.place);
            photo.setURL(i.URL);
            photo.setTitle(i.title);
            photo.setTime(i.time);
            photo.setLat(i.lat);
            photo.setLng(i.lng);
            photoLoaded.getPhotos().add(photo);
        }
/*
        //System.out.printf("data: " + data.toString());
        for (HashMap<String,String> i : data) {
            System.out.printf("photo : " + i.toString());

            PhotoInfo photo = new PhotoInfo();
            photo.setFrom(i.get("from").toString());
            photo.setPlace(i.get("place").toString());
            photo.setURL(i.get("URL").toString());
            photo.setTitle(i.get("title").toString());
            photo.setTime(i.get("time").toString());
            photo.setLat(Float.parseFloat(i.get("lng").toString()));
            photo.setLng(Float.parseFloat(i.get("lat").toString()));
            photoLoaded.add(photo);
        }
*/
/*
        for (HashMap<String,String> i : data) {
            Map<String,String> list = mapper.convertValue(i, new TypeReference<HashMap<String,String>>(){});
            System.out.printf("photo : " + i);

            PhotoInfo photo = new PhotoInfo();
            photo.setFrom(list.get("from").toString());
            photo.setPlace(list.get("placeName").toString());
            photo.setURL(list.get("URL").toString());
            photo.setTitle(list.get("title").toString());
            photo.setTime(list.get("time").toString());
            photo.setLat(Float.parseFloat(list.get("lng").toString()));
            photo.setLng(Float.parseFloat(list.get("lat").toString()));
            photoLoaded.add(photo);
        }
*/
//////////
        Map<String,Integer> result= new HashMap<String, Integer>();
        result.put("result",photoLoaded.getPhotos().size());
        return result;
    }

    @RequestMapping(value = "/load/Flickr", method = RequestMethod.GET)
    public Object loadFlickrPhotos() {
        flickrLoader.loadImages();

/*
        PhotoList<Photo> photos;
        Photosets psets = null;
        try {
            psets = flickr.getPhotosetsInterface().getList("Julunu");
            System.out.println("Number of photosets: "+ psets.getTotal());
            PhotosetsInterface pi = flickr.getPhotosetsInterface();
            Photoset pset = pi.getInfo(setId);
            int count = pset.getPhotoCount();
            System.out.println("count: " + count);
            PhotoList<Photo> photoList  = pi.getPhotos(setId,30,1);
            for (Iterator<Photo> iter = photoList.iterator(); iter.hasNext();) {
                Photo photo = (Photo) iter.next();
                String id = photo.getId();
                String url = photo.getOriginalUrl();
                BufferedImage img = ImageIO.read(new URL(url));
                File file = new File("C://Users/acrace/" + photo.getTitle() + ".jpg");
                ImageIO.write(img, "jpg", file);
            }
        } catch (FlickrException e) {
            e.printStackTrace();
        }
        */
        return null;
    }


    @RequestMapping("/about")
    public ModelAndView about() {
        ModelAndView view = new ModelAndView();
        view.setViewName("about");
        view.addObject("name", "IMAGE_NAME");

        return view;
    }
}
