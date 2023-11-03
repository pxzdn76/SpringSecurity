package tech.chello.avisutilisateur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.chello.avisutilisateur.entite.Avis;
import tech.chello.avisutilisateur.entite.Utilisateur;
import tech.chello.avisutilisateur.repository.AvisRepository;


@Service
public class AvisService {


    @Autowired
    private AvisRepository avisRepository;

    public void saveNewAvis(Avis avis) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        avisRepository.save(avis);
    }
}
