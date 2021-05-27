package project.sec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.sec.controller.MemberForm;
import project.sec.domain.Member;
import project.sec.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class MemberValidator implements Validator {


    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberForm.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        MemberForm memberForm = (MemberForm) obj;

        if(memberRepository.findByEmail(memberForm.getEmail()).size() == 1) {
            errors.rejectValue("email","email.error","이미 사용중인 이메일입니다.");
        } else if(memberRepository.findBynicName(memberForm.getNicname()).size() == 1){
            errors.rejectValue("nicname","nicname.error","이미 사용중인 닉네임입니다.");
        } else if (memberForm.getNicname().length() == 0
        || memberForm.getPassword().length() == 0
        || memberForm.getEmail().length() == 0) {
            errors.rejectValue("password", "blank.error", "모든 칸에 공백이 없게해주세요");
        }
    }
}
