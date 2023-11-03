package tech.chello.avisutilisateur.service;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.chello.avisutilisateur.TypeRole;
import tech.chello.avisutilisateur.entite.Role;
import tech.chello.avisutilisateur.entite.Utilisateur;
import tech.chello.avisutilisateur.entite.Validation;
import tech.chello.avisutilisateur.repository.UtilisateurRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ValidationService validationService;

    public void inscription(Utilisateur user){
        if(user.getEmail().indexOf("@") == -1 ){
            throw new RuntimeException("Votre mail est invalide");
        }

        if(!user.getEmail().contains(".")){
            throw new RuntimeException("Votre mail est invalide");
        }

        Optional<Utilisateur> optionalUtilisateur = this.utilisateurRepository.findByEmail(user.getEmail());
        if(optionalUtilisateur.isPresent()){
            throw new RuntimeException("Votre mail existe deja");
        }

        String mdpCrypte = this.passwordEncoder.encode(user.getMdp());
        user.setMdp(mdpCrypte);


        Role role = new Role();
        role.setLibelle(TypeRole.UTILISATEUR);
        user.setRole(role);

        Utilisateur utilisateur = this.utilisateurRepository.save(user);
        this.validationService.enregistrer(utilisateur);
    }

    public void activation(Map<String, String> activation) {
       Validation validation=  this.validationService.lireEnFontionDuCode(activation.get("code"));
       if(Instant.now().isAfter(validation.getExpiration())){
           throw new RuntimeException("Votre code est expiré");
       }
      Utilisateur utilisateurActive =  this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
       utilisateurActive.setActif(true);
       utilisateurRepository.save(utilisateurActive);
    }


    //chercher un utilisateur dans la bse de données en fonction de son email
    //il recupere toutes les infos et il compare des mdps car ils sont cryptés
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return this.utilisateurRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu"));

    }
}
