package alura.forohub.api.repository;


import alura.forohub.api.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Buscar curso por nombre
    Optional<Curso> findByNombre(String nombre);

    // Verificar si existe un curso con ese nombre
    boolean existsByNombre(String nombre);

    // Buscar cursos activos
    List<Curso> findByActivoTrueOrderByNombre();

    // Buscar cursos por categoría
    List<Curso> findByCategoriaOrderByNombre(String categoria);

    // Buscar cursos activos por categoría
    List<Curso> findByCategoriaAndActivoTrueOrderByNombre(String categoria);
}