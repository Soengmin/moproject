package project.sec.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter @Setter
public class CommentDTO {
    private Long commentId;
    private Long memberId;
    private String memberName;
    private String comment;
    private int is_my_comment;

    public CommentDTO(Long memberId ,String memberName, String comment, int is_my_comment, Long commentId) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.comment = comment;
        this.is_my_comment = is_my_comment;
        this.commentId = commentId;
    }
}
