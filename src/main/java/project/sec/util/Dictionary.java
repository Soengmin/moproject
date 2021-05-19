package project.sec.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.*;
import scala.collection.Seq;

import javax.persistence.EntityManager;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Dictionary {

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

/*            Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
            if (m.getOutline().length() >= 1) {
                KomoranResult list = komoran.analyze(m.getOutline());
                List<Token> tokenList = list.getTokenList();
                for (Token token : tokenList) {
                    if (token.getPos().equals("NNP")) {
                        System.out.println(token.getMorph());
                        set.add(token.getMorph());
                    }
                }
            }*/

            if (m.getOutline().length() >= 1) {
                CharSequence normalized = OpenKoreanTextProcessorJava.normalize(m.getOutline());
                Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
                List<KoreanTokenJava> strings = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
                for (KoreanTokenJava s : strings) {
                    if (s.getPos().toString().equals("Noun") ||
                    s.getPos().toString().equals("Alpha")) {
                        set.add(s.getText());
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
            for (String s : tmp) {
                ret_map.put(s, ret_map.get(s) + 1);
            }
        } catch (NullPointerException e) {
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

        List<Movie_Genre> genreList = movie.getGenreList();
        for (Movie_Genre movie_genre : genreList) {
            ret_map.put(movie_genre.getGenre().getGenre(), ret_map.get(movie_genre.getGenre().getGenre()) + 1);
        }

        if (movie.getOutline().length() >= 1) {
            CharSequence normalized = OpenKoreanTextProcessorJava.normalize(movie.getOutline());
            Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
            List<KoreanTokenJava> strings = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
            for (KoreanTokenJava s : strings) {
                if (s.getPos().toString().equals("Noun") ||
                        s.getPos().toString().equals("Alpha")) {
                    ret_map.put(s.getText(), ret_map.get(s.getText()) + 1);
                }
            }
        }
        return ret_map;
    }

    public static HashMap<String, Integer> get_member_vector(Member member) {
        long beforeTime = System.currentTimeMillis();
        HashMap<String, Integer> ret_map = new HashMap<>();
        for (String value : dictionaryMap.values()) {
            ret_map.put(value, 0);
        }

        for (EvalList member_movie : member.getEvalLists()) {
            Movie mov = member_movie.getMovie_id();
            HashMap<String, Integer> movieDic = return_movie_dic(mov);
            for (String s : movieDic.keySet()) {
                if (movieDic.get(s) >= 1) {
                    ret_map.put(s, ret_map.get(s) + movieDic.get(s));
                }
            }
        }

        long afterTime = System.currentTimeMillis();
        System.out.println(afterTime - beforeTime);

        return ret_map;
    }
}