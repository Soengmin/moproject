package project.sec.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecommentDto {
    private String memberName;
    private String content;
    private Long id;
    private String otherNic;

    public RecommentDto(Long id,String memberName, String content,String otherNic){
        this.id = id;
        this.memberName = memberName;
        this.content = content;
        this.otherNic = otherNic;
    }
}
