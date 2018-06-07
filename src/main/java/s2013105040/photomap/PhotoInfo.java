package s2013105040.photomap;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String time;
    @Column
    private String title;
    @Column
    private String URL;
    @Column
    private String content;
    @Column
    private String album;
    @Column
    private String from;
    @Column
    private String place;
    @Column
    private int height;
    @Column
    private int width;
    @Column
    private float lat;
    @Column
    private float lng;


    public PhotoInfo(String URL, String time, String title, String content, String album, int height, int width, float lat, float lng){}
    public PhotoInfo(){}

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
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

    public void setWidth(int width) {
        this.width = width;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getAlbum() {
        return album;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
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
}
