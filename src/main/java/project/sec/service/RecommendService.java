package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.EvalList;
import project.sec.domain.Movie;
import project.sec.repository.EvalListRepository;
import project.sec.repository.MemberRepository;
import project.sec.repository.MovieRepository;
import scala.Int;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final VectorService vectorService;
    private final EvalListRepository evalListRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    public List<Movie> recommend(String email){
        List<String> list = vectorService.cosSim(email);
        List<EvalList> basicList = evalListRepository.findByMemberId(memberRepository.findByEmail(email).get(0));
        List<Long> movies = new ArrayList<>();
        List<Long> forUser = new ArrayList<>();
        for(String user : list){
            List<EvalList> evalLists = evalListRepository.findByMemberId(memberRepository.findByEmail(user).get(0));
            for(EvalList evalList : evalLists){
                if(!movies.contains(evalList.getMovie_id().getId())) {
                    //System.out.println(evalList.getMovie_id().getId());
                    movies.add(evalList.getMovie_id().getId());
                    if(movies.size()>5){
                        break;
                    }
                }
            }
            for(EvalList basic : basicList){
                movies.remove(basic.getMovie_id().getId());
            }
            if(movies.size() >4){
                break;
            }
        }
        List<Movie> mov = new ArrayList<>();
        for(Long movie : movies){
            mov.add(movieRepository.findById(movie).get());
        }
        return mov;
    }
}
