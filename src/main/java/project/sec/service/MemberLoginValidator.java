package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.sec.controller.MemberForm;
import project.sec.controller.MemberLoginForm;
import project.sec.domain.Member;
import project.sec.repository.MemberRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberLoginValidator implements Validator {

    private final MemberRepository memberRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return MemberLoginForm.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        MemberLoginForm memberLoginForm = (MemberLoginForm) obj;
        List<Member> aaa = memberRepository.findByEmail(memberLoginForm.getEmail());
        if(aaa.size() == 0){
            errors.rejectValue("email","email.error","존재하지 않는 이메일입니다");
        }else if(!aaa.get(0).getPassword().equals(memberLoginForm.getPassword())){
            errors.rejectValue("password","password.error","일치하지 않는 비밀번호입니다");
        }
    }
}
