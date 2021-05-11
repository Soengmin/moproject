package project.sec.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Movie_Genre {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Genre genre;

    public Movie_Genre(Movie movie_id) {

        this.movie = movie_id;
        //this.genre_id = genre;
    }


    public void setGenre_id(Genre genre_id) {
        this.genre = genre_id;
    }

}
