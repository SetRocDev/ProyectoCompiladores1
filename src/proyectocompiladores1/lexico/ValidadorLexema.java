package proyectocompiladores1.lexico;

import java.util.List;
import proyectocompiladores1.errores.CodigoError;
import proyectocompiladores1.errores.GestorErrores;
import proyectocompiladores1.io.ArchivoFuente;
import proyectocompiladores1.validacion.ValidadorComentarios;

/**
 * ===== ETAPA 4: VALIDACIÓN DEL LEXEMA =====
 *
 * Comprueba que cada lexema cumpla las reglas definidas para su tipo.
 * Las reglas generales están aquí; las de cada requerimiento están en
 * su propio validador (paquete validacion) para que sea fácil ubicarlas.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class ValidadorLexema {

    /**
     * @param tokens  tokens ya clasificados (etapa 2)
     * @param fuente  archivo original (algunas reglas miran líneas completas)
     * @param errores donde se registran los errores encontrados
     */
    public void validar(List<Token> tokens, ArchivoFuente fuente, GestorErrores errores) {
        validarReglasGenerales(tokens, errores);
        new ValidadorComentarios().validar(tokens, errores);           // R5
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
}
