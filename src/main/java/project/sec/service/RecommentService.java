package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.Comment;
import project.sec.domain.Member;
import project.sec.domain.Recomment;
import project.sec.repository.CommentRepository;
import project.sec.repository.MemberRepository;
import project.sec.repository.RecommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommentService {
    private final RecommentRepository recommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public List<Recomment> loadComment(Long commentId){
        Comment comment = commentRepository.getOne(commentId);
        return recommentRepository.findByComment(comment);
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
}
