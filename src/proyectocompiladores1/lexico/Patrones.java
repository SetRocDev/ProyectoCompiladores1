package proyectocompiladores1.lexico;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ===== ETAPA 3: DETERMINACIÓN DEL PATRÓN =====
 *
 * Reúne en un solo lugar el patrón (la regla) que define a cada tipo de
 * token. Se usan expresiones regulares de java.util.regex (parte del JDK,
 * no es una librería externa) y conjuntos de palabras/símbolos.
 *
 * La etapa 2 usa estos patrones para clasificar y la etapa 4 para validar
 * que el lexema completo cumpla su regla.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public final class Patrones {

    private Patrones() {
    }

    /**
     * Crea un conjunto de solo lectura con los elementos indicados.
     * Se usa en lugar de Set.of(...) para que el programa funcione desde
     * Java 8, que es la versión que suele estar instalada en Windows.
     */
    public static Set<String> conjunto(String... elementos) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elementos)));
    }

    /** Etiquetas exactas (R4). */
    public static final String ETIQUETA_APERTURA = "<?php";
    public static final String ETIQUETA_CIERRE = "?>";

    /** R3: '$' seguido de letra o '_', y luego letras, dígitos o '_'. */
    public static final Pattern IDENTIFICADOR = Pattern.compile("\\$[A-Za-z_][A-Za-z0-9_]*");

    /** Entero o decimal: 85, 3.5 */
    public static final Pattern NUMERO = Pattern.compile("[0-9]+(\\.[0-9]+)?");

    /** Cadena cerrada con comillas dobles o simples (admite \" dentro). */
    public static final Pattern CADENA = Pattern.compile("\"(\\\\.|[^\"\\\\])*\"|'(\\\\.|[^'\\\\])*'");

    /** R5: comentario de línea, inicia con // o # y llega hasta el final de la línea. */
    public static final Pattern COMENTARIO = Pattern.compile("(//|#).*");

    /** Palabra: letra o '_' seguida de letras, dígitos o '_' (admite tildes). */
    public static final Pattern PALABRA = Pattern.compile("[\\p{L}_][\\p{L}\\p{N}_]*");

    /** Las 20 palabras reservadas definidas por el enunciado (en minúscula). */
    public static final Set<String> PALABRAS_RESERVADAS = conjunto(
            "class", "function", "if", "while",
            "echo", "else", "continue", "for",
            "return", "public", "null", "false",
            "true", "trim", "foreach", "private",
            "include", "print", "const", "do");

    /**
     * Otras palabras clave de PHP que no están en la lista del enunciado.
     * No son reservadas para la regla R3, pero tampoco son "texto suelto"
     * (R5): son código PHP válido, por ejemplo "as" en un foreach.
     */
    public static final Set<String> OTRAS_PALABRAS_CLAVE_PHP = conjunto(
            "as", "and", "or", "xor", "new", "elseif", "break", "switch", "case",
            "default", "static", "global", "extends", "implements", "interface",
            "throw", "try", "catch", "finally", "instanceof", "var", "use",
            "namespace", "exit", "die", "endif", "endwhile", "endfor", "endforeach");

    /** Operadores (el '=' solo se considera ASIGNACIÓN, decisión Q5). */
    public static final Set<String> OPERADORES = conjunto(
            "===", "!==", "==", "!=", "<>", "<=", ">=", "<", ">",
            "+", "-", "*", "/", "%", ".", "!", "?", ":",
            "++", "--", "+=", "-=", "*=", "/=", ".=", "&&", "||", "->", "=>", "::");

    public static final String ASIGNACION = "=";

    public static final Set<String> DELIMITADORES = conjunto(";", ",", "(", ")", "{", "}", "[", "]");

    /**
     * Decisión Q5: las palabras reservadas se comparan sin distinguir
     * mayúsculas (en PHP "IF" e "if" son la misma palabra clave).
     */
    public static boolean esPalabraReservada(String palabra) {
        return PALABRAS_RESERVADAS.contains(palabra.toLowerCase(Locale.ROOT));
    }

    public static boolean esOtraPalabraClave(String palabra) {
        return OTRAS_PALABRAS_CLAVE_PHP.contains(palabra.toLowerCase(Locale.ROOT));
    }
}
