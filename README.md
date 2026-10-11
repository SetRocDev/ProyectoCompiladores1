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
- Para ejecutar: Java 8 o superior. El proyecto se compila con `javac.source/target = 1.8`, así que el `.jar` corre con cualquier Java instalado (probado con Java 8 y Java 26).
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

## Las 4 etapas del análisis léxico
| Etapa | Clase | Qué hace |
|---|---|---|
| 1. Obtención de tokens | [AnalizadorLexico](src/proyectocompiladores1/lexico/AnalizadorLexico.java) | Recorre cada línea carácter por carácter y corta los lexemas |
| 2. Clasificación | [ClasificadorTokens](src/proyectocompiladores1/lexico/ClasificadorTokens.java) | Asigna a cada lexema su tipo de token |
| 3. Patrón | [Patrones](src/proyectocompiladores1/lexico/Patrones.java) | Expresiones regulares y listas que definen cada tipo |
| 4. Validación del lexema | [ValidadorLexema](src/proyectocompiladores1/lexico/ValidadorLexema.java) | Aplica las reglas de identificadores, etiquetas y comentarios |

## Evidencia del proceso de desarrollo
El proyecto se desarrolló por etapas. Cada etapa tiene una tarjeta en el tablero de Trello (listas *Project Resources, Questions, To Do, In Progress, Code Review, Done*), una rama de Git y un Pull Request con la descripción del cambio, el requerimiento que cubre y cómo se probó.

- Tablero de Trello: https://trello.com/b/7hmp5Ixn/proyecto-compiladores
- Pull Requests (todos fusionados): [lista completa](../../pulls?q=is%3Apr+is%3Amerged)
- Historial de commits: [commits de main](../../commits/main)

| Tarea | Contenido | Pull Request |
|---|---|---|
| T01 | Proyecto NetBeans *Java with Ant* y estructura del repositorio | [#1](../../pull/1) |
| T02 | Modelo de errores (catálogo, error, gestor) | [#2](../../pull/2) |
| T03 | R1: lectura del archivo `.php` | [#3](../../pull/3) |
| T04 | R2: generación de `nombreArchivo-errores.log` | [#4](../../pull/4) |
| T05 | R6: etapa 1 (obtención de tokens) y etapa 3 (patrones) | [#5](../../pull/5) |
| T06 | R6: etapa 2 (clasificación) y etapa 4 (validación) | [#6](../../pull/6) |
| T07 | R5: comentarios `//` y `#` | [#7](../../pull/7) |
| T08 | R4: etiquetas `<?php` y `?>` | [#8](../../pull/8) |
| T09 | R3: identificadores | [#9](../../pull/9) |
| T10 | Integración en `Main` y ejecución del `.jar` | [#10](../../pull/10) |
| T11 | Batería de pruebas `.php` | [#11](../../pull/11) |
| T12 | R7: `INFORME.txt` y documentación | [#12](../../pull/12) |
| T13 | Compatibilidad con Java 8 y preparación de la entrega | [#13](../../pull/13) |

Notas del flujo:
- Una rama por tarea (`feature/Txx-descripcion`), con un PR hacia `main`.
- Los `.php` se guardan byte a byte (`.gitattributes`: `*.php -text`), porque su formato exacto es parte de lo que se evalúa.

## Uso de inteligencia artificial
Se utilizó Claude Code (Anthropic) como asistente supervisado. El detalle, con los prompts principales, está en la sección 10 de [INFORME.txt](INFORME.txt).
