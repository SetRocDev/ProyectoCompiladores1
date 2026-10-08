# ProyectoCompiladores1

Analizador léxico de PHP escrito en Java, para el curso **03307 Compiladores** de la UNED (III Cuatrimestre 2026, Proyecto 1).

El desarrollo se asiste con IA supervisada (Claude Code). Cada etapa se controla con una tarjeta de Trello y un Pull Request, para llevar orden y no mezclar cambios.

## Qué hace
1. Busca los archivos con extensión exactamente `.php` en la carpeta donde está el `.jar`.
2. Los lee sin alterar el formato (espacios, líneas en blanco, saltos de línea).
3. Obtiene, clasifica y valida los tokens: identificadores, etiquetas `<?php` y `?>`, y comentarios `//` y `#`.
4. Genera `nombreArchivo-errores.log`: una copia numerada (`0001 `) del archivo, con los errores debajo de cada línea.

## Requisitos
- Apache NetBeans 24 o superior. Proyecto tipo **Java with Ant**.
- Para ejecutar: Java 17 o superior. El proyecto se compila con `javac.source/target = 17`.
- Sin librerías externas.

## Compilar y ejecutar
En NetBeans: *Open Project* → seleccionar esta carpeta → *Clean and Build* (Shift+F11). Se genera `dist/ProyectoCompiladores1.jar`.

Desde CMD:
```
cd dist
copy ..\recursos\CodigoProyecto.php .
java -jar ProyectoCompiladores1.jar
```
`java -jar ProyectoCompiladores1.jar -tokens` muestra además la tabla de tokens y la tabla de símbolos.

Pruebas: copiar `pruebas/*` a `dist/` y ejecutar el `.jar`. Se genera un log por archivo; la tabla de resultados esperados está en [pruebas/esperado.txt](pruebas/esperado.txt).

Los pasos detallados, las decisiones de diseño y el uso de IA están en [INFORME.txt](INFORME.txt).

## Estructura
```
src/proyectocompiladores1/
├── Main.java        punto de entrada
├── io/              localizar, leer y escribir archivos
├── lexico/          etapas del análisis léxico (tokens, patrones, clasificación, validación)
├── validacion/      reglas R3 identificadores, R4 etiquetas, R5 comentarios
└── errores/         catálogo y gestión de errores
recursos/            código PHP original provisto por la cátedra
pruebas/             archivos .php con errores provocados a propósito
nbproject/, build.xml, manifest.mf   proyecto NetBeans (Ant)
```

## Flujo de trabajo
- Una rama por tarea: `feature/Txx-descripcion`, con un PR hacia `main`.
- Los `.php` se guardan byte a byte (`.gitattributes`: `*.php -text`), porque su formato exacto es parte de lo que se evalúa.
