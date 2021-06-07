package project.sec.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecommentDto {
    private String memberName;
    private String content;
    private Long id;

    public RecommentDto(Long id,String memberName, String content){
        this.id = id;
        this.memberName = memberName;
        this.content = content;
    }
}
