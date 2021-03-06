package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.Comment;
import project.sec.domain.Member;
import project.sec.domain.Recomment;
import project.sec.domain.RecommentDto;
import project.sec.repository.CommentRepository;
import project.sec.repository.MemberRepository;
import project.sec.repository.RecommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommentService {
    private final RecommentRepository recommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public List<RecommentDto> loadComment(Long commentId,String email){
        String nic = memberRepository.findByEmail(email).get(0).getNicName();
        Comment comment = commentRepository.getOne(commentId);
        List<Recomment> recomments = recommentRepository.findByComment(comment);
        List<RecommentDto> recommentDtos = new ArrayList<>();
        for(Recomment recomment : recomments) {
            RecommentDto recommentDto = new RecommentDto(recomment.getId(),recomment.getMemberId().getNicName(),recomment.getContent(),nic);
            recommentDtos.add(recommentDto);
        }
        return recommentDtos;

    }

    public void save(String auth,Long comId, String content){
        Comment comment = commentRepository.getOne(comId);
        Member memberId = memberRepository.findByEmail(auth).get(0);
        Recomment recomment = new Recomment(memberId,comment,content);
        recommentRepository.save(recomment);
    }

    public void delete(Long id){
        recommentRepository.deleteById(id);
    }

    public void deleteParent(Long id){
        //id는 Comment의 id
        System.out.println(id);
        Comment comment = commentRepository.getOne(id);
        List<Recomment> recomments = recommentRepository.findByComment(comment);
        for(Recomment recomment : recomments){
            recommentRepository.deleteById(recomment.getId());
        }
    }
}
