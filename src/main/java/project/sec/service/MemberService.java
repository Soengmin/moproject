package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import project.sec.controller.MemberForm;
import project.sec.domain.Member;
import project.sec.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final EntityManager em;
    private final MemberRepository memberRepository;

    public void jungbokNicName(String name, Errors errors) {
        List<Member> find = em.createQuery("select m from Member m where m.nicName = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        if (!find.isEmpty()) {
            errors.rejectValue("nicname", "key", "이미 사용자 이름이 존재합니다.");
        }
    }

    public boolean jungbokEmail(String email) {
        List<Member> find = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return find.isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> findname = memberRepository.findByEmail(username);
        if (findname.size() == 1) return findname.get(0);
        else {
            new UsernameNotFoundException(username);
            return null;
        }
    }

    public Long save(MemberForm memberForm) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberForm.setPassword(encoder.encode(memberForm.getPassword()));

        return memberRepository.save(Member.builder()
                .email(memberForm.getEmail())
                .nicName(memberForm.getNicname())
                .auth(memberForm.getAuth())
                .password(memberForm.getPassword()).build()).getId();
    }
}
