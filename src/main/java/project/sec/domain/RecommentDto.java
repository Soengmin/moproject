package project.sec.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecommentDto {
    private String memberName;
    private String content;

    public RecommentDto(String memberName, String content){
        this.memberName = memberName;
        this.content = content;
    }
}
