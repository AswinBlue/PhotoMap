package s2013105040.photomap;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.*;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.tags.Tag;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlickrLoader {
    private static String path = "/resources/pics";
    private static Preferences userPrefs = Preferences.userNodeForPackage(FlickrLoader.class);
    private static final Logger log = LoggerFactory.getLogger(BasicController.class);

    private String apikey = "2a69f74a6bdc7cb9d5768e318f45db9b";
    private String secret = "83b1cbe7f23dddba";
    private Flickr flickr = new Flickr(apikey, secret, new REST());

    private Token token = null;
    private Token requestToken = null;

    /*// convert filename to clean filename
    public static String convertToFileSystemChar(String name) {
        String erg = "";
        Matcher m = Pattern.compile("[a-z0-9 _#&@\\[\\(\\)\\]\\-\\.]", Pattern.CASE_INSENSITIVE).matcher(name);
        while (m.find()) {
            erg += name.substring(m.start(), m.end());
        }
        if (erg.length() > 200) {
            erg = erg.substring(0, 200);
            System.out.println("cut filename: " + erg);
        }
        return erg;
    }

    public static boolean saveImage(Flickr f, Photo p) {

        String cleanTitle = convertToFileSystemChar(p.getTitle());

        File orgFile = new File(path + File.separator + cleanTitle + "_" + p.getId() + "_o." + p.getOriginalFormat());
        File largeFile = new File(path + File.separator + cleanTitle + "_" + p.getId() + "_b." + p.getOriginalFormat());

        if (orgFile.exists() || largeFile.exists()) {
            System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " skipped!");
            return false;
        }

        try {
            Photo nfo = f.getPhotosInterface().getInfo(p.getId(), null);
            if (nfo.getOriginalSecret().isEmpty()) {
                ImageIO.write(p.getLargeImage(), p.getOriginalFormat(), largeFile);
                System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " was written to " + largeFile.getName());
            } else {
                p.setOriginalSecret(nfo.getOriginalSecret());
                ImageIO.write(p.getOriginalImage(), p.getOriginalFormat(), orgFile);
                System.out.println(p.getTitle() + "\t" + p.getOriginalUrl() + " was written to " + orgFile.getName());
            }
        } catch (FlickrException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }*/

    public String presetToken() {

        Flickr.debugStream = false;
        //authenticate
        AuthInterface authInterface = flickr.getAuthInterface();
        if (token == null) {
            token = authInterface.getRequestToken();
            log.info("token : " + token.getToken());
        }
        //url
        return authInterface.getAuthorizationUrl(token, Permission.DELETE);
    }

    public ArrayList<PhotoInfo> loadImages(String tokenKey) throws FlickrException {

        ArrayList<PhotoInfo> photoInfoArrayList = new ArrayList<>();

        AuthInterface authInterface = flickr.getAuthInterface();

        if (requestToken == null)
            requestToken = authInterface.getAccessToken(token, new Verifier(tokenKey));

        System.out.println("Authentication success");

        Auth auth = authInterface.checkToken(requestToken);

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);

        //get user's photos
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setUserId(auth.getUser().getId());
        searchParameters.setPrivacyFilter(5);
        searchParameters.setHasGeo(true);
        searchParameters.setAccuracy(1);


        PhotosInterface photosInterface = flickr.getPhotosInterface();
        PhotoList<Photo> photolist = photosInterface.search(searchParameters, 100, 1);
        for (Photo i : photolist) {
            PhotoInfo photoInfo = new PhotoInfo();
            log.info(i.getId());
            Photo photo = photosInterface.getPhoto(i.getId());
            if (photo.hasGeoData()) {
                photoInfo.setLat(photo.getGeoData().getLatitude());
                photoInfo.setLng(photo.getGeoData().getLongitude());

                String place = "";
                if (photo.getCountry() != null)
                    place += photo.getCountry().getName();
                if (photo.getRegion() != null)
                    place += " " + photo.getRegion().getName();
                if (photo.getLocality() != null)
                    place += " " + photo.getLocality().getName();

                photoInfo.setPlace(place);
            } else {
                photoInfo.setLat(999);
                photoInfo.setLng(999);
            }
            photoInfo.setTitle(i.getTitle());
            photoInfo.setFrom("Flickr");

            photoInfo.setTime(photo.getDateTaken().toString());
            photoInfo.setContent(photo.getDescription());

            List<Size> size = new ArrayList(photosInterface.getSizes(i.getId()));
            photoInfo.setURL(size.get(5).getSource());
            photoInfo.setPostURL(photo.getUrl());

            photoInfoArrayList.add(photoInfo);

        }

        return photoInfoArrayList;
    }
}

//find user by name
//            User user = flickr.getPeopleInterface().findByUsername(auth.getUser().getUsername());

////////////////////////////////////////
            /*
            OAuthService service = new ServiceBuilder()
                    .provider(FlickrApi.class)
                    .apiKey(apikey)
                    .apiSecret(secret)
                    .build();
            Token flickReq = service.getRequestToken();
            String authUrl = service.getAuthorizationUrl(flickReq);
            try{
                OAuthRequest req = new OAuthRequest(Verb.POST, "https://api.flickr.com/services/rest/");
                req.addQuerystringParameter("method", "flickr.test.login");
                service.signRequest(flickReq, req);
                Response resp = req.send();
                System.out.println(resp.getBody());
            */
////////////////////////////////////////

/*
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setAccuracy(1);
        StringBuilder tagsBuilder = new StringBuilder();
        for (String tmp : args) {
            tagsBuilder.append(" " + tmp);
        }
        path = "/pics" + File.separator + tagsBuilder.toString().substring(1);

        new File(path).mkdirs();
        searchParameters.setTags(args);

        for (int i = userPrefs.getInt(path, 0); true; i++) {
            userPrefs.putInt( path, i );
            System.out.println("\tcurrent page: " + userPrefs.getInt(path, 0));
            try {
                PhotoList<Photo> list = flickr.getPhotosInterface().search(searchParameters, 500, i);
}
                if (list.isEmpty())
                    break;

                Iterator itr = list.iterator();
                while (itr.hasNext()) {
                    saveImage(flickr, (Photo) itr.next());
                }
            } catch (FlickrException e) {
                e.printStackTrace();
            }
        }
*/

