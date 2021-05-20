package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.Member;
import project.sec.domain.Member_dictionary;

import java.util.List;

@Repository
public interface MemberDictionaryRepository extends JpaRepository<Member_dictionary,Long> {
    List<Member_dictionary> findByMember(Member member);
}
