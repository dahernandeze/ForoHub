package alura.forohub.api.service;


import alura.forohub.api.dto.AuthResponseDTO;
import alura.forohub.api.dto.LoginRequestDTO;
import alura.forohub.api.dto.RegisterRequestDTO;
import alura.forohub.api.exception.ValidationException;
import alura.forohub.api.model.Usuario;
import alura.forohub.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO request) {
        // Crear token de autenticación
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        // Autenticar usuario
        Authentication auth = authenticationManager.authenticate(authToken);

        // Obtener usuario autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Generar JWT
        String token = jwtService.generarToken(usuario);

        return new AuthResponseDTO(token, usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Validar que el email no esté en uso
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ValidationException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));

        // Guardar usuario
        usuario = usuarioRepository.save(usuario);

        // Generar token
        String token = jwtService.generarToken(usuario);

        return new AuthResponseDTO(token, usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }
}