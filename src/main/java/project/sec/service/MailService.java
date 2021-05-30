package project.sec.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "hibyesayonara@gmail.com";

    public String mailsend(String address) {
        Random random = new Random();
        String certNum = String.valueOf(random.nextInt(8888888) + 1111111);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setFrom(FROM_ADDRESS);
        message.setSubject("인증번호를 확인해주세요.");
        String msg = "";
        msg += "가입해주셔서 감사합니다\n";
        msg = msg + "인증번호는 " + certNum + " 입니다.";
        message.setText(msg);

        mailSender.send(message);
        System.out.println("email : " + address + " certNum : " + certNum);
        return certNum;
    }
}
