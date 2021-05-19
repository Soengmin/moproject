package project.sec.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Dictionary {
    @Id @GeneratedValue
    private Long id;
    String word;

    public Dictionary(String word) {
        this.word = word;
    }
}
