package proyectocompiladores1.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import proyectocompiladores1.errores.ErrorLexico;
import proyectocompiladores1.errores.GestorErrores;

/**
 * Genera el archivo de errores "nombreArchivo-errores.log" (R2).
 *
 * Formato (decisiones Q2 y Q3):
 *   - Cada línea del .php se copia sin ningún cambio, precedida por su número
 *     de 4 dígitos y un espacio: "0001 <?php".
 *   - Los errores de una línea se escriben justo debajo de ella, uno por
 *     renglón: "Error 201. Descripción" (Opción 1 del enunciado).
 *   - Los errores globales (que no pertenecen a una línea, por ejemplo
 *     "falta la etiqueta de cierre") se escriben al final.
 *
 * El .log se guarda en la misma carpeta del .php, en UTF-8, con el mismo
 * separador de línea que el original. Si ya existe se reemplaza.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class EscritorLog {

    /** Sufijo que se agrega al nombre del archivo original. */
    public static final String SUFIJO = "-errores.log";

    /**
     * @param fuente  archivo .php leído
     * @return ruta del .log que corresponde a ese archivo
     */
    public Path rutaDelLog(ArchivoFuente fuente) {
        return fuente.getRuta().resolveSibling(fuente.getNombreBase() + SUFIJO);
    }

    /**
     * Escribe el .log con todas las líneas numeradas y sus errores.
     *
     * @param fuente  archivo .php leído
     * @param errores errores encontrados durante el análisis
     * @return ruta del .log generado
     * @throws IOException si no se puede escribir el archivo
     */
    public Path escribir(ArchivoFuente fuente, GestorErrores errores) throws IOException {
        Path destino = rutaDelLog(fuente);
        String salto = fuente.getSeparadorLinea();

        try (BufferedWriter out = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            for (int numero = 1; numero <= fuente.getCantidadLineas(); numero++) {
                out.write(numerar(numero, fuente.getLinea(numero)));
                out.write(salto);
                for (ErrorLexico error : errores.erroresDeLinea(numero)) {
                    out.write(error.toString());
                    out.write(salto);
                }
            }
            for (ErrorLexico error : errores.erroresGlobales()) {
                out.write(error.toString());
                out.write(salto);
            }
        }
        return destino;
    }

    /**
     * @return la línea precedida por su número de 4 dígitos y un espacio,
     *         por ejemplo numerar(1, "<?php") devuelve "0001 <?php"
     */
    public String numerar(int numero, String linea) {
        return String.format("%04d %s", numero, linea);
    }
}
