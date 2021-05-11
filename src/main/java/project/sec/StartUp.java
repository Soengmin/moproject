package project.sec;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.sec.service.MemberService;
import project.sec.util.MovieUtil;

@Component
@RequiredArgsConstructor
public class StartUp implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberService memberService;
    private final MovieUtil movieUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        movieUtil.load_movie(false);
    }
}