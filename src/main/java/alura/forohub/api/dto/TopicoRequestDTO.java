package alura.forohub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TopicoRequestDTO(

        @NotBlank(message = "El título es obligatorio")
        @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
        String titulo,

        @NotBlank(message = "El mensaje es obligatorio")
        @Size(min = 10, max = 2000, message = "El mensaje debe tener entre 10 y 2000 caracteres")
        String mensaje,

        @NotNull(message = "El ID del curso es obligatorio")
        Long cursoId
) {

    // Método de validación personalizada
    public boolean tieneDatosSuficientes() {
        return titulo != null && !titulo.trim().isEmpty() &&
                mensaje != null && !mensaje.trim().isEmpty() &&
                cursoId != null && cursoId > 0;
    }

    // Método para limpiar datos
    public TopicoRequestDTO limpiarDatos() {
        return new TopicoRequestDTO(
                titulo != null ? titulo.trim() : null,
                mensaje != null ? mensaje.trim() : null,
                cursoId
        );
    }
}