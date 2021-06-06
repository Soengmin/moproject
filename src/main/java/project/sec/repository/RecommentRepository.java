package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.Comment;
import project.sec.domain.Recomment;

import java.util.List;

@Repository
public interface RecommentRepository extends JpaRepository<Recomment,Long> {
    List<Recomment> findByComment(Comment comment);
}
