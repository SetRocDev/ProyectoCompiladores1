package proyectocompiladores1.errores;

/**
 * Representa un error concreto detectado durante el análisis.
 *
 * Es inmutable: guarda el código del catálogo, la ubicación (línea y columna)
 * y la descripción ya completada con el lexema que falló.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public final class ErrorLexico {

    /** Valor de línea usado para errores que no pertenecen a una línea concreta. */
    public static final int SIN_LINEA = 0;

    private final CodigoError codigo;
    private final int linea;
    private final int columna;
    private final String descripcion;

    /**
     * @param codigo      tipo de error según el catálogo
     * @param linea       número de línea (desde 1), o {@link #SIN_LINEA} si es global
     * @param columna     columna donde inicia el problema (desde 1), 0 si no aplica
     * @param descripcion descripción específica del error
     */
    public ErrorLexico(CodigoError codigo, int linea, int columna, String descripcion) {
        this.codigo = codigo;
        this.linea = linea;
        this.columna = columna;
        this.descripcion = descripcion;
    }

    public CodigoError getCodigo() {
        return codigo;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Formato del .log (Opción 1, el error va debajo de su línea):
     * {@code Error 201. El identificador '$1x' no puede iniciar con un dígito...}
     */
    @Override
    public String toString() {
        return "Error " + codigo.getNumero() + ". " + descripcion;
    }
}
