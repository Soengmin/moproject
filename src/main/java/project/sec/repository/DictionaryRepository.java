package project.sec.repository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Dictionary;

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
}
