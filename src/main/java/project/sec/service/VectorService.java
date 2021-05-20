package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.Dictionary;
import project.sec.domain.Member;
import project.sec.domain.Member_dictionary;
import project.sec.repository.DictionaryRepository;
import project.sec.repository.MemberDictionaryRepository;
import project.sec.repository.MemberRepository;
import scala.Int;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VectorService {
    private final MemberDictionaryRepository memberDictionaryRepository;
    private final MemberRepository memberRepository;
    private final DictionaryRepository dictionaryRepository;

    public List<Integer> userVector(Long id){
        List<Dictionary> dics = dictionaryRepository.findAll();
        Member member = memberRepository.findById(id).get();
        List<Member_dictionary> userVector = memberDictionaryRepository.findByMember(member);
        List<String> dicArr = new ArrayList<>();
        for(Dictionary dic : dics){
            dicArr.add(dic.getWord());
        }
        List<Integer> vector = new ArrayList<>();
        for(int i = 0; i<dics.size();i++){
            vector.add(0);
        }
        for(Member_dictionary memdic : userVector){
            String word = memdic.getDictionary().getWord();
            int index = dicArr.indexOf(word);
            vector.set(index,memdic.getCount());
        }
        return vector;
    }

    public void cosSim(String email){
        Long userId = memberRepository.findByEmail(email).get(0).getId();
        List<Integer> stanvec = userVector(userId);
        List<Member> members = memberRepository.findAll();
        //System.out.println(memberRepository.findByEmail(email).get(0).getEmail());
        members.remove(memberRepository.findByEmail(email).get(0));

        List<List<Integer>> vectors = new ArrayList<>();

        for (Member member : members) {
            //System.out.println(member.getEmail());
            vectors.add(userVector(member.getId()));
        }



        Map<String, Double> cossim = new HashMap<>();
        int j = 0;
        for(List<Integer> vector : vectors){
            int sumA = 0, sumB=0 , mul=0;
            int a = 0 ,b = 0;
            double cos;
            for(int i = 0; i<vector.size();i++){
                a = stanvec.get(i);
                b = vector.get(i);

                sumA += Math.pow(a,2);
                sumB += Math.pow(b,2);

                mul += a*b;
            }
            cos = mul / (Math.sqrt(sumA) * Math.sqrt(sumB));

            cossim.put(members.get(j++).getEmail(),cos);
        }
        System.out.println(cossim);
    }
}
