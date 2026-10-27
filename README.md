# ProyectoTareas

Aplicación de consola desarrollada en Java para gestionar una lista sencilla de tareas. Permite crear, consultar, completar y eliminar tareas desde un menú interactivo.

## Características

- Crear tareas con una descripción.
- Mostrar todas las tareas registradas.
- Marcar tareas como completadas mediante su identificador.
- Eliminar tareas mediante su identificador.
- Validar opciones numéricas y descripciones vacías.
- Limitar a un máximo de 10 tareas en la lista, incluidas las completadas.
- Asignar identificadores consecutivos a las tareas.
- Guardar y cargar tareas desde `tareas_guardadas.txt` con codificación UTF-8.
- Utilizar lambdas para recorrer, filtrar y eliminar elementos.
- Manejar errores con excepciones personalizadas y bloques `try-catch`.

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
- `MenuTareas.java`: muestra el menú, lee las entradas, presenta los resultados y gestiona la lectura y escritura del archivo. Delega las operaciones sobre las tareas en el gestor.
- `GestorTareas.java`: administra la colección, el límite de diez tareas, los identificadores y las operaciones de creación, consulta, completado y eliminación, sin depender de la consola.
- `Tarea.java`: representa una tarea con identificador, descripción y estado de finalización.
- `DescripcionVaciaException.java`: indica que una descripción está vacía o es nula.
- `LimiteTareasException.java`: indica que se alcanzó o superó el límite de tareas.
- `pom.xml`: configura el proyecto Maven y la compilación con Java 17.

La gestión de la lista está separada de la interacción con la consola; `main` se encarga del arranque.

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
========== MENU DE TAREAS ==========
Tareas actuales: 0/10
Tareas pendientes de guardar: 0/10
1. Crear tarea
2. Mostrar tareas
3. Marcar tarea como completada
4. Eliminar tarea
5. Guardar tareas pendientes
6. Salir
====================================
```

### Flujo de ejemplo

1. Selecciona `1` y escribe la descripción de una tarea, por ejemplo `Preparar presentación`.
2. Selecciona `2` para consultar las tareas existentes y sus identificadores.
3. Selecciona `3` e introduce el ID de una tarea para marcarla como completada.
4. Selecciona `4` e introduce el ID de una tarea para eliminarla.
5. Selecciona `5` para guardar las tareas nuevas.
6. Selecciona `6` para salir.

## Persistencia y validaciones

Al iniciar, se cargan las tareas de `tareas_guardadas.txt`. Si no existe, se inicia con una lista vacía. El archivo se busca y guarda en el directorio desde el que se ejecuta la aplicación.

Cada línea contiene el identificador, la descripción y el estado, separados por punto y coma:

```text
1;Estudiar Java;Pendiente
2;Repasar colecciones;Completada
```

La opción 5 guarda toda la lista actual. Completar o eliminar una tarea también guarda la lista. La opción 6 sale sin guardar automáticamente: antes de salir, usá la opción 5 para conservar las tareas nuevas.

Se rechazan descripciones vacías o con punto y coma y se controlan las entradas numéricas. Al cargar el archivo se validan el formato, los estados, los identificadores positivos y no repetidos, las descripciones y el límite de tareas. Si se detectan datos inválidos, se informa el error y no se inicia el menú.

## Lambdas utilizadas

- `forEach(tarea -> idsGuardados.add(tarea.getId()))`: registra los identificadores guardados; la lambda actúa como un `Consumer<Tarea>`.
- `filter(tarea -> !idsGuardados.contains(tarea.getId()))`: selecciona las tareas pendientes de guardar para contarlas; la lambda actúa como un `Predicate<Tarea>`.
- `removeIf(tarea -> tarea.getId() == id)`: elimina la tarea con el identificador indicado mediante un predicado.
- `forEach(tarea -> { ... })`: recorre las tareas para escribirlas en el archivo y comunica los errores de escritura.

## Limitaciones conocidas

- Solo se pueden mantener 10 tareas a la vez.
- La persistencia es local en un archivo de texto; no se utiliza una base de datos.
- La interacción se realiza exclusivamente mediante la consola.
- Los identificadores no se reutilizan después de eliminar una tarea durante la ejecución actual.
- Al cargar el archivo, el siguiente identificador se calcula a partir del mayor ID guardado.

