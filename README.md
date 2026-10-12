# Analizador léxico de PHP en Java

**Proyecto 1 · Compiladores (03307) · UNED · III Cuatrimestre 2026**
Estudiante: Pablo Cortés

El programa revisa un archivo de código PHP, busca errores de escritura y crea un archivo `nombreArchivo-errores.log` con cada línea numerada y los errores debajo de la línea donde ocurren.

## Cómo probarlo
El programa ya viene compilado. Solo se necesita Java 8 o más nuevo.

1. Presionar el botón verde **Code** y luego **Download ZIP**. Descomprimir el archivo.
2. Entrar a la carpeta **`dist`**. Ahí están el programa (`ProyectoCompiladores1.jar`) y el código de ejemplo (`CodigoProyecto.php`).
3. Abrir el Símbolo del sistema en esa carpeta: hacer clic en la barra de direcciones del Explorador, escribir `cmd` y presionar Enter.
4. Escribir este comando y presionar Enter:
   ```
   java -jar ProyectoCompiladores1.jar
   ```
5. Abrir el archivo nuevo **`CodigoProyecto-errores.log`** con el Bloc de notas.

Las instrucciones completas, en lenguaje sencillo, están en **[INFORME.txt](INFORME.txt)**.

## Qué hay en cada carpeta
| Carpeta o archivo | Qué contiene |
|---|---|
| [`INFORME.txt`](INFORME.txt) | Instrucciones de uso, lista de errores, decisiones y uso de IA |
| [`dist/`](dist) | El programa listo para usar (`.jar`) y el archivo PHP de ejemplo |
| [`src/`](src/proyectocompiladores1) | El código fuente en Java |
| [`pruebas/`](pruebas) | 24 archivos PHP con errores puestos a propósito y la tabla [`esperado.txt`](pruebas/esperado.txt) |
| [`recursos/`](recursos) | Copia de respaldo del código PHP original de la cátedra |
| `nbproject/`, `build.xml`, `manifest.mf`, `test/` | Archivos del proyecto NetBeans (*Java with Ant*) |

## Dónde está cada parte del código
Las 4 etapas del análisis léxico:

| Etapa | Archivo | Qué hace |
|---|---|---|
| 1. Obtención de tokens | [AnalizadorLexico.java](src/proyectocompiladores1/lexico/AnalizadorLexico.java) | Recorre cada línea letra por letra y la separa en pedazos (lexemas) |
| 2. Clasificación | [ClasificadorTokens.java](src/proyectocompiladores1/lexico/ClasificadorTokens.java) | Dice de qué tipo es cada pedazo |
| 3. Patrón | [Patrones.java](src/proyectocompiladores1/lexico/Patrones.java) | Tiene las reglas de cada tipo y las palabras reservadas |
| 4. Validación del lexema | [ValidadorLexema.java](src/proyectocompiladores1/lexico/ValidadorLexema.java) | Revisa que cada pedazo cumpla sus reglas |

Los requisitos del enunciado:

| Requisito | Archivo |
|---|---|
| Lectura del archivo `.php` | [LocalizadorArchivo.java](src/proyectocompiladores1/io/LocalizadorArchivo.java), [LectorArchivoPHP.java](src/proyectocompiladores1/io/LectorArchivoPHP.java) |
| Archivo `-errores.log` | [EscritorLog.java](src/proyectocompiladores1/io/EscritorLog.java) |
| Identificadores | [ValidadorIdentificadores.java](src/proyectocompiladores1/validacion/ValidadorIdentificadores.java) |
| Etiquetas `<?php` y `?>` | [ValidadorEtiquetas.java](src/proyectocompiladores1/validacion/ValidadorEtiquetas.java) |
| Comentarios `//` y `#` | [ValidadorComentarios.java](src/proyectocompiladores1/validacion/ValidadorComentarios.java) |
| Lista de errores | [CodigoError.java](src/proyectocompiladores1/errores/CodigoError.java) |
| Punto de inicio | [Main.java](src/proyectocompiladores1/Main.java) |

## Cómo se desarrolló
El proyecto se hizo por etapas. Cada etapa tiene una tarjeta en Trello y un *Pull Request* en GitHub, donde se explica qué se cambió y cómo se probó.

| Etapa | Contenido | Pull Request |
|---|---|---|
| T01 | Proyecto NetBeans *Java with Ant* | [#1](../../pull/1) |
| T02 | Lista y manejo de errores | [#2](../../pull/2) |
| T03 | Lectura del archivo `.php` | [#3](../../pull/3) |
| T04 | Creación del archivo `-errores.log` | [#4](../../pull/4) |
| T05 | Etapa 1 (obtención de tokens) y etapa 3 (patrones) | [#5](../../pull/5) |
| T06 | Etapa 2 (clasificación) y etapa 4 (validación) | [#6](../../pull/6) |
| T07 | Comentarios | [#7](../../pull/7) |
| T08 | Etiquetas `<?php` y `?>` | [#8](../../pull/8) |
| T09 | Identificadores | [#9](../../pull/9) |
| T10 | Unión de todas las partes y ejecución del `.jar` | [#10](../../pull/10) |
| T11 | Archivos de prueba | [#11](../../pull/11) |
| T12 | INFORME y documentación | [#12](../../pull/12) |
| T13 | Compatibilidad con Java 8 y preparación de la entrega | [#13](../../pull/13), [#14](../../pull/14) |

- Todos los Pull Requests: [lista completa](../../pulls?q=is%3Apr+is%3Amerged)
- Historial de cambios: [commits](../../commits/main)

## Uso de inteligencia artificial
Se usó Claude Code (Anthropic) como asistente supervisado. El detalle y los prompts principales están en la sección 12 de [INFORME.txt](INFORME.txt).
