package alura.forohub.api.repository;


import alura.forohub.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Verificar si existe un usuario con ese email
    boolean existsByEmail(String email);

    // Buscar usuarios activos
    Optional<Usuario> findByEmailAndActivoTrue(String email);
}