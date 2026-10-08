package proyectocompiladores1.lexico;

import java.util.List;
import java.util.Map;
import proyectocompiladores1.errores.CodigoError;
import proyectocompiladores1.errores.GestorErrores;
import proyectocompiladores1.io.ArchivoFuente;
import proyectocompiladores1.validacion.ValidadorComentarios;
import proyectocompiladores1.validacion.ValidadorEtiquetas;
import proyectocompiladores1.validacion.ValidadorIdentificadores;

/**
 * ===== ETAPA 4: VALIDACIÓN DEL LEXEMA =====
 *
 * Comprueba que cada lexema cumpla las reglas definidas para su tipo.
 * Las reglas generales están aquí; las de cada requerimiento están en
 * su propio validador (paquete validacion) para que sea fácil ubicarlas:
 *   R3 identificadores -> ValidadorIdentificadores
 *   R4 etiquetas       -> ValidadorEtiquetas
 *   R5 comentarios     -> ValidadorComentarios
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class ValidadorLexema {

    private final ValidadorIdentificadores identificadores = new ValidadorIdentificadores();
    private final ValidadorEtiquetas etiquetas = new ValidadorEtiquetas();
    private final ValidadorComentarios comentarios = new ValidadorComentarios();

    /**
     * @param tokens  tokens ya clasificados (etapa 2)
     * @param fuente  archivo original (las reglas de etiquetas miran líneas completas)
     * @param errores donde se registran los errores encontrados
     */
    public void validar(List<Token> tokens, ArchivoFuente fuente, GestorErrores errores) {
        validarReglasGenerales(tokens, errores);
        identificadores.validar(tokens, errores);       // R3
        etiquetas.validar(tokens, fuente, errores);     // R4
        comentarios.validar(tokens, errores);           // R5
    }

    /** Cadenas sin cerrar (502) y caracteres que no pertenecen al lenguaje (501). */
    private void validarReglasGenerales(List<Token> tokens, GestorErrores errores) {
        for (Token token : tokens) {
            if (token.es(TipoToken.CADENA) && !Patrones.CADENA.matcher(token.getLexema()).matches()) {
                errores.registrar(CodigoError.CADENA_SIN_CERRAR, token.getLinea(), token.getColumna(),
                        token.getLexema());
            }
            if (token.es(TipoToken.DESCONOCIDO)) {
                errores.registrar(CodigoError.CARACTER_NO_RECONOCIDO, token.getLinea(), token.getColumna(),
                        token.getLexema());
            }
        }
    }

    /** @return tabla de símbolos (identificador exacto -> primera línea), distingue mayúsculas. */
    public Map<String, Integer> getTablaDeSimbolos() {
        return identificadores.getTablaDeSimbolos();
    }
}
