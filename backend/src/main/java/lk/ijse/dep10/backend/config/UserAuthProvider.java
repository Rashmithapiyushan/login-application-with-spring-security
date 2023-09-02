package lk.ijse.dep10.backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import jakarta.annotation.PostConstruct;
import lk.ijse.dep10.backend.dto.UserDto;
import lk.ijse.dep10.backend.entity.User;
import lk.ijse.dep10.backend.exception.AppException;
import lk.ijse.dep10.backend.mappers.UserMapper;
import lk.ijse.dep10.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.net.PasswordAuthentication;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


//to create and validate the JWT tokens.
@RequiredArgsConstructor
@Component
public class UserAuthProvider {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    //only the back end can encode and decode the jwt and for that it needs the secret key.

    @PostConstruct
    protected void init(){
        //this is used to avoid having wrong secret key in the JVM.
        secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /*need to methods to create the token and the validate the token.*/
    public String createToken(UserDto dto){
        /*inside the jwt we include
                login
                created date
                expiry date
                custom claims.(we can add so many custom claims.)
                */

        Date now=new Date();
        Date validity=new Date(now.getTime()+3600000);
        return JWT.create()
                .withIssuer(dto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName",dto.getFirstName())
                .withClaim("lastName",dto.getLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }
    /* Now the method to decode the token */
    public Authentication validateToToken(String token){

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decode = verifier.verify(token);
        UserDto user=UserDto.builder()
                .login(decode.getIssuer())
                .firstName(decode.getClaim("firstName").asString())
                .lastName(decode.getClaim("lastName").asString())
                .build();
        return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());


    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decode = verifier.verify(token);
        User user = userRepository.findByLogin(decode.getIssuer())
                .orElseThrow(() -> new AppException("Unknown User", HttpStatus.NOT_FOUND));
        return new UsernamePasswordAuthenticationToken(userMapper.toUserDto(user),null,Collections.emptyList());
    }
}
