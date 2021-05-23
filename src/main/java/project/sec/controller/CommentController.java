package project.sec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import project.sec.domain.Comment;
import project.sec.domain.CommentDTO;
import project.sec.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/movies/inputComment", method = RequestMethod.GET)
    @ResponseBody
    public void inputComment(String text, long movieId, Authentication auth) {
        System.out.println(movieId + " " + auth.getName() + " " + text);
        commentService.inputComment(auth.getName(), movieId, text);
    }

    @RequestMapping(value = "/movies/loadComment", method = RequestMethod.POST)
    @ResponseBody
    public List<CommentDTO> loadComment(Long movieId, Authentication auth) {
        return commentService.loadComment(movieId, auth.getName());
    }

    @RequestMapping(value = "/movies/deleteComment", method = RequestMethod.POST)
    @ResponseBody
    public void deleteComment(Long commentId) {
        commentService.deleteComment(commentId);
    }
}
