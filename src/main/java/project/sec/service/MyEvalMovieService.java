package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.EvalList;
import project.sec.domain.Member;
import project.sec.domain.Movie;
import project.sec.repository.EvalListRepository;
import project.sec.repository.MemberRepository;
import project.sec.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyEvalMovieService {
    private final MemberRepository memberRepository;
    private final EvalListRepository evalListRepository;
    private final MovieRepository movieRepository;

    public List<Movie> find(String email){
        List<Member>members = memberRepository.findByEmail(email);
        Member member = members.get(0);
        List<EvalList> evalLists = evalListRepository.findByMemberId(member);
        List<Movie> movies = new ArrayList<>();
        for(EvalList evalList : evalLists){
            movies.add(evalList.getMovie_id());
        }
        return movies;
    }
}
