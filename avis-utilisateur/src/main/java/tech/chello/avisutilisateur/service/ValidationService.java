package tech.chello.avisutilisateur.service;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.chello.avisutilisateur.entite.Utilisateur;
import tech.chello.avisutilisateur.entite.Validation;
import tech.chello.avisutilisateur.repository.ValidationRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@AllArgsConstructor
@Service
public class ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    //stocker une validation
    public void enregistrer(Utilisateur utilisateur){
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);
        validation.setExpiration(expiration);
        Random random = new Random();
        int randomInteger = random.nextInt(99999);
        String code = String.format("%06d",randomInteger);
        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.envoyerNotification(validation);
    }


    public Validation lireEnFontionDuCode(String code){
        return this.validationRepository.findByCode((code)).orElseThrow(() -> new RuntimeException("Votre code est invalide"));


    }
}
