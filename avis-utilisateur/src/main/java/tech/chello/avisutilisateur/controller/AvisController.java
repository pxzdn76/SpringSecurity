package tech.chello.avisutilisateur.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.chello.avisutilisateur.entite.Avis;
import tech.chello.avisutilisateur.service.AvisService;

@RequestMapping("/avis")
@RestController
public class AvisController {

    @Autowired
    private AvisService avisService;


    @PostMapping
    public void creer(@RequestBody Avis avis) {
        avisService.saveNewAvis(avis);
    }




}
