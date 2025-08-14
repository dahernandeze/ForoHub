package alura.forohub.api.service;

import alura.forohub.api.dto.TopicoListaDTO;
import alura.forohub.api.dto.TopicoRequestDTO;
import alura.forohub.api.dto.TopicoResponseDTO;
import alura.forohub.api.exception.ResourceNotFoundException;
import alura.forohub.api.exception.ValidationException;
import alura.forohub.api.model.Curso;
import alura.forohub.api.model.StatusTopico;
import alura.forohub.api.model.Topico;
import alura.forohub.api.model.Usuario;
import alura.forohub.api.repository.CursoRepository;
import alura.forohub.api.repository.TopicoRepository;
import alura.forohub.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // Crear nuevo tópico
    public TopicoResponseDTO crearTopico(TopicoRequestDTO request, String emailAutor) {
        // Limpiar datos de entrada
        TopicoRequestDTO requestLimpio = request.limpiarDatos();

        // Validar duplicados
        if (topicoRepository.existsByTituloAndMensaje(requestLimpio.titulo(), requestLimpio.mensaje())) {
            throw new ValidationException("Ya existe un tópico con el mismo título y mensaje");
        }

        // Buscar autor
        Usuario autor = usuarioRepository.findByEmailAndActivoTrue(emailAutor)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", emailAutor));

        // Buscar curso
        Curso curso = cursoRepository.findById(requestLimpio.cursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso", "id", requestLimpio.cursoId()));

        if (!curso.getActivo()) {
            throw new ValidationException("No se pueden crear tópicos para cursos inactivos");
        }

        // Crear y guardar el tópico
        Topico topico = new Topico(requestLimpio.titulo(), requestLimpio.mensaje(), autor, curso);
        Topico topicoGuardado = topicoRepository.save(topico);

        return new TopicoResponseDTO(topicoGuardado);
    }

    // Listar todos los tópicos con paginación
    @Transactional(readOnly = true)
    public Page<TopicoListaDTO> listarTopicos(Pageable pageable) {
        return topicoRepository.findAllByOrderByFechaCreacionDesc(pageable)
                .map(TopicoListaDTO::new);
    }

    // Listar solo tópicos activos
    @Transactional(readOnly = true)
    public Page<TopicoListaDTO> listarTopicosActivos(Pageable pageable) {
        return topicoRepository.findByStatusOrderByFechaCreacionDesc(StatusTopico.ACTIVO, pageable)
                .map(TopicoListaDTO::new);
    }

    // Obtener tópico por ID
    @Transactional(readOnly = true)
    public TopicoResponseDTO obtenerTopicoPorId(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico", "id", id));

        return new TopicoResponseDTO(topico);
    }

    // Actualizar tópico
    public TopicoResponseDTO actualizarTopico(Long id, TopicoRequestDTO request, String emailAutor) {
        // Buscar tópico existente
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico", "id", id));

        // Verificar que el usuario sea el autor
        if (!topico.getAutor().getEmail().equals(emailAutor)) {
            throw new ValidationException("Solo el autor puede modificar el tópico");
        }

        // Limpiar datos
        TopicoRequestDTO requestLimpio = request.limpiarDatos();

        // Verificar duplicados (excluyendo el tópico actual)
        boolean existeDuplicado = topicoRepository.findAll().stream()
                .anyMatch(t -> !t.getId().equals(id) &&
                        t.getTitulo().equals(requestLimpio.titulo()) &&
                        t.getMensaje().equals(requestLimpio.mensaje()));

        if (existeDuplicado) {
            throw new ValidationException("Ya existe otro tópico con el mismo título y mensaje");
        }

        // Verificar curso si ha cambiado
        if (!topico.getCurso().getId().equals(requestLimpio.cursoId())) {
            Curso nuevoCurso = cursoRepository.findById(requestLimpio.cursoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso", "id", requestLimpio.cursoId()));

            if (!nuevoCurso.getActivo()) {
                throw new ValidationException("No se puede asignar un curso inactivo");
            }

            topico.setCurso(nuevoCurso);
        }

        // Actualizar campos
        topico.setTitulo(requestLimpio.titulo());
        topico.setMensaje(requestLimpio.mensaje());

        Topico topicoActualizado = topicoRepository.save(topico);
        return new TopicoResponseDTO(topicoActualizado);
    }

    // Eliminar tópico
    public void eliminarTopico(Long id, String emailAutor) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico", "id", id));

        // Verificar que el usuario sea el autor
        if (!topico.getAutor().getEmail().equals(emailAutor)) {
            throw new ValidationException("Solo el autor puede eliminar el tópico");
        }

        topicoRepository.delete(topico);
    }

    // Cambiar status del tópico
    public TopicoResponseDTO cambiarStatus(Long id, StatusTopico nuevoStatus, String emailAutor) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico", "id", id));

        // Verificar que el usuario sea el autor
        if (!topico.getAutor().getEmail().equals(emailAutor)) {
            throw new ValidationException("Solo el autor puede cambiar el status del tópico");
        }

        topico.setStatus(nuevoStatus);
        Topico topicoActualizado = topicoRepository.save(topico);

        return new TopicoResponseDTO(topicoActualizado);
    }

    // Buscar tópicos por título
    @Transactional(readOnly = true)
    public Page<TopicoListaDTO> buscarPorTitulo(String titulo, Pageable pageable) {
        return topicoRepository.findByTituloContainingIgnoreCase(titulo, pageable)
                .map(TopicoListaDTO::new);
    }

    // Obtener estadísticas
    @Transactional(readOnly = true)
    public EstadisticasDTO obtenerEstadisticas() {
        long totalTopicos = topicoRepository.count();
        long topicosActivos = topicoRepository.countByStatus(StatusTopico.ACTIVO);
        long topicosCerrados = topicoRepository.countByStatus(StatusTopico.CERRADO);
        long topicosSolucionados = topicoRepository.countByStatus(StatusTopico.SOLUCIONADO);

        return new EstadisticasDTO(totalTopicos, topicosActivos, topicosCerrados, topicosSolucionados);
    }

    // DTO para estadísticas
    public record EstadisticasDTO(
            long totalTopicos,
            long topicosActivos,
            long topicosCerrados,
            long topicosSolucionados
    ) {}
}