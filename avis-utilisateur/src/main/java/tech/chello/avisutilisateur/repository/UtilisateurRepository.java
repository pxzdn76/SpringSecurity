package tech.chello.avisutilisateur.repository;

import org.springframework.data.repository.CrudRepository;
import tech.chello.avisutilisateur.entite.Avis;
import tech.chello.avisutilisateur.entite.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {

    //si l'utilisateur existe elle va retourner un oprional plein sinon vide
    Optional<Utilisateur> findByEmail(String email);
}
