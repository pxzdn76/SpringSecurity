package tech.chello.avisutilisateur.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.chello.avisutilisateur.dto.AthentificationDTO;
import tech.chello.avisutilisateur.entite.Utilisateur;
import tech.chello.avisutilisateur.securite.JwtService;
import tech.chello.avisutilisateur.service.UtilisateurService;

import java.util.Map;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/inscription")
    public void inscription(@RequestBody Utilisateur user) {

        this.utilisateurService.inscription(user);
    }

    @PostMapping("/activation")
    public void validation(@RequestBody Map<String,String> activation) {

        this.utilisateurService.activation(activation);
    }
    @PostMapping("/connexion")
    //on va retourner le token et la valeur du token
    public Map<String,String> connexion(@RequestBody AthentificationDTO athentificationDTO) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(athentificationDTO.username(), athentificationDTO.password()
                ));

        if(authenticate.isAuthenticated()){
            return this.jwtService.generate(athentificationDTO.username());
        }
        return null;
    }
}

