package project.sec.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.Movie;
import project.sec.repository.MovieRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class MovieSearchService {

    private final MovieRepository movieRepository;

    public List<Movie> search(String title){
        //Movie movie = movies.get(0);
        //System.out.println(movie.getTitle());
        return movieRepository.findByTitleContainingOrActorContainingOrDirectorContaining(title, title, title);
    }
}
