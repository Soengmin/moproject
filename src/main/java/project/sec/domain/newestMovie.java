package project.sec.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class newestMovie {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public newestMovie(Movie movie) {
        this.movie = movie;
    }
}
