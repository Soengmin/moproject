package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByTitleContaining(String title);
    List<Movie> findByTitleContainingOrActorContainingOrDirectorContaining(String title, String title1,String title2);
    List<Movie> findTop50ByOrderByIdDesc();
    //List<Movie> findByTitleAndActorAndDirectorContaining(String title);
}
