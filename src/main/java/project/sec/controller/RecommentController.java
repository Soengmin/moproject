package project.sec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.sec.domain.Recomment;
import project.sec.domain.RecommentDto;
import project.sec.service.RecommendService;
import project.sec.service.RecommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommentController {
    private final RecommentService recommentService;

    @RequestMapping(value = "/movies/recomment", method = RequestMethod.POST)
    @ResponseBody
    public List<RecommentDto> reComment(Authentication auth, Long commentId){
        return recommentService.loadComment(commentId);
    }

    @RequestMapping(value = "/movies/recommentsave", method = RequestMethod.POST)
    @ResponseBody
    public void save(Long commentId, String content,Authentication auth){
        recommentService.save(auth.getName(),commentId,content);
    }

    @RequestMapping(value = "/movies/recommentdelete", method = RequestMethod.POST)
    @ResponseBody
    public void delete(Long id){
        recommentService.delete(id);
    }
}
