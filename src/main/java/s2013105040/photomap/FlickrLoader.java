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
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlickrLoader {
    private static String path = "/resources/pics";
    private static Preferences userPrefs = Preferences.userNodeForPackage(FlickrLoader.class);
    private static final Logger log = LoggerFactory.getLogger(BasicController.class);

    // convert filename to clean filename
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
    }

    public static void loadImages() {
        //args : {  }
        String apikey = "2a69f74a6bdc7cb9d5768e318f45db9b";
        String secret = "83b1cbe7f23dddba";

        Flickr flickr = new Flickr(apikey, secret, new REST());
        Flickr.debugStream = false;
        try {
            //authenticate
            AuthInterface authInterface = flickr.getAuthInterface();
            Token token = authInterface.getRequestToken();
            log.info("token : " + token.getToken());
            String url = authInterface.getAuthorizationUrl(token, Permission.DELETE);

            System.out.println("Follow this URL to authorise yourself on Flickr");
            System.out.println(url);
            System.out.println("Paste in the token it gives you:");
            System.out.print(">>");

            Scanner scanner = new Scanner(System.in);
            String tokenKey = scanner.nextLine();
            scanner.close();
//            scanner.remove();
            Token requestToken = authInterface.getAccessToken(token, new Verifier(tokenKey));
            System.out.println("Authentication success");

            Auth auth = authInterface.checkToken(requestToken);

            RequestContext requestContext = RequestContext.getRequestContext();
            requestContext.setAuth(auth);
            flickr.setAuth(auth);

            // This token can be used until the user revokes it.
            System.out.println("Token: " + requestToken.getToken());
            System.out.println("Secret: " + requestToken.getSecret());
            System.out.println("nsid: " + auth.getUser().getId());
            System.out.println("Realname: " + auth.getUser().getRealName());
            System.out.println("Username: " + auth.getUser().getUsername());
            System.out.println("Permission: " + auth.getPermission().getType());

//            log.info(token);faang
            //get user's photos
            SearchParameters searchParameters = new SearchParameters();
            searchParameters.setUserId(auth.getUser().getId());
            searchParameters.setPrivacyFilter(5);
            searchParameters.setHasGeo(true);
            searchParameters.setAccuracy(1);

            PhotoList<Photo> photolist = flickr.getPhotosInterface().search(searchParameters, 100, 1);
//            PhotoList<Photo> photolist = flickr.getPeopleInterface().getPhotos(auth.getUser().getId(),null,null,null,null,null,null,null,null,100,1);

            for (Photo i : photolist) {
                log.info(i.getTitle().toString());
                GeoData geo = flickr.getGeoInterface().getLocation(i.getId());
                if(geo != null){
                    float lat = geo.getLatitude();
                    float lng = geo.getLongitude();
                }

                if (i.hasGeoData()) {
                    float lat = i.getGeoData().getLatitude();
                    float lng = i.getGeoData().getLongitude();
                }
                Collection<Tag> tags = i.getTags();
                for (Tag j : tags){
                    System.out.println("value : "+ j.getValue());
                    System.out.println("Raw : " +j.getRaw());
                    System.out.println("id"+j.getId());
                }


            }
//            PhotoList<Photo> photolist = flickr.getPhotosInterface().getWithGeoData(null,null,null,null,0,null,null,10,i);
        } catch (FlickrException e) {
            e.printStackTrace();
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
}
