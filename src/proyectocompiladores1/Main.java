package proyectocompiladores1;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import proyectocompiladores1.errores.CodigoError;
import proyectocompiladores1.errores.GestorErrores;
import proyectocompiladores1.io.ArchivoFuente;
import proyectocompiladores1.io.EscritorLog;
import proyectocompiladores1.io.LectorArchivoPHP;
import proyectocompiladores1.io.LocalizadorArchivo;
import proyectocompiladores1.lexico.AnalizadorLexico;
import proyectocompiladores1.lexico.ClasificadorTokens;
import proyectocompiladores1.lexico.Token;
import proyectocompiladores1.lexico.ValidadorLexema;

/**
 * Punto de entrada del analizador léxico de PHP.
 *
 * Curso: 03307 Compiladores - UNED, III Cuatrimestre 2026. Proyecto 1.
 *
 * Uso:  java -jar ProyectoCompiladores1.jar           analiza los .php de la carpeta del .jar
 *       java -jar ProyectoCompiladores1.jar -tokens   además muestra la tabla de tokens
 *
 * Flujo para cada archivo .php:
 *   Lectura         -> LectorArchivoPHP     (R1)
 *   Etapa 1         -> AnalizadorLexico     obtención de tokens
 *   Etapa 2         -> ClasificadorTokens   clasificación de tokens
 *   Etapa 3         -> Patrones             patrón de cada tipo (lo usan las etapas 2 y 4)
 *   Etapa 4         -> ValidadorLexema      validación del lexema (R3, R4, R5)
 *   Archivo de log  -> EscritorLog          nombreArchivo-errores.log (R2)
 *
 * Uso de IA: la estructura de esta clase fue generada con apoyo de
 * Claude Code (Anthropic) y revisada por el estudiante.
 */
public class Main {

    public static void main(String[] args) {
        boolean mostrarTokens = args.length > 0 && args[0].equalsIgnoreCase("-tokens");

        LocalizadorArchivo localizador = new LocalizadorArchivo();
        Path carpeta = localizador.obtenerCarpetaDeEjecucion();
        System.out.println("=== Analizador lexico PHP ===");
        System.out.println("Carpeta: " + carpeta);

        List<Path> archivos;
        try {
            for (Path ignorado : localizador.buscarArchivosConExtensionInvalida(carpeta)) {
                mostrarError(CodigoError.EXTENSION_INVALIDA, ignorado.getFileName());
            }
            archivos = localizador.buscarArchivosPhp(carpeta);
        } catch (IOException e) {
            mostrarError(CodigoError.ERROR_LECTURA, carpeta, e.getMessage());
            return;
        }

        // Decisión Q1: sin archivos .php se avisa en consola y no se genera log
        if (archivos.isEmpty()) {
            mostrarError(CodigoError.ARCHIVO_NO_ENCONTRADO, carpeta);
            return;
        }

        for (Path ruta : archivos) {
            analizarArchivo(ruta, mostrarTokens);
        }
    }

    /** Ejecuta todas las etapas sobre un archivo y genera su log. */
    private static void analizarArchivo(Path ruta, boolean mostrarTokens) {
        System.out.println();
        System.out.println("Archivo: " + ruta.getFileName());
        try {
            // Lectura respetando el formato original (R1)
            ArchivoFuente fuente = new LectorArchivoPHP().leer(ruta);

            // Etapa 1: obtención de tokens
            List<Token> tokens = new AnalizadorLexico().obtenerTokens(fuente);

            // Etapa 2: clasificación (usa los patrones de la etapa 3)
            new ClasificadorTokens().clasificar(tokens);

            // Etapa 4: validación de cada lexema según las reglas definidas
            GestorErrores errores = new GestorErrores();
            ValidadorLexema validador = new ValidadorLexema();
            validador.validar(tokens, fuente, errores);

            // Archivo de errores (R2)
            Path log = new EscritorLog().escribir(fuente, errores);

            if (mostrarTokens) {
                System.out.println("  LINEA:COL TIPO                 LEXEMA");
                for (Token token : tokens) {
                    System.out.println("  " + token);
                }
                System.out.println("  Tabla de simbolos (distingue mayusculas):");
                for (Map.Entry<String, Integer> simbolo : validador.getTablaDeSimbolos().entrySet()) {
                    System.out.println("    " + simbolo.getKey() + "  (linea " + simbolo.getValue() + ")");
                }
            }
            System.out.println("  Lineas: " + fuente.getCantidadLineas()
                    + " | Tokens: " + tokens.size()
                    + " | Errores: " + errores.total());
            System.out.println("  Log generado: " + log.getFileName());
        } catch (IOException e) {
            mostrarError(CodigoError.ERROR_LECTURA, ruta.getFileName(), e.getMessage());
        }
    }

    private static void mostrarError(CodigoError codigo, Object... datos) {
        System.out.println("Error " + codigo.getNumero() + ". " + codigo.describir(datos));
    }
}
