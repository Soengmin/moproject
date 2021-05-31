package project.sec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import project.sec.domain.Member;
import project.sec.repository.MemberRepository;
import project.sec.service.MailService;
import project.sec.service.MemberService;
import project.sec.service.MemberValidator;
import project.sec.service.MemberLoginValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberValidator memberValidator;
    private final MailService mailService;


    @GetMapping("/members/members")
    public String MemberList(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/members/signup")
    public String GMemberNew(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/signup";
    }

    @PostMapping("/members/signup")
    public String PMemberNew(@Valid MemberForm memberForm, Model model, Errors errors) {
        memberValidator.validate(memberForm, errors);
        //System.out.println(errors.hasErrors());
        if (!errors.hasErrors()) {
            memberForm.setAuth("ROLE_USER");
            Member member = new Member(memberForm);
            memberService.save(memberForm);
            return "redirect:/";
        }
        return "members/signup";
    }

    @GetMapping("/members/login")
    public String GMemberLogin(Model model) {
        model.addAttribute("memberLoginForm", new MemberLoginForm());
        return "members/login";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/home";
    }

    @ResponseBody
    @RequestMapping(value = "/members/inputSignupForm", method = RequestMethod.POST)
    public String certValue(@RequestParam(value = "email")String email) {
        return mailService.mailsend(email);
    }
}
