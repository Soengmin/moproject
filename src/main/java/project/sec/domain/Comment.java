package project.sec.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment {

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

    public Comment(Member member_id, Movie movie_id, String comment) {
        this.member_id = member_id;
        this.movie_id = movie_id;
        this.comment = comment;
    }
}
