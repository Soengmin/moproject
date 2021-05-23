package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.EvalList;
import project.sec.domain.Member;
import project.sec.domain.Movie;

import java.util.List;

@Repository
public interface EvalListRepository extends JpaRepository<EvalList,Long> {
    List<EvalList> findByMemberId(Member memberId);
}
