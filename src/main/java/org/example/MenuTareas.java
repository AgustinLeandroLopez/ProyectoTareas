package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuTareas {
    private final GestorTareas gestor;
    private final Scanner scanner;
    private final List<Integer> idsGuardados = new ArrayList<>();

    public MenuTareas(GestorTareas gestor, Scanner scanner) {
        this.gestor = gestor;
        this.scanner = scanner;
    }

    public void iniciar() {
        if (!cargarTareasDesdeArchivo()) {
            return;
        }
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = obtenerOpcion();

            switch (opcion) {
                case 1:
                    crearTarea();
                    break;
                case 2:
                    mostrarTareas();
                    break;
                case 3:
                    marcarCompletada();
                    break;
                case 4:
                    eliminarTarea();
                    break;
                case 5:
                    try {
                        guardarTarea();
                    } catch (IOException e) {
                        System.out.println("Error al guardar las tareas: " + e.getMessage());
                    }
                    break;
                case 6:
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.\n");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n========== MENU DE TAREAS ==========");
        System.out.println("Tareas actuales: " + gestor.getCantidadTareas() + "/" + gestor.getMaxTareas());
        // La lambda selecciona las tareas que todavia no se guardaron.
        long pendientesDeGuardar = gestor.getTareas().stream()
                .filter(tarea -> !idsGuardados.contains(tarea.getId()))
                .count();
        long guardadas = gestor.getCantidadTareas() - pendientesDeGuardar;
        System.out.println("Tareas pendientes de guardar: "
                + pendientesDeGuardar + "/" + (gestor.getMaxTareas() - guardadas));
        System.out.println("1. Crear tarea");
        System.out.println("2. Mostrar tareas");
        System.out.println("3. Marcar tarea como completada");
        System.out.println("4. Eliminar tarea");
        System.out.println("5. Guardar tareas pendientes");
        System.out.println("6. Salir");
        System.out.println("====================================");
    }

    private int obtenerOpcion() {
        System.out.print("Selecciona una opción: ");
        int opcion;
        try {
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea en blanco
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar el buffer
            System.out.println("Error: Debes ingresar un número.\n");
            return -1;
        }
        return opcion;
    }

    private void crearTarea() {
        if (gestor.getCantidadTareas() >= gestor.getMaxTareas()) {
            System.out.println("\n No puedes crear más tareas. Límite de " + gestor.getMaxTareas() + " alcanzado.");
            System.out.println("Elimina algunas tareas para crear nuevas.\n");
            return;
        }

        System.out.print("\nIngresa la descripción de la tarea: ");
        String descripcion = scanner.nextLine().trim();

        try {
            Tarea nuevaTarea = gestor.crearTarea(descripcion);
            System.out.println("Tarea creada exitosamente con ID: " + nuevaTarea.getId() + "\n");
        } catch (DescripcionVaciaException | LimiteTareasException | IllegalArgumentException e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private void mostrarTareas() {
        if (gestor.getCantidadTareas() == 0) {
            System.out.println("\nNo hay tareas registradas.\n");
            return;
        }

        System.out.println("\n========== TUS TAREAS ==========");
        for (Tarea tarea : gestor.getTareas()) {
            String estado = tarea.isCompletada() ? "Completada" : "Pendiente";
            String estadoGuardado = idsGuardados.contains(tarea.getId())
                    ? "Tarea guardada" : "Tarea sin guardar";
            System.out.println("[ID: " + tarea.getId() + "] " + tarea.getDescripcion()
                    + " - " + estado + " - " + estadoGuardado);
        }
        System.out.println("================================\n");
    }

    private void marcarCompletada() {
        if (gestor.getCantidadTareas() == 0) {
            System.out.println("\nNo hay tareas para marcar como completada.\n");
            return;
        }

        mostrarTareas();
        System.out.print("Ingresa el ID de la tarea a marcar como completada: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea en blanco

            if (gestor.marcarCompletada(id)) {
                idsGuardados.remove(Integer.valueOf(id));
                System.out.println("Tarea marcada como completada.\n");
                guardarTarea();
            } else {
                System.out.println("Tarea con ID " + id + " no encontrada.\n");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Error: Debes ingresar un número válido.\n");
        } catch (IOException e) {
            System.out.println("El cambio se realizó en memoria, pero no se pudo guardar en el archivo: " + e.getMessage());
        }
    }

    private void eliminarTarea() {
        if (gestor.getCantidadTareas() == 0) {
            System.out.println("\nNo hay tareas para eliminar.\n");
            return;
        }

        mostrarTareas();
        System.out.print("Ingresa el ID de la tarea a eliminar: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea en blanco

            if (gestor.eliminarTarea(id)) {
                idsGuardados.remove(Integer.valueOf(id));
                System.out.println("Tarea eliminada exitosamente. Espacio liberado.\n");
                guardarTarea();
            } else {
                System.out.println("Tarea con ID " + id + " no encontrada.\n");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Error: Debes ingresar un número válido.\n");
        } catch (IOException e) {
            System.out.println("El cambio se realizó en memoria, pero no se pudo guardar en el archivo: " + e.getMessage());
        }
    }
    private void guardarTarea()  throws IOException {
        // Guardar las tareas en un archivo de texto.
        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter("tareas_guardadas.txt", StandardCharsets.UTF_8))) {

            gestor.getTareas().forEach(tarea -> {

                String estado = tarea.isCompletada()
                        ? "Completada"
                        : "Pendiente";

                try {
                    escritor.write(
                            tarea.getId() + ";"
                                    + tarea.getDescripcion() + ";"
                                    + estado
                    );

                    escritor.newLine();

                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

        } catch (UncheckedIOException e) {
            throw e.getCause();
        }

        actualizarIdsGuardados();
        System.out.println("\nTareas guardadas exitosamente.\n");
    }

    private void actualizarIdsGuardados() {
        idsGuardados.clear();
        gestor.getTareas().forEach(tarea -> idsGuardados.add(tarea.getId()));
    }

    private boolean cargarTareasDesdeArchivo() {
        File archivo = new File("tareas_guardadas.txt");
        List<Tarea> tareasGuardadas = new ArrayList<>();

        // Verificar si existe el archivo
        if (!archivo.exists()) {
            System.out.println("No hay archivo previo. Se inicia sin tareas.");
            return true;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            // Recorrer las líneas del archivo
            String linea;
            while ((linea = lector.readLine()) != null) {

                // Separar los datos de cada tarea
                String[] datos = linea.split(";", -1);

                // Verificar que tenga los 3 campos
                if (datos.length != 3) {
                    System.out.println("Formato incorrecto: " + linea);
                    return false;
                }

                // Recuperar los datos
                int id = Integer.parseInt(datos[0]);
                String descripcion = datos[1];
                String estado = datos[2];

                // Validar el estado
                if (!estado.equals("Completada")
                        && !estado.equals("Pendiente")) {
                    System.out.println("Estado desconocido: " + estado);
                    return false;
                }

                // Crear la tarea
                Tarea tarea = new Tarea(id, descripcion);

                // Recuperar si estaba completada
                tarea.setCompletada(estado.equals("Completada"));

                // Agregar la tarea a la lista
                tareasGuardadas.add(tarea);
            }

            // Cargar todas las tareas en el gestor
            gestor.cargarTareas(tareasGuardadas);
            actualizarIdsGuardados();

            System.out.println("Tareas recuperadas: " + tareasGuardadas.size());

            return true;

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return false;

        } catch (IllegalArgumentException | DescripcionVaciaException e) {
            System.out.println("Error en los datos del archivo: " + e.getMessage());
            return false;

        } catch (LimiteTareasException e) {
            System.out.println("Error al cargar tareas: " + e.getMessage());
            return false;
        }
    }
}
