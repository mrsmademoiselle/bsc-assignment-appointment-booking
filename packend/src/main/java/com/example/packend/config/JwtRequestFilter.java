package com.example.packend.config;


import com.example.packend.services.MitarbeiterService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filterklasse, die HttpRequest durchkämmt und auf ein valides Token prüft.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Folgendes Feld wird in den Header der Requests durchsucht, dieses Feld *muss* zwingend gesetzt werden!!
        final String requestTokenHeader = request.getHeader("authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null) {
            jwtToken = requestTokenHeader;
            try {
                // Nach dem User suchen, für den das Token generiert wurde
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Das Token konnte nicht gelesen werden. " + e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("Das Token ist nicht mehr gültig! " + e.getMessage());
            }
        } else {
            // Wenn kein User für das Token gefunden wurde ODER es gar kein Token bislang gibt Ausgabe durch logger
            logger.warn("Es wurde kein Token gesetzt");
        }

        // Sobald ein User gefunden wurde
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.mitarbeiterService.loadUserByUsername(username);

            // Laden des Users und validieren
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Sobald der User verifiziert wurde, es also ein passendes Token gibt, Weitergabe an Spring
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}