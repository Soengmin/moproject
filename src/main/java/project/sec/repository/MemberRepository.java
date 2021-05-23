package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.sec.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {


    List<Member> findBynicName(String nicname);
    List<Member> findByEmail(String email);
    List<Member> findByPassword(String password);
}
