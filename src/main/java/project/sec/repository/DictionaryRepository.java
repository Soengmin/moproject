package project.sec.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.sec.domain.Dictionary;
import project.sec.domain.Member;
import project.sec.domain.Member_dictionary;

import javax.persistence.EntityManager;
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

    public List<Member_dictionary> findMemberVector(Member member) {
        return em.createQuery("select md from Member_dictionary md where md.member = :MEM")
                .setParameter("MEM", member)
                .getResultList();
    }

}
