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
import org.springframework.beans.factory.annotation.Autowired;
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
    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(BasicController.class);

    @Autowired
    FlickrLoader flickrLoader;

    @Autowired
    private PhotoRepository photoRepository;

    @RequestMapping("/")
    public ModelAndView index(ModelMap model) {
        ModelAndView view = new ModelAndView("photo");
        //view.setViewName("photo");

/*
        Iterable<PhotoInfo> photoLoaded = photoRepository.findAll();

        if (photoLoaded != null) {
            try {
                String jsonString = mapper.writeValueAsString(photoLoaded);
                model.addAttribute("photos", jsonString);

                log.info("loaded photos : " + jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
*/
        //view.addObject("photos",photoLoaded);
        return view;
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces="application/json")
    public Object get() {
        String jsonString = null;
        Iterable<PhotoInfo> photoLoaded = photoRepository.findAll();
        try {
           jsonString = mapper.writeValueAsString(photoLoaded);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @RequestMapping("/saved")
    public ModelAndView saved() {
        ModelAndView view = new ModelAndView();
        view.setViewName("saved");

        return view;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        String url = flickrLoader.presetToken();
        view.addObject("url", url);

        return view;
    }


    @RequestMapping(value = "/load/FB", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object loadFBPhotos(@RequestBody List<FacebookForm> data) {
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
            photoRepository.save(photo);
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
        result.put("result",data.size());
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/load/Flickr", method = RequestMethod.POST)
    public String loadFlickrPhotos(@RequestBody String data) {
        //remove postfix '='
        String token = data.substring(0,data.length()-1);

        try {
            ArrayList<PhotoInfo> result = flickrLoader.loadImages(token);
            for(PhotoInfo i : result){
                photoRepository.save(i);
            }
        } catch (FlickrException e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

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


    @RequestMapping("/about")
    public ModelAndView about() {
        ModelAndView view = new ModelAndView();
        view.setViewName("about");

        return view;
    }
}
