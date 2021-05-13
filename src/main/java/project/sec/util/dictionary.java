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
}