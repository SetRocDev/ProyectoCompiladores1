package proyectocompiladores1.lexico;

/**
 * Un token: el lexema (texto exacto encontrado en el código), su ubicación
 * y su tipo.
 *
 * El token nace en la etapa 1 (obtención) solo con el lexema y la ubicación;
 * el tipo se le asigna en la etapa 2 (clasificación).
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class Token {

    private final String lexema;
    private final int linea;
    private final int columna;
    private TipoToken tipo;

    /**
     * @param lexema  texto exacto del token
     * @param linea   número de línea (desde 1)
     * @param columna columna donde inicia (desde 1)
     */
    public Token(String lexema, int linea, int columna) {
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public TipoToken getTipo() {
        return tipo;
    }

    /** Lo usa la etapa 2 (clasificación). */
    public void setTipo(TipoToken tipo) {
        this.tipo = tipo;
    }

    /** @return true si el token es del tipo indicado. */
    public boolean es(TipoToken otroTipo) {
        return tipo == otroTipo;
    }

    /** Fila para la tabla de tokens que se muestra en consola. */
    @Override
    public String toString() {
        String tipoTexto = tipo == null ? "(sin clasificar)" : tipo.getDescripcion();
        return String.format("%04d:%-3d %-20s %s", linea, columna, tipoTexto, lexema);
    }
}
