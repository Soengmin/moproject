package project.sec.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.Genre;
import project.sec.domain.Member;
import project.sec.domain.Movie;
import project.sec.domain.Movie_Genre;
import project.sec.repository.GenreRepository;
import project.sec.repository.MovieRepository;
import project.sec.repository.Movie_GenreRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class Movie_GenreService {
    private final Movie_GenreRepository movie_genreRepository;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final EntityManager em;

    public List<Movie> findMovie(Long genre_id, int sequence){
        Genre genre = genreRepository.getOne(genre_id);
        System.out.println(genre.getGenre());

        List<Movie> movies = new ArrayList<>();

        List<Object[]> find = em.createQuery("select m, g from Movie_Genre g join g.movie m where g.genre = :gen")
                .setParameter("gen", genre)
                .setFirstResult(sequence)
                .setMaxResults(20)
                .getResultList();

        for (Object[] o : find) {
            movies.add((Movie)o[0]);
        }

        return movies;
    }
}
