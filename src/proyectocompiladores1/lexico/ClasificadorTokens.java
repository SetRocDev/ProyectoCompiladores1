package proyectocompiladores1.lexico;

import java.util.List;

/**
 * ===== ETAPA 2: CLASIFICACIÓN DE TOKENS =====
 *
 * Asigna a cada lexema su tipo de token según cómo empieza y según los
 * conjuntos definidos en {@link Patrones}. La clasificación no valida:
 * "$1variable" se clasifica como IDENTIFICADOR (empieza con '$') y es la
 * etapa 4 la que detecta que no cumple el patrón.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class ClasificadorTokens {

    /** Clasifica todos los tokens de la lista. */
    public void clasificar(List<Token> tokens) {
        for (Token token : tokens) {
            token.setTipo(tipoDe(token.getLexema()));
        }
    }

    /**
     * @param lexema texto del token
     * @return el tipo que le corresponde
     */
    public TipoToken tipoDe(String lexema) {
        char primero = lexema.charAt(0);

        if (lexema.startsWith("<?")) {
            return TipoToken.ETIQUETA_APERTURA;
        }
        if (primero == '?' && lexema.endsWith(">")) {
            return TipoToken.ETIQUETA_CIERRE;
        }
        if (lexema.equals(Patrones.ASIGNACION)) {
            return TipoToken.ASIGNACION;
        }
        if (Patrones.OPERADORES.contains(lexema)) {
            return TipoToken.OPERADOR;
        }
        if (Patrones.DELIMITADORES.contains(lexema)) {
            return TipoToken.DELIMITADOR;
        }
        if (primero == '/' || primero == '#') {
            return TipoToken.COMENTARIO;
        }
        if (primero == '$') {
            return TipoToken.IDENTIFICADOR;
        }
        if (primero == '"' || primero == '\'') {
            return TipoToken.CADENA;
        }
        if (Character.isDigit(primero)) {
            return TipoToken.NUMERO;
        }
        if (Patrones.PALABRA.matcher(lexema).matches()) {
            return Patrones.esPalabraReservada(lexema) ? TipoToken.PALABRA_RESERVADA : TipoToken.PALABRA;
        }
        return TipoToken.DESCONOCIDO;
    }
}
