package project.sec.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class Genre {
    @Id @GeneratedValue
    private Long id;

    private String genre;

    public Genre(String genre) {
        this.genre = genre;
    }
}
