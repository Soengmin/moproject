package project.sec.repository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Movie;
import project.sec.domain.newestMovie;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewestMovieRepository {
    private final EntityManager em;

    public List<Movie> get_newest_movie () {
        List<newestMovie> ret = em.createQuery("select m from newestMovie m", newestMovie.class)
                .getResultList();
        List<Movie> movieList = new ArrayList<>();
        for (newestMovie g : ret) {
            movieList.add(g.getMovie());
        }

        return movieList;
    }

    @Transactional
    public void delete_all() {
        List<newestMovie> ret = em.createQuery("select m from newestMovie m", newestMovie.class)
                .getResultList();
        for (newestMovie newestMovie : ret) {
            em.remove(newestMovie);
        }
    }

    @Transactional
    public void save(Movie movie) {
        newestMovie mo = new newestMovie(movie);
        em.persist(mo);
    }
}
