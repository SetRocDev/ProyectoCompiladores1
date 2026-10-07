package proyectocompiladores1;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import proyectocompiladores1.errores.CodigoError;
import proyectocompiladores1.errores.GestorErrores;
import proyectocompiladores1.io.ArchivoFuente;
import proyectocompiladores1.io.EscritorLog;
import proyectocompiladores1.io.LectorArchivoPHP;
import proyectocompiladores1.io.LocalizadorArchivo;

/**
 * Punto de entrada del analizador léxico de PHP.
 *
 * Curso: 03307 Compiladores - UNED, III Cuatrimestre 2026. Proyecto 1.
 *
 * Flujo previsto (se implementa por etapas, ver tablero de Trello):
 *   1. Localizar el archivo .php en la carpeta del .jar  (io)        [T03]
 *   2. Leer el archivo respetando su formato original     (io)        [T03]
 *   3. Obtener, clasificar y validar los tokens           (lexico)    [T05-T06]
 *   4. Aplicar las reglas R3, R4 y R5                     (validacion)[T07-T09]
 *   5. Generar nombreArchivo-errores.log                  (io)        [T04]
 *
 * Uso de IA: la estructura base de esta clase fue generada con apoyo de
 * Claude Code (Anthropic) y revisada por el estudiante.
 */
public class Main {

    public static void main(String[] args) {
        LocalizadorArchivo localizador = new LocalizadorArchivo();
        Path carpeta = localizador.obtenerCarpetaDeEjecucion();
        System.out.println("Analizador lexico PHP");
        System.out.println("Carpeta de busqueda: " + carpeta);

        List<Path> archivos;
        try {
            for (Path ignorado : localizador.buscarArchivosConExtensionInvalida(carpeta)) {
                System.out.println("Error " + CodigoError.EXTENSION_INVALIDA.getNumero() + ". "
                        + CodigoError.EXTENSION_INVALIDA.describir(ignorado.getFileName()));
            }
            archivos = localizador.buscarArchivosPhp(carpeta);
        } catch (IOException e) {
            System.out.println("Error " + CodigoError.ERROR_LECTURA.getNumero() + ". "
                    + CodigoError.ERROR_LECTURA.describir(carpeta, e.getMessage()));
            return;
        }

        // Decisión Q1: sin archivos .php se avisa en consola y no se genera log
        if (archivos.isEmpty()) {
            System.out.println("Error " + CodigoError.ARCHIVO_NO_ENCONTRADO.getNumero() + ". "
                    + CodigoError.ARCHIVO_NO_ENCONTRADO.describir(carpeta));
            return;
        }

        LectorArchivoPHP lector = new LectorArchivoPHP();
        EscritorLog escritor = new EscritorLog();
        for (Path ruta : archivos) {
            try {
                ArchivoFuente fuente = lector.leer(ruta);
                System.out.println("Archivo leido: " + ruta.getFileName()
                        + " (" + fuente.getCantidadLineas() + " lineas, "
                        + fuente.getCodificacion() + ")");

                // Las etapas de análisis (T05-T09) llenarán este gestor
                GestorErrores errores = new GestorErrores();

                Path log = escritor.escribir(fuente, errores);
                System.out.println("  Log generado: " + log.getFileName()
                        + " (" + errores.total() + " errores)");
            } catch (IOException e) {
                System.out.println("Error " + CodigoError.ERROR_LECTURA.getNumero() + ". "
                        + CodigoError.ERROR_LECTURA.describir(ruta.getFileName(), e.getMessage()));
            }
        }
    }
}
