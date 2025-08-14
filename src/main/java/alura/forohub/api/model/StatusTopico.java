package alura.forohub.api.model;


public enum StatusTopico {
    ACTIVO("Activo"),
    CERRADO("Cerrado"),
    SOLUCIONADO("Solucionado");

    private final String descripcion;

    StatusTopico(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}