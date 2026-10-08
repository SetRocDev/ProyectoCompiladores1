package proyectocompiladores1.validacion;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import proyectocompiladores1.errores.CodigoError;
import proyectocompiladores1.errores.GestorErrores;
import proyectocompiladores1.lexico.Patrones;
import proyectocompiladores1.lexico.TipoToken;
import proyectocompiladores1.lexico.Token;

/**
 * R3 - Identificadores ($variable).
 *
 * Aspecto 1 (inicio válido): el lexema debe cumplir el patrón
 *   \$[A-Za-z_][A-Za-z0-9_]*  (Patrones.IDENTIFICADOR). Si no lo cumple se
 *   indica la causa exacta: 201 dígito después de '$', 202 símbolo inválido
 *   después de '$', 203 carácter inválido dentro del nombre, 205 '$' solo.
 *   Además, un nombre sin '$' a la izquierda de '=' es el Error 206.
 * Aspecto 2 (mayúsculas y minúsculas): la tabla de símbolos usa el lexema
 *   exacto como clave, por lo que $total y $Total son dos identificadores
 *   distintos.
 * Aspecto 3 (palabras reservadas): el identificador a la izquierda de un
 *   '=' (asignación) no puede ser una palabra reservada: 204. Se compara
 *   sin distinguir mayúsculas (decisión Q5), así que $IF también es error.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class ValidadorIdentificadores {

    /** Tabla de símbolos: identificador exacto -> línea donde aparece por primera vez. */
    private final Map<String, Integer> tablaDeSimbolos = new LinkedHashMap<>();

    public void validar(List<Token> tokens, GestorErrores errores) {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.es(TipoToken.IDENTIFICADOR)) {
                validarInicio(token, errores);
                tablaDeSimbolos.putIfAbsent(token.getLexema(), token.getLinea());
            }
            if (token.es(TipoToken.ASIGNACION)) {
                validarLadoIzquierdo(tokens, i, errores);
            }
        }
    }

    /** Aspecto 1: el identificador debe cumplir el patrón; si no, se indica la causa. */
    private void validarInicio(Token token, GestorErrores errores) {
        String lexema = token.getLexema();
        if (Patrones.IDENTIFICADOR.matcher(lexema).matches()) {
            return;
        }
        int linea = token.getLinea();
        int columna = token.getColumna();
        if (lexema.length() == 1) {
            errores.registrar(CodigoError.ID_SIN_NOMBRE, linea, columna);
            return;
        }
        char segundo = lexema.charAt(1);
        if (Character.isDigit(segundo)) {
            errores.registrar(CodigoError.ID_INICIA_CON_DIGITO, linea, columna, lexema);
        } else if (!esLetraOGuionBajo(segundo)) {
            errores.registrar(CodigoError.ID_SIMBOLO_INICIAL_INVALIDO, linea, columna, lexema, segundo);
        } else {
            for (char c : lexema.substring(2).toCharArray()) {
                if (!esLetraOGuionBajo(c) && !(c >= '0' && c <= '9')) {
                    errores.registrar(CodigoError.ID_CARACTER_INVALIDO, linea, columna, lexema, c);
                    return;
                }
            }
        }
    }

    /**
     * Lo que está justo antes de '=':
     *  - una palabra sin '$' (nombre_estudiante = ...): Error 206 (aspecto 1)
     *  - un identificador que es palabra reservada ($if = ...): Error 204 (aspecto 3)
     */
    private void validarLadoIzquierdo(List<Token> tokens, int posicionAsignacion, GestorErrores errores) {
        if (posicionAsignacion == 0) {
            return;
        }
        Token izquierdo = tokens.get(posicionAsignacion - 1);
        boolean mismaLinea = izquierdo.getLinea() == tokens.get(posicionAsignacion).getLinea();
        if (mismaLinea && (izquierdo.es(TipoToken.PALABRA) || izquierdo.es(TipoToken.PALABRA_RESERVADA))) {
            errores.registrar(CodigoError.ID_SIN_SIMBOLO_DOLAR, izquierdo.getLinea(), izquierdo.getColumna(),
                    izquierdo.getLexema());
        }
        if (mismaLinea && izquierdo.es(TipoToken.IDENTIFICADOR)) {
            String nombre = izquierdo.getLexema().substring(1);
            if (Patrones.esPalabraReservada(nombre)) {
                errores.registrar(CodigoError.ID_PALABRA_RESERVADA, izquierdo.getLinea(), izquierdo.getColumna(),
                        izquierdo.getLexema(), nombre.toLowerCase(Locale.ROOT));
            }
        }
    }

    /** Solo letras del alfabeto inglés (a-z, A-Z) o '_', como indica el enunciado. */
    private boolean esLetraOGuionBajo(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    /** Aspecto 2: identificadores distintos encontrados (distingue mayúsculas). */
    public Map<String, Integer> getTablaDeSimbolos() {
        return tablaDeSimbolos;
    }
}
