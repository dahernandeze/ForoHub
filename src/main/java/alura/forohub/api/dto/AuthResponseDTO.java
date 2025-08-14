package alura.forohub.api.dto;

public record AuthResponseDTO(
        String token,
        String tipo,
        Long id,
        String nombre,
        String email
) {
    public AuthResponseDTO(String token, Long id, String nombre, String email) {
        this(token, "Bearer", id, nombre, email);
    }
}