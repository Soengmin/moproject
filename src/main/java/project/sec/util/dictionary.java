package project.sec.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Genre;
import project.sec.domain.Movie;

import javax.persistence.EntityManager;
import java.util.*;

@Component
@RequiredArgsConstructor
public class dictionary {

    private final EntityManager em;

    public static Map<Integer, String> dictionaryMap = new HashMap<>();

    public static Map<Integer, String> getDictionaryMap() {
        return dictionaryMap;
    }

    @Transactional(readOnly = true)
    public void make_dic() {

        List<Genre> genre = em.createQuery("select g from Genre g", Genre.class)
                .getResultList();
        HashSet<String> set = new HashSet<>();
        for (Genre genre1 : genre) {
            set.add(genre1.getGenre());
        }

        List<Movie> movie = em.createQuery("select m from Movie m", Movie.class)
                .getResultList();

        for (Movie m : movie) {
            String[] tmp = {};
            try {
                tmp = m.getActor().split("\\|");
            } catch (NullPointerException e) {
            }
            for (String s : tmp) {
                set.add(s);
            }

            try {
                for (String s : m.getCountry().split(",")) {
                    set.add(s);
                }
            } catch (NullPointerException e) {
            }

            try {
                for (String s : m.getDirector().split("\\|")) {
                    set.add(s);
                }
            } catch (NullPointerException e) {
            }

            Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
            if (m.getOutline().length() >= 1) {
                KomoranResult list = komoran.analyze(m.getOutline());
                List<Token> tokenList = list.getTokenList();
                for (Token token : tokenList) {
                    if (token.getPos().equals("NNP")) {
                        System.out.println(token.getMorph());
                        set.add(token.getMorph());
                    }
                }
            }
        }

        Iterator<String> iterator = set.iterator();
        for (int i = 0; i < set.size(); i++) {
            dictionaryMap.put(i, iterator.next());
        }
    }

    /**
     * 인자로 movie 넘기면 그 movie의 벡터 해쉬맵 리턴
     */
    public static HashMap<String, Integer> return_movie_dic(Movie movie) {
        HashMap<String, Integer> ret_map = new HashMap<>();

        for (String value : dictionaryMap.values()) {
            ret_map.put(value, 0);
        }

        String[] tmp = {};
        try {
            tmp = movie.getActor().split("\\|");
        } catch (NullPointerException e) {
        }
        for (String s : tmp) {
            ret_map.put(s, ret_map.get(s) + 1);
        }

        try {
            for (String s : movie.getCountry().split(",")) {
                ret_map.put(s, ret_map.get(s) + 1);
            }
        } catch (NullPointerException e) {
        }

        try {
            for (String s : movie.getDirector().split("\\|")) {
                ret_map.put(s, ret_map.get(s) + 1);
            }
        } catch (NullPointerException e) {
        }

        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        if (movie.getOutline().length() >= 1) {
            KomoranResult list = komoran.analyze(movie.getOutline());
            List<Token> tokenList = list.getTokenList();
            for (Token token : tokenList) {
                if (token.getPos().equals("NNP")) {
                    System.out.println(token.getMorph());
                    ret_map.put(token.getMorph(), ret_map.get(token.getMorph()) + 1);
                }
            }
        }

        return ret_map;
    }
}