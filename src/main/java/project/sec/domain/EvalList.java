package project.sec.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.sec.controller.EvalForm;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EvalList {

    @GeneratedValue
    @Id
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie_id;

    @Column(nullable = false)
    private double score;

    public EvalList(Member member,Movie movie,double score){
        this.memberId = member;
        this.movie_id = movie;
        this.score = score;
    }

    public void setScore(double score){
        this.score=score;
    }
}
