package alura.forohub.api.dto;


import alura.forohub.api.model.StatusTopico;
import alura.forohub.api.model.Topico;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TopicoListaDTO(
        Long id,
        String titulo,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaCreacion,

        StatusTopico status,
        String autor,
        String curso
) {

    public TopicoListaDTO(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre()
        );
    }
}