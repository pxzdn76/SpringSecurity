package tech.chello.avisutilisateur.repository;

import org.springframework.data.repository.CrudRepository;
import tech.chello.avisutilisateur.entite.Utilisateur;
import tech.chello.avisutilisateur.entite.Validation;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {


    Optional<Validation> findByCode(String code);
}
