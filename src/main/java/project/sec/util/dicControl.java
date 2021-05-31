package project.sec.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.sec.domain.Dictionary;
import project.sec.domain.Member;
import project.sec.domain.Member_dictionary;
import project.sec.repository.DictionaryRepository;
import project.sec.repository.MemberRepository;
import project.sec.service.DictionaryService;
import project.sec.service.RecommendService;
import project.sec.service.VectorService;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class dicControl {
    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final DictionaryService dictionaryService;
    private final DictionaryRepository dictionaryRepository;
    private final VectorService vectorService;
    private final RecommendService recommendService;

    @GetMapping(value = "/check_dic")
    public String check(Model model) {
        Map<Integer, String> dictionaryMap = new HashMap<>();
        List<Dictionary> all = dictionaryService.findAll();
        for (Dictionary dictionary : all) {
            dictionaryMap.put(dictionary.getId().intValue(), dictionary.getWord());
        }

        model.addAttribute("dic", dictionaryMap);
        return "/check_dic";
    }

    @GetMapping(value = "/myDic")
    public String mydic(Model model, Authentication auth) {
        Long a = System.currentTimeMillis();
        String email = auth.getName();
        Member member = memberRepository.findByEmail(email).get(0);
        List<Member_dictionary> memberVector = dictionaryRepository.findMemberVector(member);
        HashMap<String, Integer> vec = new HashMap<>();

        for (Member_dictionary md : memberVector) {
            vec.put(md.getDictionary().getWord(), md.getCount());
        }
        model.addAttribute("mem_dic", vec);

        Long b = System.currentTimeMillis();
        System.out.println(b - a);
        //vectorService.cosSim(email);
        recommendService.userRecommend(email);
        return "/myDic";
    }
}
