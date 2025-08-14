package alura.forohub.api.repository;

import alura.forohub.api.model.StatusTopico;
import alura.forohub.api.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Buscar tópicos por status con paginación
    Page<Topico> findByStatusOrderByFechaCreacionDesc(StatusTopico status, Pageable pageable);

    // Buscar todos los tópicos ordenados por fecha
    Page<Topico> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    // Verificar si existe un tópico con el mismo título y mensaje
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    // Buscar tópicos por autor
    Page<Topico> findByAutorIdOrderByFechaCreacionDesc(Long autorId, Pageable pageable);

    // Buscar tópicos por curso
    Page<Topico> findByCursoIdOrderByFechaCreacionDesc(Long cursoId, Pageable pageable);

    // Buscar tópicos por título (búsqueda parcial)
    @Query("SELECT t FROM Topico t WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')) ORDER BY t.fechaCreacion DESC")
    Page<Topico> findByTituloContainingIgnoreCase(@Param("titulo") String titulo, Pageable pageable);

    // Contar tópicos activos
    long countByStatus(StatusTopico status);
}