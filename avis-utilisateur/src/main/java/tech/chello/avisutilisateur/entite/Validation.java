package tech.chello.avisutilisateur.entite;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "validation")
public class Validation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Instant creation;
    //il a combien de temps pour valider
    private Instant expiration;
    private Instant validation;
    private String code;

    //car une vcalidatrion ne conscrne qu'un seul utilisateur a la fois
    @OneToOne(cascade = CascadeType.ALL)
    private Utilisateur utilisateur;










}
