package tech.chello.avisutilisateur.service;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tech.chello.avisutilisateur.entite.Validation;


@AllArgsConstructor
@Service
public class NotificationService {

    private JavaMailSender javaMailSender;


    public void envoyerNotification(Validation validation){
        SimpleMailMessage mailMessage  = new SimpleMailMessage();
        mailMessage.setFrom("ilyes@gmail.com");
        mailMessage.setTo(validation.getUtilisateur().getEmail());
        mailMessage.setSubject("Votre code d'activation");
        mailMessage.setText(String.format("Bonjour %s, votre code d'activation est %s, il expire a %s",validation.getUtilisateur().getNom(),validation.getCode(), validation.getExpiration()));

        javaMailSender.send(mailMessage);
    }
}
