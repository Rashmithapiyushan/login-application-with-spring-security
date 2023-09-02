package lk.ijse.dep10.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*This class is used to add filters for incoming request. and validate JWT token.*/
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final UserAuthProvider userAuthProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header !=null){
            String[] authElement = header.split(" ");
            if(authElement.length==2 && "Bearer".equals(authElement[0])){
                try {
                    //validate the token. we need storng validation when update or save or delete things.

                    if("GET".equals(request.getMethod())){
                        SecurityContextHolder.getContext().setAuthentication(userAuthProvider.validateToToken(authElement[1]));
                    }else {
                        SecurityContextHolder.getContext().setAuthentication(userAuthProvider.validateTokenStrongly(authElement[1]));
                    }

                }catch (RuntimeException e){
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }

        }
        filterChain.doFilter(request,response);


    }
}
