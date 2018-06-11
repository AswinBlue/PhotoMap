package s2013105040.photomap;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoInfo {

    @Id
    private String URL;
    @Column
    private String time;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String source;
    @Column
    private String place;
    @Column
    private double lat;
    @Column
    private double lng;
    @Column
    private String postURL;

    public PhotoInfo(){}

    public void setContent(String content) {
        this.content = content;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setFrom(String from) {
        this.source = from;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return source;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public String getPostURL() {
        return postURL;
    }
}
