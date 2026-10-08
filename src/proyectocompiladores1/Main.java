package proyectocompiladores1;

/**
 * Punto de entrada del analizador léxico de PHP.
 *
 * Curso: 03307 Compiladores - UNED, III Cuatrimestre 2026. Proyecto 1.
 *
 * Flujo previsto (se implementa por etapas, ver tablero de Trello):
 *   1. Localizar el archivo .php en la carpeta del .jar  (io)
 *   2. Leer el archivo respetando su formato original     (io)
 *   3. Obtener, clasificar y validar los tokens           (lexico)
 *   4. Aplicar las reglas R3, R4 y R5                     (validacion)
 *   5. Generar nombreArchivo-errores.log                  (io + errores)
 *
 * Uso de IA: la estructura base de esta clase fue generada con apoyo de
 * Claude Code (Anthropic) y revisada por el estudiante.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Analizador lexico PHP - estructura inicial (T01)");
    }
}
