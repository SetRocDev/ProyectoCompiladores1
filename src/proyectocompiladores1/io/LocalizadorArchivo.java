package proyectocompiladores1.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import proyectocompiladores1.Main;

/**
 * Encuentra los archivos .php que se deben analizar, sin depender de un
 * nombre fijo ni de una ruta escrita en el código (R1).
 *
 * Regla (decisión Q1): se procesan todos los archivos cuyo nombre termina
 * exactamente en ".php" (en minúscula) y que están en la misma carpeta del
 * .jar. Los archivos con extensiones parecidas (.PHP, .php.txt, .phtml) no se
 * procesan, pero se informan para que el usuario sepa por qué se ignoraron.
 *
 * Uso de IA: estructura generada con apoyo de Claude Code y revisada por el estudiante.
 */
public class LocalizadorArchivo {

    /** Extensión exacta aceptada (comparación sensible a mayúsculas). */
    public static final String EXTENSION = ".php";

    /**
     * Determina la carpeta donde se deben buscar los archivos .php.
     *
     * - Si el programa corre desde el .jar (java -jar ...), es la carpeta que
     *   contiene el .jar, sin importar desde dónde se ejecute el comando.
     * - Si corre desde NetBeans (clases sueltas en build/classes), se usa la
     *   carpeta de trabajo, que en NetBeans es la raíz del proyecto.
     *
     * @return carpeta de búsqueda
     */
    public Path obtenerCarpetaDeEjecucion() {
        try {
            Path origen = Paths.get(Main.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            if (Files.isRegularFile(origen)) {
                return origen.getParent();
            }
        } catch (URISyntaxException | SecurityException | NullPointerException e) {
            // Si no se puede determinar el origen, se usa la carpeta de trabajo
        }
        return Paths.get(System.getProperty("user.dir"));
    }

    /**
     * @param carpeta carpeta donde buscar
     * @return archivos con extensión exacta .php, ordenados por nombre
     * @throws IOException si la carpeta no se puede listar
     */
    public List<Path> buscarArchivosPhp(Path carpeta) throws IOException {
        List<Path> encontrados = new ArrayList<>();
        try (DirectoryStream<Path> contenido = Files.newDirectoryStream(carpeta)) {
            for (Path archivo : contenido) {
                if (Files.isRegularFile(archivo) && tieneExtensionPhp(archivo)) {
                    encontrados.add(archivo);
                }
            }
        }
        encontrados.sort(null);
        return encontrados;
    }

    /**
     * Busca archivos que parecen PHP pero no tienen la extensión exacta, por
     * ejemplo "prueba.PHP", "prueba.php.txt" o "prueba.phtml".
     *
     * @param carpeta carpeta donde buscar
     * @return archivos ignorados, ordenados por nombre
     * @throws IOException si la carpeta no se puede listar
     */
    public List<Path> buscarArchivosConExtensionInvalida(Path carpeta) throws IOException {
        List<Path> ignorados = new ArrayList<>();
        try (DirectoryStream<Path> contenido = Files.newDirectoryStream(carpeta)) {
            for (Path archivo : contenido) {
                String nombre = archivo.getFileName().toString().toLowerCase(Locale.ROOT);
                boolean parecephp = nombre.endsWith(".php") || nombre.contains(".php.")
                        || nombre.endsWith(".phtml");
                if (Files.isRegularFile(archivo) && parecephp && !tieneExtensionPhp(archivo)) {
                    ignorados.add(archivo);
                }
            }
        }
        ignorados.sort(null);
        return ignorados;
    }

    /**
     * @return true si el nombre termina exactamente en ".php" y tiene algo
     *         antes de la extensión (".php" solo no es un nombre válido)
     */
    public boolean tieneExtensionPhp(Path archivo) {
        String nombre = archivo.getFileName().toString();
        return nombre.endsWith(EXTENSION) && nombre.length() > EXTENSION.length();
    }
}
