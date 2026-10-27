# ProyectoTareas

Aplicación de consola desarrollada en Java para gestionar una lista sencilla de tareas. Permite crear, consultar, completar y eliminar tareas desde un menú interactivo.

## Características

- Crear tareas con una descripción.
- Mostrar todas las tareas registradas.
- Marcar tareas como completadas mediante su identificador.
- Eliminar tareas mediante su identificador.
- Validar opciones numéricas y descripciones vacías.
- Limitar a un máximo de 5 tareas activas.
- Asignar identificadores consecutivos a las tareas.

## Requisitos

- JDK 17 o superior.
- Apache Maven 3.6 o superior (opcional si se compila directamente con el JDK).
- Terminal o consola para interactuar con la aplicación.

El proyecto no utiliza dependencias externas: únicamente emplea clases de la biblioteca estándar de Java.

## Estructura del proyecto

```text
ProyectoTareas/
|-- pom.xml
|-- README.md
`-- src/main/java/org/example/
    |-- Main.java
    |-- MenuTareas.java
    |-- GestorTareas.java
    |-- Tarea.java
    |-- DescripcionVaciaException.java
    `-- LimiteTareasException.java
```

- `Main.java`: construye los objetos, inicia el menú y cierra el lector de teclado.
- `MenuTareas.java`: muestra el menú, lee las entradas y presenta los resultados. Cada opción tiene su propio método y delega las operaciones en el gestor.
- `GestorTareas.java`: administra la colección, el límite de cinco tareas, los identificadores y las operaciones de creación, consulta, completado y eliminación, sin depender de la consola.
- `Tarea.java`: representa una tarea con identificador, descripción y estado de finalización.
- `pom.xml`: configura el proyecto Maven y la compilación con Java 17.

Esta separación aplica el principio de responsabilidad única: los cambios del menú no requieren modificar la gestión de tareas y `main` solo se encarga del arranque.

## Compilar el proyecto

Desde la raíz del proyecto, ejecuta:

```bash
mvn clean package
```

Las clases compiladas se generarán en `target/classes` y el artefacto Maven en `target/`.

## Ejecutar la aplicación

Después de compilar, inicia la aplicación con:

```bash
java -cp target/classes org.example.Main
```

También puedes compilar sin limpiar los archivos anteriores:

```bash
mvn compile
java -cp target/classes org.example.Main
```

## Compilar sin Maven (PowerShell)

Desde la raíz del proyecto:

```powershell
javac -encoding UTF-8 -d target/classes src/main/java/org/example/*.java
java -cp target/classes org.example.Main
```

## Uso

Al iniciar la aplicación se muestra un menú similar al siguiente:

```text
========== MENÚ DE TAREAS ==========
Tareas actuales: 0/5
1. Crear tarea
2. Mostrar tareas
3. Marcar tarea como completada
4. Eliminar tarea
5. Salir
====================================
```

### Flujo de ejemplo

1. Selecciona `1` y escribe la descripción de una tarea, por ejemplo `Preparar presentación`.
2. Selecciona `2` para consultar las tareas existentes y sus identificadores.
3. Selecciona `3` e introduce el ID de una tarea para marcarla como completada.
4. Selecciona `4` e introduce el ID de una tarea para eliminarla.
5. Selecciona `5` para salir.

Las tareas se mantienen únicamente en memoria, por lo que se pierden al cerrar la aplicación.


## Limitaciones conocidas

- Solo se pueden mantener 5 tareas a la vez.
- No existe persistencia en archivos ni base de datos.
- La interacción se realiza exclusivamente mediante la consola.
- Los identificadores no se reutilizan después de eliminar una tarea durante la ejecución actual.

