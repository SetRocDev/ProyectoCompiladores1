package proyectocompiladores1.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee un archivo .php respetando su formato original en su totalidad (R1).
 *
 * Se lee el archivo completo como bytes, en lugar de usar readLine(), porque
 * readLine() pierde información que el analizador necesita: si el archivo
 * termina con un salto de línea, cuál separador usa (CRLF o LF) y si tiene
 * BOM. Esa información es clave para validar que no exista nada después de
 * la etiqueta '?>' (R4).
 *
 * Codificación: se intenta UTF-8 estricto; si el archivo tiene bytes que no
 * son UTF-8 válido (por ejemplo, si se guardó como "ANSI" en el Bloc de
 * notas), se lee como Windows-1252 para no perder las tildes.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class LectorArchivoPHP {

    private static final byte[] BOM_UTF8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    /**
     * @param ruta archivo a leer (debe tener extensión .php)
     * @return el contenido con su información de formato
     * @throws IOException si el archivo no se puede leer
     */
    public ArchivoFuente leer(Path ruta) throws IOException {
        byte[] bytes = Files.readAllBytes(ruta);

        boolean tieneBom = iniciaConBom(bytes);
        int inicio = tieneBom ? BOM_UTF8.length : 0;

        String texto;
        try {
            texto = decodificarEstricto(bytes, inicio, StandardCharsets.UTF_8);
        } catch (CharacterCodingException e) {
            texto = new String(bytes, inicio, bytes.length - inicio, Charset.forName("windows-1252"));
        }

        return new ArchivoFuente(ruta, separarLineas(texto), terminaConSalto(texto),
                tieneBom, detectarSeparador(texto));
    }

    /**
     * Divide el texto en líneas aceptando CRLF, LF o CR. Se usa el límite -1
     * para no descartar líneas vacías. Si el archivo termina en salto de
     * línea, el último elemento vacío no es una línea real y se quita; ese
     * dato queda guardado en terminaConSaltoDeLinea.
     */
    private List<String> separarLineas(String texto) {
        List<String> lineas = new ArrayList<>(List.of(texto.split("\r\n|\n|\r", -1)));
        if (terminaConSalto(texto)) {
            lineas.remove(lineas.size() - 1);
        }
        return lineas;
    }

    private boolean terminaConSalto(String texto) {
        return texto.endsWith("\n") || texto.endsWith("\r");
    }

    /** @return el primer separador encontrado; si no hay ninguno, CRLF (Windows). */
    private String detectarSeparador(String texto) {
        int pos = texto.indexOf('\n');
        if (pos > 0 && texto.charAt(pos - 1) == '\r') {
            return "\r\n";
        }
        if (pos >= 0) {
            return "\n";
        }
        return "\r\n";
    }

    private boolean iniciaConBom(byte[] bytes) {
        return bytes.length >= 3 && bytes[0] == BOM_UTF8[0]
                && bytes[1] == BOM_UTF8[1] && bytes[2] == BOM_UTF8[2];
    }

    /** Decodifica fallando ante bytes inválidos, en vez de reemplazarlos con '?'. */
    private String decodificarEstricto(byte[] bytes, int inicio, Charset charset)
            throws CharacterCodingException {
        return charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes, inicio, bytes.length - inicio))
                .toString();
    }
}
