package project.sec.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class member_dictionary {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    private Dictionary dictionary;

    int count;

    public member_dictionary(Member member, Dictionary dictionary) {
        this.member = member;
        this.dictionary = dictionary;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
