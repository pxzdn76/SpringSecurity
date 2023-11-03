package tech.chello.avisutilisateur.entite;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.chello.avisutilisateur.service.UtilisateurService;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avis")
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    private String status;
    //pour dire que plusieurs avis peuvent etre associe a un utilisateur
    //un utilisateur peut avoir plusieurs avis
    @ManyToOne
    private Utilisateur utilisateur;


}
