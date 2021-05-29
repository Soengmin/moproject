package project.sec.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Movie;
import project.sec.repository.MovieRepository;
import sun.security.krb5.internal.EncTicketPart;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class MovieSearchService {

    private final MovieRepository movieRepository;
    private final EntityManager em;

    public List<Movie> search(String title){
        //Movie movie = movies.get(0);
        //System.out.println(movie.getTitle());
        return movieRepository.findByTitleContainingOrActorContainingOrDirectorContaining(title, title, title);
    }

    @Transactional
    @Modifying
    public List<Movie> createRandomTable(int sequence, String name) {
        if (sequence == 0) {
            String q = "drop table if exists " + name;
            em.createNativeQuery(q).executeUpdate();
        }
        String sql = "CREATE TEMPORARY TABLE IF NOT EXISTS " + name + " Select * From MOVIE order by rand()";
        int i = em.createNativeQuery(sql, Movie.class)
                .executeUpdate();

        sql = "select * from " + name;
        List<Movie> resultList = em.createNativeQuery(sql, Movie.class)
                .setFirstResult(sequence)
                .setMaxResults(20)
                .getResultList();
        return resultList;
    }
}
