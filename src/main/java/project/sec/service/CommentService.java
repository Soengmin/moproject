package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Comment;
import project.sec.domain.CommentDTO;
import project.sec.domain.Member;
import project.sec.domain.Movie;
import project.sec.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final EntityManager em;
    private final MemberRepository memberRepository;

    @Transactional
    public void inputComment(String email, long movieId, String text) {
        Member member = this.em.createQuery("select m from Member m where m.email = :em", Member.class)
                .setParameter("em", email)
                .getResultList()
                .get(0);
        Movie movie = em.createQuery("select m from Movie m where m.id = :i", Movie.class)
                .setParameter("i", movieId)
                .getResultList()
                .get(0);
        Comment comment = new Comment(member, movie, text);
        em.persist(comment);
    }

    public List<CommentDTO> loadComment(Long movieId, String email) {
        List<Comment> comments = em.createQuery("select c from Comment c where c.movie_id.id = :id order by c.id desc", Comment.class)
                .setParameter("id", movieId)
                .getResultList();
        Member member = memberRepository.findByEmail(email).get(0);
        int is_mine = 0;

        List<CommentDTO> ret_val = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getMember_id() == member) is_mine = 1;
            else is_mine = 0;
            ret_val.add(new CommentDTO(comment.getMember_id().getId() ,comment.getMember_id().getNicName()
                    , comment.getComment(), is_mine, comment.getId()));
        }

        return ret_val;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = em.createQuery("select c from Comment c where c.id = :id", Comment.class)
                .setParameter("id", commentId)
                .getResultList()
                .get(0);
        em.remove(comment);
    }
}
