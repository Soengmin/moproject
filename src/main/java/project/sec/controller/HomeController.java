package project.sec.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.sec.domain.Member;
import project.sec.repository.MemberRepository;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(Authentication auth, Model model){
        Member member = memberRepository.findByEmail(auth.getName()).get(0);
        if (member.getAuth().equals("ROLE_NOTYET")) return "/movies/first_eval";
        model.addAttribute("member",member);
        return "home";
    }
}
