package project.sec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.sec.domain.Movie;
import project.sec.service.RecommendService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping( "movies/recommend")
    public String recommend() {
        return "movies/recommend";
    }

    @ResponseBody
    @RequestMapping(value = "/movies/recommend", method = RequestMethod.POST)
    public List<Movie> precommend(Authentication auth) {
        List<Movie> movies = recommendService.recommend(auth.getName());
        System.out.println(movies.size());
        return movies;
    }
}
