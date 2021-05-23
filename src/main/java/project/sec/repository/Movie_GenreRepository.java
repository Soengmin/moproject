package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.Genre;
import project.sec.domain.Member;
import project.sec.domain.Movie_Genre;

import java.util.List;

@Repository
public interface Movie_GenreRepository extends JpaRepository<Movie_Genre,Long> {
    //List<Movie_Genre> findByGenre_id(G)
    List<Movie_Genre> findByGenre_id(Long genre);
}
