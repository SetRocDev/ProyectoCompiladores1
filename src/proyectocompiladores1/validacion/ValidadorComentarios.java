package proyectocompiladores1.validacion;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import proyectocompiladores1.errores.CodigoError;
import proyectocompiladores1.errores.GestorErrores;
import proyectocompiladores1.lexico.Patrones;
import proyectocompiladores1.lexico.TipoToken;
import proyectocompiladores1.lexico.Token;

/**
 * R5 - Comentarios de línea (// y #).
 *
 * Aspecto 1 (reconocimiento): el AnalizadorLexico reconoce "//" y "#" y
 *   deja todo el resto de la línea en un único token COMENTARIO.
 * Aspecto 2 (delimitación): el comentario termina al final de la línea.
 *   Si en la línea siguiente aparece texto suelto (por ejemplo
 *   "continúa aquí"), es un comentario que se quiso extender: Error 402.
 *   Un comentario escrito con una sola barra ("/ texto"): Error 401.
 * Aspecto 3 (independencia): como el comentario es un solo token, su
 *   contenido ("// if while echo") nunca genera otros tokens ni errores.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class ValidadorComentarios {

    /** Palabras después de las cuales viene un nombre (de clase, función o miembro). */
    private static final Set<String> ANTES_DE_UN_NOMBRE = Set.of("class", "function", "new", "->", "::");

    public void validar(List<Token> tokens, GestorErrores errores) {
        validarFormaDeComentarios(tokens, errores);
        validarTextoSuelto(tokens, errores);
    }

    /** Error 401: el comentario debe iniciar exactamente con "//" o "#". */
    private void validarFormaDeComentarios(List<Token> tokens, GestorErrores errores) {
        for (Token token : tokens) {
            if (token.es(TipoToken.COMENTARIO) && !Patrones.COMENTARIO.matcher(token.getLexema()).matches()) {
                errores.registrar(CodigoError.COMENTARIO_MAL_FORMADO, token.getLinea(), token.getColumna(),
                        token.getLexema().strip());
            }
        }
    }

    /**
     * Error 402: palabras sueltas que no son código PHP. Las palabras seguidas
     * de la misma línea se reportan juntas en un solo error.
     */
    private void validarTextoSuelto(List<Token> tokens, GestorErrores errores) {
        int i = 0;
        while (i < tokens.size()) {
            if (!esTextoSuelto(tokens, i)) {
                i++;
                continue;
            }
            Token primero = tokens.get(i);
            StringBuilder texto = new StringBuilder(primero.getLexema());
            i++;
            while (i < tokens.size() && esTextoSuelto(tokens, i)
                    && tokens.get(i).getLinea() == primero.getLinea()) {
                texto.append(' ').append(tokens.get(i).getLexema());
                i++;
            }
            errores.registrar(CodigoError.TEXTO_FUERA_DE_COMENTARIO, primero.getLinea(), primero.getColumna(),
                    texto.toString());
        }
    }

    /**
     * Una PALABRA es código válido si es una llamada a función (le sigue "("),
     * un nombre después de class/function/new/->/::, o una palabra clave de
     * PHP. Si le sigue "=" es un identificador sin '$', que reporta R3 (206).
     * En cualquier otro caso es texto suelto.
     */
    private boolean esTextoSuelto(List<Token> tokens, int i) {
        Token token = tokens.get(i);
        if (!token.es(TipoToken.PALABRA) || Patrones.esOtraPalabraClave(token.getLexema())) {
            return false;
        }
        Token siguiente = vecinoEnLaLinea(tokens, i, +1);
        if (siguiente != null && (siguiente.getLexema().equals("(") || siguiente.es(TipoToken.ASIGNACION))) {
            return false;
        }
        Token anterior = vecinoEnLaLinea(tokens, i, -1);
        return anterior == null || !ANTES_DE_UN_NOMBRE.contains(anterior.getLexema().toLowerCase(Locale.ROOT));
    }

    /** @return el token anterior (-1) o siguiente (+1) si está en la misma línea; si no, null. */
    private Token vecinoEnLaLinea(List<Token> tokens, int i, int paso) {
        int j = i + paso;
        if (j < 0 || j >= tokens.size() || tokens.get(j).getLinea() != tokens.get(i).getLinea()) {
            return null;
        }
        return tokens.get(j);
    }
}
