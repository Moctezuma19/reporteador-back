package com.jrivera.reporteador.controlador;

import com.jrivera.reporteador.dto.LoginUserDto;
import com.jrivera.reporteador.dto.UsuarioDto;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.seguridad.JWTProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.impl.DefaultClaims;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AutenticacionControlador {

    private final static Logger logger = LoggerFactory.getLogger(AutenticacionControlador.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @ResponseBody
    @CrossOrigin
    @RequestMapping(path = "/genera-token", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUserDto loginUser) {
        logger.info("Intento de inicio de sesion de " + loginUser);
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getCorreo(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Usuario user = usuarioRepositorio.findByCorreo(loginUser.getCorreo());
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        final String token = jwtProvider.doGenerateToken(user.getCorreo());
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setIdUsuario(user.getIdUsuario());
        usuarioDto.setCorreo(user.getCorreo());
        usuarioDto.setNombre(user.getNombre() + " " + user.getApellido());
        usuarioDto.setIdRol(user.getRol().getIdRol());
        usuarioDto.setToken(token);

        return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
    }

    @ResponseBody
    @CrossOrigin
    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request)
            throws AuthenticationException {

        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        String token = jwtProvider.doGenerateRefreshToken(claims, claims.getSubject());

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", token);
        response.put("success", "true");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
