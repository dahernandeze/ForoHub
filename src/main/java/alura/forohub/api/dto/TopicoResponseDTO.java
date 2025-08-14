package alura.forohub.api.dto;

import alura.forohub.api.model.StatusTopico;
import alura.forohub.api.model.Topico;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TopicoResponseDTO(
        Long id,
        String titulo,
        String mensaje,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaCreacion,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaActualizacion,

        StatusTopico status,
        AutorDTO autor,
        CursoDTO curso
) {

    // Constructor desde entidad
    public TopicoResponseDTO(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getFechaActualizacion(),
                topico.getStatus(),
                new AutorDTO(topico.getAutor().getId(), topico.getAutor().getNombre(), topico.getAutor().getEmail()),
                new CursoDTO(topico.getCurso().getId(), topico.getCurso().getNombre(), topico.getCurso().getCategoria())
        );
    }

    // DTOs anidados
    public record AutorDTO(Long id, String nombre, String email) {}
    public record CursoDTO(Long id, String nombre, String categoria) {}
}