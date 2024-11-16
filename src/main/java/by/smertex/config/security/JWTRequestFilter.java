package by.smertex.config.security;

import by.smertex.util.JwtTokenUtils;
import by.smertex.util.ResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);
            try{
                username = jwtTokenUtils.getUsername(jwt);
            } catch (ExpiredJwtException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(ResponseMessage.EXPIRED_JWT_EXCEPTION);
                return;
            } catch (SignatureException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(ResponseMessage.SIGNATURE_EXCEPTION);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
