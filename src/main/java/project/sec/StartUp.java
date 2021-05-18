package project.sec;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.sec.service.MemberService;
import project.sec.util.MovieUtil;
import project.sec.util.dictionary;

@Component
@RequiredArgsConstructor
public class StartUp implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberService memberService;
    private final MovieUtil movieUtil;
    private final dictionary dictionary;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        movieUtil.load_movie(false);
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        dictionary.make_dic();
        System.gc();

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println((before - after));
    }
}