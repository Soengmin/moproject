package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.Genre;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
}
