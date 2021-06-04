package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.controller.EvalForm;
import project.sec.domain.EvalList;
import project.sec.domain.Member;
import project.sec.domain.Movie;
import project.sec.repository.EvalListRepository;
import project.sec.repository.MemberRepository;
import project.sec.repository.MovieRepository;
import project.sec.repository.MyEvalListRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final EvalListRepository evalListRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final MyEvalListRepository myEvalListRepository;
    private final DictionaryService dictionaryService;
    private final EntityManager em;

    public void save(String email,Long m_id, double rating){
        Member member = memberRepository.findByEmail(email).get(0);
        movieRepository.findById(m_id);
        Optional<Movie> movie = movieRepository.findById(m_id);
        Movie movie1 = movie.get();

        //===========여기에 member_dictionary 추가
        int result = myEvalListRepository.save_eval(member, movie1, rating);
        // result : 1이면 이미 평가한 영화, 0이면 한번도 평가하지 않은 영화
        if (result == 0) dictionaryService.saveMovieMemberDic(member, movie1,(int)rating);
    }

    public Movie findById(Long id) {
        Optional<Movie> find = movieRepository.findById(id);
        return find.get();
    }

    public int getEvelCount(String user) {
        return em.createQuery("select e from EvalList e where e.memberId.email = :email", EvalList.class)
                .setParameter("email", user)
                .getResultList()
                .size();
    }
}
