package s2013105040.photomap;

import javax.persistence.*;

@Entity
public class AlbumInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long albumId;

    @Column
    private String name;

    @Column
    private String time;

    @Column
    private String ownerName;

    @Column
    private String  ownerID;
}
