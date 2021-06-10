package project.sec;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.sec.service.MemberService;
import project.sec.util.MovieUtil;
import project.sec.util.Dictionary_old;

@Component
@RequiredArgsConstructor
public class StartUp implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberService memberService;
    private final MovieUtil movieUtil;
    private final Dictionary_old dictionary;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        movieUtil.update_movie();
    }
}