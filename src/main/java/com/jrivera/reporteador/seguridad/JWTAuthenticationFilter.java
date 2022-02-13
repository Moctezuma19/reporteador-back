package com.jrivera.reporteador.seguridad;

import com.jrivera.reporteador.seguridad.modelo.UsuarioPrincipal;
import com.jrivera.reporteador.seguridad.servicio.UsuarioSeguridadServicio;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private UsuarioSeguridadServicio usuarioSeguridadServicio;
    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            String header = request.getHeader(HEADER_STRING);
            String username = null;
            String jwtToken = null;
            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                jwtToken = header.replace(TOKEN_PREFIX, "");
                username = jwtProvider.getUsernameFromToken(jwtToken);
            } else {
                logger.warn("Couldn't find bearer string, will ignore the header");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsuarioPrincipal userDetails = (UsuarioPrincipal) ((UserDetailsService) usuarioSeguridadServicio).loadUserByUsername(username);
                if (jwtProvider.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("Authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // AGREGAR EL ID DE USUARIO Y USUARIO PARA SU USO EN LOS SERVICIOS
                /*RequestContext ctx = RequestContext.getCurrentContext();
                ctx.addZuulRequestHeader("Username", username);
                ctx.addZuulRequestHeader("IdUser", userDetails.getIdUser().toString());*/
            }
        } catch (ExpiredJwtException ex) {
//            String isRefreshToken = request.getHeader("isRefreshToken");
            String requestURL = request.getRequestURL().toString();

            // allow for Refresh Token creation if following conditions are true.
            if (requestURL.contains("refresh-token")) {
                LOG.info("Adding claims ");
                allowForRefreshToken(ex, request);
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("exception", ex);
            }
        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        }
        chain.doFilter(request, res);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());
    }
}