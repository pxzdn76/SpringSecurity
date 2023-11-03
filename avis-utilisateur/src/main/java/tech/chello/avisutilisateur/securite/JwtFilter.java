package tech.chello.avisutilisateur.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.chello.avisutilisateur.service.UtilisateurService;

import java.io.IOException;
@Slf4j


@Service
public class JwtFilter extends OncePerRequestFilter {
    //Pour recuperer les infos de l'utilisateur
    private UtilisateurService utilisateurService;


    private JwtService jwtService;


    public JwtFilter(UtilisateurService utilisateurService,JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Boolean isTokenExpired = true;
        //pour recuperer le token transmet dans le headre de la requete
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer")){
            //pour elimener Bearer de Bearer eyJhbGciOiJIUzI1NiJ9.eyJub20iOiJpbHllcyB6ZXJkYW5lIiwiZW1haWwiOiJ6ZXJkYW5lOUBnbWFpbC5jb20ifQ.m8C6U56tavi2Cf3cym25IfQphUmeIYUEC0kGWAF7mzA
            token = authorization.substring(7);
            //pour des bonnes pratique le token doit etre stocker dans la base de données
            isTokenExpired = jwtService.isTokenExpired(token);
            //maintenant il faut recuperer le username a partir du token
            username = jwtService.extractUsername(token);


        }
        log.info("isTokenExpired : "+ isTokenExpired);
        log.info("username : "+ username);
        log.info("SecurityContextHolder.getContext().getAuthentication(): "+ (true  ? SecurityContextHolder.getContext().getAuthentication() ==  null: false));

        //si le username !=null et que aucune authentification n'est en cours
        if(isTokenExpired == false && username != null && SecurityContextHolder.getContext().getAuthentication() ==  null){
            log.info("il est ici");
            //ici on utilise UserDetails au lieu de Utilisateur car on est dans la sécurité
            UserDetails utilisateur = utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(utilisateur,null,utilisateur.getAuthorities());
            //on dit a spring que l'utilisateur est authentifié (la partie de verification du token est faite avec succès)
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);



        }
        filterChain.doFilter(request,response);



    }
}
