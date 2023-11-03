package tech.chello.avisutilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import tech.chello.avisutilisateur.entite.Avis;

import java.util.Optional;

public interface AvisRepository extends CrudRepository<Avis, Integer> {
}
