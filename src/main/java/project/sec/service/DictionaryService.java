package project.sec.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.*;
import project.sec.repository.DictionaryRepository;
import scala.collection.Seq;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final EntityManager em;

    public void save(Dictionary dictionary) {
        String word = dictionary.getWord();
        List<Dictionary> find = dictionaryRepository.findByWord(word);
        if (find.size() == 0) {
            dictionaryRepository.save(dictionary);
        }
    }

    public List<Dictionary> findByWord(String word) {
        return dictionaryRepository.findByWord(word);
    }

    public List<Dictionary> findAll() {
        return dictionaryRepository.findAll();
    }

    @Transactional
    public void make_dictionary(Movie movie) {
        HashMap<String, Integer> ret_map = new HashMap<>();
        HashSet<String> dic_set = new HashSet<>();

        String[] tmp = {};
        try {
            tmp = movie.getActor().split("\\|");
            for (String s : tmp) {
                dic_set.add(s);
            }
        } catch (NullPointerException e) {
        }

        try {
            for (String s : movie.getCountry().split(",")) {
                dic_set.add(s);
            }
        } catch (NullPointerException e) {
        }

        try {
            for (String s : movie.getDirector().split("\\|")) {
                dic_set.add(s);
            }
        } catch (NullPointerException e) {
        }

        List<Movie_Genre> genreList = movie.getGenreList();
        for (Movie_Genre movie_genre : genreList) {
            dic_set.add(movie_genre.getGenre().getGenre());
        }

        if (movie.getOutline().length() >= 1) {
            CharSequence normalized = OpenKoreanTextProcessorJava.normalize(movie.getOutline());
            Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
            List<KoreanTokenJava> strings = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
            for (KoreanTokenJava s : strings) {
                if (s.getPos().toString().equals("Noun") ||
                        s.getPos().toString().equals("Alpha")) {
                    dic_set.add(s.getText());
                }
            }
        }

        for (String s : dic_set) {
            Dictionary dictionary = new Dictionary(s);
            save(dictionary);
        }
    }

    @Transactional
    public void saveMovieMemberDic(Member member, Movie movie) {
        List<Dictionary> dictionary = findAll();
        HashMap<String, Integer> ret_map = new HashMap<>();

        for (Dictionary d : dictionary) {
            ret_map.put(d.getWord(), 0);
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

        for (String s : ret_map.keySet()) {
            if (ret_map.get(s) >= 1) {
                Dictionary dic = findByWord(s).get(0);
                String Query = "select md from Member_dictionary md where md.dictionary = :DIC and md.member = :MEM";
                List<Member_dictionary> memDic = em.createQuery(Query, Member_dictionary.class)
                        .setParameter("DIC", dic)
                        .setParameter("MEM", member)
                        .getResultList();

                if (memDic.size() == 1) {
                    memDic.get(0).setCount(memDic.get(0).getCount() + ret_map.get(s));
                } else if (memDic.size() == 0) {
                    Member_dictionary md = new Member_dictionary(member, dic);
                    md.setCount(ret_map.get(s));
                    em.persist(md);
                }
            }
        }
    }
}
