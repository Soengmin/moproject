package project.sec.repository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Dictionary;
import project.sec.domain.Member;
import project.sec.domain.Movie;
import project.sec.domain.member_dictionary;

import javax.persistence.EntityManager;
import javax.xml.bind.DatatypeConverterInterface;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictionaryRepository {
    private final EntityManager em;


    public void save(Dictionary dictionary) {
        em.persist(dictionary);
    }

    public List<Dictionary> findByWord(String word) {
        List<Dictionary> result = em.createQuery("select d from Dictionary d where d.word = :wo", Dictionary.class)
                .setParameter("wo", word)
                .getResultList();

        return result;
    }

    public List<Dictionary> findAll() {
        return em.createQuery("select d from Dictionary d", Dictionary.class)
                .getResultList();
    }

    public List<member_dictionary> findMemberVector(Member member) {
        return em.createQuery("select md from member_dictionary md where md.member = :MEM")
                .setParameter("MEM", member)
                .getResultList();
    }

}
