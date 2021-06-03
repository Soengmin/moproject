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
