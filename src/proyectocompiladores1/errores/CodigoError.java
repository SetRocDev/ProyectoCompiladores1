package proyectocompiladores1.errores;

/**
 * Catálogo de errores del analizador léxico.
 *
 * Cada constante define un número de error y una plantilla de descripción
 * (formato de {@link String#format}) que se completa con el lexema o dato
 * concreto que provocó el error, para que el mensaje sea específico y no
 * genérico, como exige el enunciado.
 *
 * Numeración por centenas, una por requerimiento:
 *   1xx  Archivo (se reportan en consola, no hay .log sin archivo)
 *   2xx  Identificadores (R3)
 *   3xx  Etiquetas de apertura y cierre (R4)
 *   4xx  Comentarios (R5)
 *   5xx  Errores léxicos generales
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public enum CodigoError {

    // ===== 1xx: Archivo =====
    ARCHIVO_NO_ENCONTRADO(101, "No se encontró ningún archivo con extensión .php en la carpeta '%s'"),
    EXTENSION_INVALIDA(102, "El archivo '%s' no tiene la extensión exacta .php"),
    ERROR_LECTURA(103, "No se pudo leer el archivo '%s': %s"),

    // ===== 2xx: Identificadores (R3) =====
    ID_INICIA_CON_DIGITO(201, "El identificador '%s' no puede iniciar con un dígito después de '$'"),
    ID_SIMBOLO_INICIAL_INVALIDO(202, "El identificador '%s' tiene el carácter inválido '%s' después de '$'; debe iniciar con una letra o guion bajo"),
    ID_CARACTER_INVALIDO(203, "El identificador '%s' contiene el carácter inválido '%s'; solo se permiten letras, dígitos y guion bajo"),
    ID_PALABRA_RESERVADA(204, "El identificador '%s' usa la palabra reservada '%s' y no puede ser asignado"),
    ID_SIN_NOMBRE(205, "Se encontró el símbolo '$' sin un nombre de identificador"),
    ID_SIN_SIMBOLO_DOLAR(206, "El identificador '%s' no inicia con el símbolo '$'; todo identificador debe iniciar con '$' seguido de una letra o guion bajo"),

    // ===== 3xx: Etiquetas de apertura y cierre (R4) =====
    APERTURA_CONTENIDO_PREVIO(301, "Antes de la etiqueta de apertura '<?php' hay %s; la etiqueta debe ser lo primero del archivo"),
    APERTURA_MAL_ESCRITA(302, "Etiqueta de apertura mal escrita '%s'; se esperaba exactamente '<?php'"),
    APERTURA_FALTANTE(303, "No se encontró la etiqueta de apertura '<?php' al inicio del archivo"),
    CIERRE_MAL_ESCRITO(304, "Etiqueta de cierre mal escrita '%s'; se esperaba exactamente '?>'"),
    CIERRE_FALTANTE(305, "No se encontró la etiqueta de cierre '?>' en el archivo"),
    CIERRE_CONTENIDO_POSTERIOR(306, "Se encontró contenido después de la etiqueta de cierre '?>': '%s'"),
    CIERRE_NO_ES_ULTIMA_LINEA(307, "La etiqueta de cierre '?>' no está en la última línea del archivo; después se encontró: '%s'"),
    CIERRE_ESPACIOS_POSTERIORES(308, "Hay %s después de la etiqueta de cierre '?>'; el archivo debe terminar exactamente en '?>'"),

    // ===== 4xx: Comentarios (R5) =====
    COMENTARIO_MAL_FORMADO(401, "Comentario mal formado '%s'; un comentario de línea debe iniciar con '//' o '#'"),
    TEXTO_FUERA_DE_COMENTARIO(402, "Texto '%s' fuera de un comentario: no es código PHP válido; si es parte de un comentario, cada línea debe iniciar con '//' o '#'"),

    // ===== 5xx: Errores léxicos generales =====
    CARACTER_NO_RECONOCIDO(501, "Carácter no reconocido '%s'"),
    CADENA_SIN_CERRAR(502, "La cadena de texto %s no tiene la comilla de cierre");

    private final int numero;
    private final String plantilla;

    CodigoError(int numero, String plantilla) {
        this.numero = numero;
        this.plantilla = plantilla;
    }

    /** @return número del error, por ejemplo 201. */
    public int getNumero() {
        return numero;
    }

    /**
     * Construye la descripción específica del error completando la plantilla.
     *
     * @param datos valores a insertar (lexema, carácter inválido, etc.)
     * @return descripción lista para escribir en el .log
     */
    public String describir(Object... datos) {
        return datos.length == 0 ? plantilla : String.format(plantilla, datos);
    }
}
