package alura.forohub.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 10, max = 2000, message = "El mensaje debe tener entre 10 y 2000 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTopico status = StatusTopico.ACTIVO;

    @NotNull(message = "El autor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @NotNull(message = "El curso es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    // Constructores
    public Topico() {}

    public Topico(String titulo, String mensaje, Usuario autor, Curso curso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.autor = autor;
        this.curso = curso;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public void setStatus(StatusTopico status) {
        this.status = status;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    // Métodos de negocio
    public void cerrarTopico() {
        this.status = StatusTopico.CERRADO;
    }

    public void marcarComoSolucionado() {
        this.status = StatusTopico.SOLUCIONADO;
    }

    public void reactivar() {
        this.status = StatusTopico.ACTIVO;
    }

    public boolean esDelAutor(Usuario usuario) {
        return this.autor != null && Objects.equals(this.autor.getId(), usuario.getId());
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topico topico = (Topico) o;
        return Objects.equals(id, topico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Topico{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", status=" + status +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}