package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final int MAX_TAREAS = 5;
    private static ArrayList<Tarea> tareas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorId = 1;

    public static void main(String[] args) {
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
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.\n");
            }
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========== MENÚ DE TAREAS ==========");
        System.out.println("Tareas actuales: " + tareas.size() + "/" + MAX_TAREAS);
        System.out.println("1. Crear tarea");
        System.out.println("2. Mostrar tareas");
        System.out.println("3. Marcar tarea como completada");
        System.out.println("4. Eliminar tarea");
        System.out.println("5. Salir");
        System.out.println("====================================");
    }

    private static int obtenerOpcion() {
        System.out.print("Selecciona una opción: ");
        int opcion = -1;
        try {
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea en blanco
        } catch (Exception e) {
            scanner.nextLine(); // Limpiar el buffer
            System.out.println("Error: Debes ingresar un número.\n");
            return -1;
        }
        return opcion;
    }

    private static void crearTarea() {
        if (tareas.size() >= MAX_TAREAS) {
            System.out.println("\n No puedes crear más tareas. Límite de " + MAX_TAREAS + " alcanzado.");
            System.out.println("Elimina algunas tareas para crear nuevas.\n");
            return;
        }

        System.out.print("\nIngresa la descripción de la tarea: ");
        String descripcion = scanner.nextLine().trim();

        if (descripcion.isEmpty()) {
            System.out.println("La descripción no puede estar vacía.\n");
            return;
        }

        Tarea nuevaTarea = new Tarea(contadorId, descripcion);
        tareas.add(nuevaTarea);
        System.out.println("Tarea creada exitosamente con ID: " + contadorId + "\n");
        contadorId++;
    }

    private static void mostrarTareas() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas registradas.\n");
            return;
        }

        System.out.println("\n========== TUS TAREAS ==========");
        for (Tarea tarea : tareas) {
            String estado = tarea.isCompletada() ? "Completada" : "Pendiente";
            System.out.println("[ID: " + tarea.getId() + "] " + tarea.getDescripcion() + " - " + estado);
        }
        System.out.println("================================\n");
    }

    private static void marcarCompletada() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas para marcar como completada.\n");
            return;
        }

        mostrarTareas();
        System.out.print("Ingresa el ID de la tarea a marcar como completada: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea en blanco

            boolean encontrada = false;
            for (Tarea tarea : tareas) {
                if (tarea.getId() == id) {
                    tarea.marcarComoCompletada();
                    System.out.println("Tarea marcada como completada.\n");
                    encontrada = true;
                    break;
                }
            }

            if (!encontrada) {
                System.out.println("Tarea con ID " + id + " no encontrada.\n");
            }
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Error: Debes ingresar un número válido.\n");
        }
    }

    private static void eliminarTarea() {
        if (tareas.isEmpty()) {
            System.out.println("\nNo hay tareas para eliminar.\n");
            return;
        }

        mostrarTareas();
        System.out.print("Ingresa el ID de la tarea a eliminar: ");

        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea en blanco

            boolean eliminada = false;
            for (int i = 0; i < tareas.size(); i++) {
                if (tareas.get(i).getId() == id) {
                    tareas.remove(i);
                    System.out.println("Tarea eliminada exitosamente. Espacio liberado.\n");
                    eliminada = true;
                    break;
                }
            }

            if (!eliminada) {
                System.out.println("Tarea con ID " + id + " no encontrada.\n");
            }
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Error: Debes ingresar un número válido.\n");
        }
    }
}