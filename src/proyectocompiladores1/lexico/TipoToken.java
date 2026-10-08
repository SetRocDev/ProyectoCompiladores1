package proyectocompiladores1.lexico;

/**
 * Tipos de token (categorías léxicas) que reconoce el analizador.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public enum TipoToken {
    ETIQUETA_APERTURA("Etiqueta de apertura"),   // <?php
    ETIQUETA_CIERRE("Etiqueta de cierre"),       // ?>
    IDENTIFICADOR("Identificador"),              // $variable
    PALABRA_RESERVADA("Palabra reservada"),      // echo, if, for...
    PALABRA("Palabra / función"),                // array, nombres de función
    NUMERO("Número"),                            // 85, 3.5
    CADENA("Cadena de texto"),                   // "texto" o 'texto'
    ASIGNACION("Asignación"),                    // =
    OPERADOR("Operador"),                        // + - * / . == >= < ...
    DELIMITADOR("Delimitador"),                  // ; , ( ) { } [ ]
    COMENTARIO("Comentario"),                    // // texto   o   # texto
    DESCONOCIDO("Desconocido");                  // carácter que no pertenece al lenguaje

    private final String descripcion;

    TipoToken(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
