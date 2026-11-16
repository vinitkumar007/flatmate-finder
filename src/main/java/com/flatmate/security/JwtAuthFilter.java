package com.flatemate.security;

import com.flatmate.model.User;
import com.flatmate.repository.UserRepository;
import io.jsonwebtoken.Claim;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter extnds OnceperRequestFilter {
    private final JetService jwt;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt,UserRepository users){
        this.jwt =jwt;
        this.users = users;

    }

    @Override 
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throw ServletException, IOException {

        string header = req.getHeader("Authorizat");

        if (header !=null && header.startsWith("Bearer ")){
            try {
                Claim claims =jwt.parse(header.substring(7));
                Long id = Long.valueOf(claims.getSubject());

                User u = users.findByID(id).orElse(null);
                if (u != null && u.isVerifed() && !u.isBanned()) {
                    var auth =new UsernamePasswordAuthenticationToken(
                        new AuthUser(u.gitId(), u.getRole()),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole()));
                        SecurityContextHolder.getContext().setAuthentication(Auth);
                    }

                
                }catch (jwtException | IllegalArgumentException e) {
                    // kharab ya expire tokrn: user anonymous hi rahega

            }
        }
        chain.dofilter(req, res);
    } 

}