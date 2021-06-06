package project.sec.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class Movie {

    @GeneratedValue @Id
    private Long id;

    private String title;

    @OneToMany(mappedBy = "movie")
    @JsonManagedReference
    private List<Movie_Genre> genreList = new ArrayList<>();

    private String link;

    @Lob
    private String outline;

    private String director;
    private String actor;
    private String img_link;
    private int OpeningDate;
    private String country;

    public Movie(String title, String link) {
        this.title = title;
        this.link = link;
    }

}
