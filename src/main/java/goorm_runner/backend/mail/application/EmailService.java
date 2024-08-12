package goorm_runner.backend.mail.application;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.mail.application.exception.MailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private static int num;

    public static void createRandom(){
        num = (int) (Math.random() * 900000) + 100000;
    }

    public MimeMessage createMail(String mail){
        createRandom();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("야구루트 ⚾️ 본인 확인을 위한 이메일 인증");
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "야구루트 입니다." + "</h1>";
            body += "<h3>" + "본인 확인을 위한을 위한 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";
            body += "<div align='center' style='border:1px solid black;'>";
            body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
            body += "<h1 style='color:black'>" + num + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "5분 안에 입력해주시면 감사하겠습니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new MailException(ErrorCode.EMAIL_SEND_FAILED, e);
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return num;
    }
}
