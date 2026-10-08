package proyectocompiladores1.io;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Contenido de un archivo .php tal como se leyó del disco.
 *
 * Las líneas se guardan sin el salto de línea final, pero se conserva todo lo
 * demás (espacios, tabulaciones, líneas en blanco). Además se registran
 * los detalles del formato que no caben en la lista de líneas y que necesitan
 * otras etapas:
 *   - si el archivo termina con salto de línea (R4: nada después de '?>')
 *   - si tiene BOM UTF-8 (R4: nada antes de '<?php')
 *   - el separador de línea original (R2: el .log respeta el formato)
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public final class ArchivoFuente {

    private final Path ruta;
    private final List<String> lineas;
    private final boolean terminaConSaltoDeLinea;
    private final boolean tieneBom;
    private final String separadorLinea;
    private final Charset codificacion;

    public ArchivoFuente(Path ruta, List<String> lineas, boolean terminaConSaltoDeLinea,
                         boolean tieneBom, String separadorLinea, Charset codificacion) {
        this.ruta = ruta;
        this.lineas = Collections.unmodifiableList(lineas);
        this.terminaConSaltoDeLinea = terminaConSaltoDeLinea;
        this.tieneBom = tieneBom;
        this.separadorLinea = separadorLinea;
        this.codificacion = codificacion;
    }

    /** @return ruta completa del archivo leído. */
    public Path getRuta() {
        return ruta;
    }

    /** @return nombre del archivo sin la extensión .php (por ejemplo "CodigoProyecto"). */
    public String getNombreBase() {
        String nombre = ruta.getFileName().toString();
        return nombre.substring(0, nombre.length() - LocalizadorArchivo.EXTENSION.length());
    }

    /** @return líneas del archivo, sin el salto de línea, en orden. */
    public List<String> getLineas() {
        return lineas;
    }

    /** @return cantidad de líneas. */
    public int getCantidadLineas() {
        return lineas.size();
    }

    /**
     * @param numero número de línea, empezando en 1 (como se muestra en el .log)
     * @return el texto de esa línea
     */
    public String getLinea(int numero) {
        return lineas.get(numero - 1);
    }

    /** @return true si después del último carácter hay un salto de línea. */
    public boolean terminaConSaltoDeLinea() {
        return terminaConSaltoDeLinea;
    }

    /** @return true si el archivo inicia con la marca BOM de UTF-8 (EF BB BF). */
    public boolean tieneBom() {
        return tieneBom;
    }

    /** @return separador de línea usado en el archivo ("\r\n" o "\n"). */
    public String getSeparadorLinea() {
        return separadorLinea;
    }

    /** @return codificación con la que se pudo leer el archivo. */
    public Charset getCodificacion() {
        return codificacion;
    }
}
