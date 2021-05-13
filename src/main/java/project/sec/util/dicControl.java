package project.sec.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class dicControl {

    @GetMapping(value = "/check_dic")
    public String check(Model model) {
        Map<Integer, String> dictionaryMap = dictionary.getDictionaryMap();
        dictionaryMap.put(0, "ㅎㅇ");

        model.addAttribute("dic", dictionaryMap);
        return "/check_dic";
    }
}
