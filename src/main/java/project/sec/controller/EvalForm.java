package project.sec.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.sec.domain.Member;
import project.sec.domain.Movie;

@Getter @Setter
@NoArgsConstructor
public class EvalForm {
    Member member_id;
    Movie movie_id;
    int rating;

    public EvalForm(Member member_id, Movie movie_id, int rating){
        this.member_id = member_id;
        this.movie_id = movie_id;
        this.rating = rating;
    }
}
