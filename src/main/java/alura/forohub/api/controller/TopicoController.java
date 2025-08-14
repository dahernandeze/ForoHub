package alura.forohub.api.controller;

import alura.forohub.api.dto.TopicoListaDTO;
import alura.forohub.api.dto.TopicoRequestDTO;
import alura.forohub.api.dto.TopicoResponseDTO;
import alura.forohub.api.model.StatusTopico;
import alura.forohub.api.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/topicos")
@CrossOrigin(origins = "*") // Para desarrollo, en producción especificar dominios
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    // Crear nuevo tópico
//    @PostMapping
//    public ResponseEntity<TopicoResponseDTO> crearTopico(
//            @Valid @RequestBody TopicoRequestDTO request,
//            UriComponentsBuilder uriBuilder) {
//
//        // Por ahora simulamos un usuario fijo, después usaremos JWT
//        String emailAutor = "juan@email.com";
//
//        TopicoResponseDTO topicoCreado = topicoService.crearTopico(request, emailAutor);
//
//        URI uri = uriBuilder.path("/api/topicos/{id}")
//                .buildAndExpand(topicoCreado.id())
//                .toUri();
//
//        return ResponseEntity.created(uri).body(topicoCreado);
//    }

    @PostMapping
    public ResponseEntity<TopicoResponseDTO> crearTopico(
            @Valid @RequestBody TopicoRequestDTO request,
            UriComponentsBuilder uriBuilder,
            Authentication authentication) {

        String emailAutor = authentication.getName();
        TopicoResponseDTO topicoCreado = topicoService.crearTopico(request, emailAutor);
        // ... resto del código
        URI uri = uriBuilder.path("/api/topicos/{id}")
                .buildAndExpand(topicoCreado.id())
                .toUri();

        return ResponseEntity.created(uri).body(topicoCreado);
    }




    // Listar todos los tópicos
    @GetMapping
    public ResponseEntity<Page<TopicoListaDTO>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") Boolean soloActivos) {

        Page<TopicoListaDTO> topicos;

        if (soloActivos) {
            topicos = topicoService.listarTopicosActivos(pageable);
        } else {
            topicos = topicoService.listarTopicos(pageable);
        }

        return ResponseEntity.ok(topicos);
    }

    // Obtener tópico específico
    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> obtenerTopico(@PathVariable Long id) {
        TopicoResponseDTO topico = topicoService.obtenerTopicoPorId(id);
        return ResponseEntity.ok(topico);
    }

    // Actualizar tópico
    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> actualizarTopico(
            @PathVariable Long id,
            @Valid @RequestBody TopicoRequestDTO request,
            Authentication authentication) {

        String emailAutor = authentication.getName();
        TopicoResponseDTO topicoActualizado = topicoService.actualizarTopico(id, request, emailAutor);
        return ResponseEntity.ok(topicoActualizado);
    }

    // Eliminar tópico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTopico(
            @PathVariable Long id,
            Authentication authentication) {
        String emailAutor = authentication.getName();
        topicoService.eliminarTopico(id, emailAutor);
        return ResponseEntity.noContent().build();
    }

    // Cambiar status del tópico
    @PatchMapping("/{id}/status")
    public ResponseEntity<TopicoResponseDTO> cambiarStatus(
            @PathVariable Long id,
            @RequestParam StatusTopico status,
            Authentication authentication) {

        // Por ahora simulamos un usuario fijo
        //String emailAutor = "juan@email.com";

        String emailAutor = authentication.getName();
        TopicoResponseDTO topicoActualizado = topicoService.cambiarStatus(id, status, emailAutor);
        return ResponseEntity.ok(topicoActualizado);
    }

    // Buscar tópicos por título
    @GetMapping("/buscar")
    public ResponseEntity<Page<TopicoListaDTO>> buscarTopicos(
            @RequestParam String titulo,
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<TopicoListaDTO> topicos = topicoService.buscarPorTitulo(titulo, pageable);
        return ResponseEntity.ok(topicos);
    }

    // Obtener estadísticas
    @GetMapping("/estadisticas")
    public ResponseEntity<TopicoService.EstadisticasDTO> obtenerEstadisticas() {
        TopicoService.EstadisticasDTO estadisticas = topicoService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}