package proyectocompiladores1.lexico;

import java.util.ArrayList;
import java.util.List;
import proyectocompiladores1.io.ArchivoFuente;

/**
 * ===== ETAPA 1: OBTENCIÓN DE TOKENS =====
 *
 * Recorre cada línea carácter por carácter y la divide en lexemas
 * (los pedazos de texto con significado). Los espacios separan lexemas y
 * se descartan.
 *
 * Esta etapa solo CORTA el texto; no decide si un lexema es correcto.
 * Por eso un identificador mal formado como "$1variable" o "$-dato" se
 * corta completo, para que la etapa 4 lo pueda reportar tal cual.
 *
 * Reglas de corte según el primer carácter del lexema:
 *   "//" o "#"       comentario hasta el final de la línea (su contenido no se divide)
 *   "/" al inicio    intento de comentario mal escrito (R5), hasta el final de la línea
 *   "<?"             intento de etiqueta de apertura (incluye "<? php")
 *   "?" ... ">"      intento de etiqueta de cierre (incluye "? >")
 *   comillas         cadena hasta la comilla de cierre
 *   "$"              identificador hasta un separador
 *   dígito           número
 *   letra o "_"      palabra
 *   otro             operador, delimitador o carácter desconocido
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class AnalizadorLexico {

    /** Caracteres que terminan un identificador ($nombre). */
    private static final String SEPARADORES_IDENTIFICADOR = ";=(),[]{}.+-*/<>!\"'?:&|%^~#";

    /** Caracteres que no pueden ir justo después de '$' ni siquiera como error. */
    private static final String DELIMITADORES_FUERTES = ";=(),[]{}";

    /** Operadores de dos o tres caracteres, del más largo al más corto. */
    private static final String[] OPERADORES_COMPUESTOS = {
        "===", "!==", "==", "!=", "<>", "<=", ">=", "++", "--", "+=", "-=",
        "*=", "/=", ".=", "&&", "||", "->", "=>", "::"};

    /**
     * @param fuente archivo leído
     * @return todos los tokens del archivo, en orden (aún sin clasificar)
     */
    public List<Token> obtenerTokens(ArchivoFuente fuente) {
        List<Token> tokens = new ArrayList<>();
        for (int numero = 1; numero <= fuente.getCantidadLineas(); numero++) {
            tokens.addAll(obtenerTokensDeLinea(fuente.getLinea(numero), numero));
        }
        return tokens;
    }

    /**
     * @param linea  texto de la línea
     * @param numero número de línea (desde 1)
     * @return tokens de esa línea
     */
    public List<Token> obtenerTokensDeLinea(String linea, int numero) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < linea.length()) {
            if (Character.isWhitespace(linea.charAt(i))) {
                i++;
                continue;
            }
            int fin = finDelLexema(linea, i);
            tokens.add(new Token(linea.substring(i, fin), numero, i + 1));
            i = fin;
        }
        return tokens;
    }

    /** Decide dónde termina el lexema que empieza en la posición i. */
    private int finDelLexema(String linea, int i) {
        char c = linea.charAt(i);

        // Comentarios: todo hasta el final de la línea es un solo token (R5)
        if (linea.startsWith("//", i) || c == '#') {
            return linea.length();
        }
        // Una línea que empieza con una sola '/' es un intento de comentario
        // mal escrito (no puede ser una división porque no hay nada antes)
        if (c == '/' && linea.substring(0, i).isBlank() && !linea.startsWith("/=", i)) {
            return linea.length();
        }
        // Etiquetas (R4)
        if (linea.startsWith("<?", i)) {
            return finEtiquetaApertura(linea, i);
        }
        if (c == '?') {
            int fin = finEtiquetaCierre(linea, i);
            if (fin > 0) {
                return fin;
            }
        }
        if (c == '"' || c == '\'') {
            return finCadena(linea, i, c);
        }
        if (c == '$') {
            return finIdentificador(linea, i);
        }
        if (Character.isDigit(c)) {
            return finNumero(linea, i);
        }
        if (Character.isLetter(c) || c == '_') {
            return finPalabra(linea, i);
        }
        return finOperador(linea, i);
    }

    /** "<?php", y también las formas mal escritas "<?", "<? php", "<?PHP". */
    private int finEtiquetaApertura(String linea, int i) {
        int j = i + 2;
        while (j < linea.length() && linea.charAt(j) == ' ') {
            j++;
        }
        if (linea.regionMatches(true, j, "php", 0, 3)) {
            return j + 3;
        }
        return i + 2;
    }

    /** "?>" o la forma mal escrita "? >". Devuelve -1 si el '?' no es un cierre. */
    private int finEtiquetaCierre(String linea, int i) {
        int j = i + 1;
        while (j < linea.length() && linea.charAt(j) == ' ') {
            j++;
        }
        if (j < linea.length() && linea.charAt(j) == '>') {
            return j + 1;
        }
        return -1;
    }

    /** Hasta la comilla de cierre (saltando \" escapadas) o el final de la línea si falta. */
    private int finCadena(String linea, int i, char comilla) {
        int j = i + 1;
        while (j < linea.length()) {
            char c = linea.charAt(j);
            if (c == '\\') {
                j += 2;
            } else if (c == comilla) {
                return j + 1;
            } else {
                j++;
            }
        }
        return linea.length();
    }

    /**
     * '$' y todo lo que sigue hasta un separador. El primer carácter después
     * de '$' se incluye aunque sea inválido (por ejemplo '-' en "$-dato")
     * para poder reportar el identificador completo.
     */
    private int finIdentificador(String linea, int i) {
        int j = i + 1;
        if (j < linea.length() && !Character.isWhitespace(linea.charAt(j))
                && DELIMITADORES_FUERTES.indexOf(linea.charAt(j)) < 0) {
            j++;
        }
        while (j < linea.length() && !Character.isWhitespace(linea.charAt(j))
                && SEPARADORES_IDENTIFICADOR.indexOf(linea.charAt(j)) < 0) {
            j++;
        }
        return j;
    }

    private int finNumero(String linea, int i) {
        int j = i;
        while (j < linea.length() && Character.isDigit(linea.charAt(j))) {
            j++;
        }
        if (j + 1 < linea.length() && linea.charAt(j) == '.' && Character.isDigit(linea.charAt(j + 1))) {
            j++;
            while (j < linea.length() && Character.isDigit(linea.charAt(j))) {
                j++;
            }
        }
        return j;
    }

    private int finPalabra(String linea, int i) {
        int j = i;
        while (j < linea.length()
                && (Character.isLetterOrDigit(linea.charAt(j)) || linea.charAt(j) == '_')) {
            j++;
        }
        return j;
    }

    /** Operador compuesto si coincide; si no, un solo carácter. */
    private int finOperador(String linea, int i) {
        for (String operador : OPERADORES_COMPUESTOS) {
            if (linea.startsWith(operador, i)) {
                return i + operador.length();
            }
        }
        return i + 1;
    }
}
