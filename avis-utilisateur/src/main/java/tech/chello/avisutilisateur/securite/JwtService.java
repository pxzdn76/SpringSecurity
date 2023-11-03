package tech.chello.avisutilisateur.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tech.chello.avisutilisateur.entite.Utilisateur;
import tech.chello.avisutilisateur.service.UtilisateurService;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Service pour gérer les opérations liées au JWT (JSON Web Token).
 * JWT est utilisé pour l'authentification et l'échange d'informations sécurisées.
 */
@AllArgsConstructor
@Service
public class JwtService {
    // Clé de chiffrement pour le JWT
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
    private UtilisateurService utilisateurService;

    /**
     * Génère un JWT pour un utilisateur spécifique.
     *
     * @param username Le nom d'utilisateur pour lequel générer le JWT.
     * @return Un Map contenant le JWT.
     */
    public Map<String, String> generate(String username) {
        UserDetails utilisateur = this.utilisateurService.loadUserByUsername(username);
        return this.generateJwt((Utilisateur) utilisateur);
    }

    /**
     * Extrait le nom d'utilisateur du JWT.
     *
     * @param token Le JWT à partir duquel extraire le nom d'utilisateur.
     * @return Le nom d'utilisateur.
     */
    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    /**
     * Vérifie si le JWT a expiré.
     *
     * @param token Le JWT à vérifier.
     * @return true si le JWT a expiré, false sinon.
     */
    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    /**
     * Récupère la date d'expiration du JWT.
     *
     * @param token Le JWT à partir duquel récupérer la date d'expiration.
     * @return La date d'expiration du JWT.
     */
    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    /**
     * Récupère une revendication spécifique du JWT.
     *
     * @param token Le JWT à partir duquel récupérer la revendication.
     * @param function La fonction pour extraire la revendication.
     * @return La revendication extraite.
     */
    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    /**
     * Récupère toutes les revendications du JWT.
     *
     * @param token Le JWT à partir duquel récupérer les revendications.
     * @return Les revendications du JWT.
     */
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Génère un JWT pour un utilisateur spécifique.
     *
     * @param utilisateur L'utilisateur pour lequel générer le JWT.
     * @return Un Map contenant le JWT.
     */
    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        final Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, utilisateur.getEmail()
        );

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);
    }

    /**
     * Récupère la clé de chiffrement pour le JWT.
     *
     * @return La clé de chiffrement.
     */
    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

}