package project.sec.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class member_movie {

    @GeneratedValue @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member_id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie_id;

    @Lob
    private String comment;
}
