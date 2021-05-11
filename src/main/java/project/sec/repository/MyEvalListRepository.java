package project.sec.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.EvalList;
import project.sec.domain.Member;
import project.sec.domain.Movie;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyEvalListRepository {
    private final EntityManager em;

    @Transactional
    public void save_eval(Member member, Movie movie, double rating) {
        System.out.println("====================");
        System.out.println(member.getId() + " " + movie.getId());
        List<EvalList> resultList = em.createQuery("select e from EvalList e where e.member_id = :memid and e.movie_id = :movid", EvalList.class)
                .setParameter("memid", member)
                .setParameter("movid", movie)
                .getResultList();
        if (resultList.size() == 1) {
            System.out.println("이미 평가한 영화");
            resultList.get(0).setScore(rating);
        }
        else if (resultList.size() == 0) {
            System.out.println("평가한 영화 추가");
            EvalList evalList = new EvalList(member, movie, rating);
            em.persist(evalList);
        }
    }
}
