package project.sec.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.sec.domain.Member;
import project.sec.repository.MemberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class dicControl {
    private final MemberRepository memberRepository;

    @GetMapping(value = "/check_dic")
    public String check(Model model) {
        Map<Integer, String> dictionaryMap = dictionary.getDictionaryMap();
        dictionaryMap.put(0, "ㅎㅇ");

        model.addAttribute("dic", dictionaryMap);
        return "/check_dic";
    }

    @GetMapping(value = "/myDic")
    public String mydic(Model model, Authentication auth) {
        String email = auth.getName();
        List<Member> byEmail = memberRepository.findByEmail(email);
        HashMap<String, Integer> member_vector = dictionary.get_member_vector(byEmail.get(0));
        model.addAttribute("mem_dic", member_vector);

        return "/myDic";
    }
}
