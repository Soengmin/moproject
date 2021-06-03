package project.sec;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.sec.service.MemberService;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/static/**/**", "/img/**", "/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/members/login", "/members/signup", "/members/memberList", "/members/inputSignupForm").permitAll()
                .antMatchers("/movies/all_movie", "/movies/explore_movies", "/movies/_movie_view"
                ,"movies/myeval_movie", "/movies/search").hasRole("USER")
                .antMatchers("/movies/is_ajax").permitAll()
                .antMatchers("/movies/first_eval").hasRole("NOTYET")
                .antMatchers("/").hasAnyRole("USER", "NOTYET")
                .anyRequest().authenticated()
                .and()
                .csrf()
                .ignoringAntMatchers("/members/signup")
                .ignoringAntMatchers("/members/login")
                .ignoringAntMatchers("/members/memberList")
                .ignoringAntMatchers("/movies/find")
                .ignoringAntMatchers("/movies/movieList")
                .ignoringAntMatchers("/movies/is_ajax")
                .ignoringAntMatchers("/movies/explore_movie")
                .ignoringAntMatchers("/movies/explore")
                .ignoringAntMatchers("/movies/search")
                .ignoringAntMatchers("/movies/myeval_movie")
                .ignoringAntMatchers("/movies/loadComment")
                .ignoringAntMatchers("/movies/deleteComment")
                .ignoringAntMatchers("/movies/recommend")
                .ignoringAntMatchers("/movies/pop")
                .ignoringAntMatchers("/members/inputSignupForm")
                .ignoringAntMatchers("/movies/update_first_eval"
                , "/members/changeAuth")
                .and()
                .formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
