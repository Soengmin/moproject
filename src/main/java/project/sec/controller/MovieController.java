package project.sec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.sec.domain.Genre;
import project.sec.domain.Movie;
import project.sec.service.*;
import project.sec.util.NaverMovieSearch;

import javax.persistence.EntityManager;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final NaverMovieSearch naverMovieSearch;
    private final MemberLoginValidator memberloginValidator;
    private final EntityManager em;
    private final MovieService movieService;
    private final GenreService genreService;
    private final Movie_GenreService movie_genreService;
    private final MovieSearchService movieSearchService;
    private final MyEvalMovieService myEvalMovieService;


    @GetMapping("/movies/search")
    public String GfindMovie(@RequestParam(value = "search", required = false) String search, Model model){
        model.addAttribute("searchValue", search);
        return "movies/search";
    }


    @ResponseBody
    @RequestMapping(value = "/movies/search", method = RequestMethod.POST)
    public List<Movie> search(String find) {
        return movieSearchService.search(find);
    }

    @GetMapping("/movies/allMovie")
    public String all_movie(Model model) {

        return "movies/all_movie";
    }

    @ResponseBody
    @RequestMapping(value = "/movies/is_ajax", method = RequestMethod.POST)
    public List<Movie> ajaaja(int sequence, Authentication auth) {
        System.out.println("sequence = " + sequence);
/*        List<Movie> find = em.createQuery("select m from Movie m", Movie.class)
                .setFirstResult(sequence)
                .setMaxResults(20)
                .getResultList();*/
        return movieSearchService.createRandomTable(sequence, auth.getName());
    }
    @ResponseBody
    @RequestMapping(value = "/movies/is_ajaxx", method = RequestMethod.GET)
    public void ajaaja2(@RequestParam(value = "movie_id") Long movie_id,@RequestParam(value = "input") double rating, Authentication auth) {
        String email = auth.getName();
        System.out.println(movie_id);
        System.out.println(rating);
        System.out.println(email);

        movieService.save(email,movie_id,rating);
    }
    @GetMapping("/movies/explore_movie")
    public String exploremoive(Model model) {

        return "movies/explore_movie";
    }
    @ResponseBody
    @RequestMapping(value = "/movies/explore_movie", method = RequestMethod.POST)
    public List<Genre> Pexploremovie() {
        //System.out.println(genreService.findAll().get(0));
        return genreService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/movies/explore", method = RequestMethod.POST)
    public List<Movie> Pexploremovie_genre(Long genre_id, int sequence) {
        List<Movie> movie = movie_genreService.findMovie(genre_id, sequence);
        return movie;
    }

    @GetMapping(value = "movies/view/{id}")
    public String MovieView(@PathVariable("id") Long id, Model model) {
        Movie findMovie = movieService.findById(id);
        model.addAttribute("findMovie", findMovie);

        return "movies/movie_view";
    }

    @GetMapping( "movies/myeval_movie")
    public String evalmovie() {
        return "movies/myeval_movie";
    }

    @ResponseBody
    @RequestMapping(value = "/movies/myeval_movie", method = RequestMethod.POST)
    public List<Movie> pevalmovie(Authentication auth) {
        List<Movie> movie = myEvalMovieService.find(auth.getName());
        return movie;
    }
}
