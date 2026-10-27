package org.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Pruebas ejecutables con java org.example.AplicacionTareasTest. */
public class AplicacionTareasTest {
    public static void main(String[] args) {
        crearYValidarTareas();
        respetarLimiteEIdentificadores();
        completarYEliminarPorId();
        protegerColeccion();
        ejecutarMenu();
        validarEntradasDelMenu();
        System.out.println("6 pruebas superadas.");
    }

    private static void crearYValidarTareas() {
        GestorTareas gestor = new GestorTareas();
        comprobar(gestor.getCantidadTareas() == 0, "El gestor comienza vacío");
        esperarExcepcion(DescripcionVaciaException.class, () -> gestor.crearTarea("   "));
        esperarExcepcion(DescripcionVaciaException.class, () -> gestor.crearTarea(null));
        Tarea tarea = gestor.crearTarea("  Estudiar Java  ");
        comprobar(tarea.getId() == 1, "Una entrada inválida no consume ID");
        comprobar(tarea.getDescripcion().equals("Estudiar Java"), "Recortar descripción");
        comprobar(!tarea.isCompletada(), "La tarea comienza pendiente");
    }

    private static void respetarLimiteEIdentificadores() {
        GestorTareas gestor = new GestorTareas();
        comprobar(gestor.getMaxTareas() == 10, "Se mantiene el límite de cinco tareas");
        for (int i = 0; i < 10; i++) {
            gestor.crearTarea("Tarea " + i);
        }
        esperarExcepcion(LimiteTareasException.class, () -> gestor.crearTarea("Sexta"));
        comprobar(gestor.getCantidadTareas() == 10, "No superar el límite");
        gestor.eliminarTarea(2);
        comprobar(gestor.crearTarea("Nueva").getId() == 6, "No reutilizar IDs eliminados");
        comprobar(new GestorTareas().getCantidadTareas() == 0, "Gestores independientes");
    }

    private static void completarYEliminarPorId() {
        GestorTareas gestor = new GestorTareas();
        comprobar(!gestor.marcarCompletada(1), "Completar en lista vacía");
        comprobar(!gestor.eliminarTarea(1), "Eliminar en lista vacía");
        Tarea tarea = gestor.crearTarea("Estudiar");
        comprobar(!gestor.marcarCompletada(99), "ID inexistente al completar");
        comprobar(gestor.marcarCompletada(tarea.getId()), "Completar por ID");
        comprobar(tarea.isCompletada(), "Actualizar el estado");
        comprobar(!gestor.eliminarTarea(99), "ID inexistente al eliminar");
        comprobar(gestor.eliminarTarea(tarea.getId()), "Eliminar por ID");
        comprobar(gestor.getCantidadTareas() == 0, "Liberar espacio");
    }

    private static void protegerColeccion() {
        GestorTareas gestor = new GestorTareas();
        gestor.crearTarea("Primera");
        esperarExcepcion(UnsupportedOperationException.class, () -> gestor.getTareas().clear());
        comprobar(gestor.getCantidadTareas() == 1, "No modificar la colección desde fuera");
    }

    private static void ejecutarMenu() {
        GestorTareas gestor = new GestorTareas();
        String salida = ejecutar(gestor, "2\n1\nEstudiar\n2\n3\n1\n2\n4\n1\n5\n6\n");
        comprobar(salida.contains("No hay tareas registradas"), "Mostrar lista vacía");
        comprobar(salida.contains("ID: 1"), "Mostrar identificador");
        comprobar(salida.contains("Pendiente"), "Mostrar estado pendiente");
        comprobar(salida.contains("Completada"), "Mostrar estado completado");
        comprobar(salida.contains("Espacio liberado"), "Confirmar eliminación");
        comprobar(salida.contains("Guardar tarea"), "Confirmar guardado");
        comprobar(salida.contains("¡Hasta luego!"), "Salir del menú");
        comprobar(gestor.getCantidadTareas() == 0, "El menú delega en el gestor");
    }

    private static void validarEntradasDelMenu() {
        GestorTareas gestor = new GestorTareas();
        String salida = ejecutar(gestor, "texto\n9\n3\n4\n1\n  \n1\nLeer\n3\nx\n4\nx\n3\n99\n4\n99\n5\n");
        comprobar(salida.contains("Debes ingresar un número"), "Rechazar texto como opción");
        comprobar(salida.contains("Opción no válida"), "Rechazar opción desconocida");
        comprobar(salida.contains("La descripción no puede estar vacía"), "Validar descripción");
        comprobar(salida.contains("número válido"), "Validar ID numérico");
        comprobar(salida.contains("ID 99 no encontrada"), "Informar ID inexistente");
        comprobar(salida.contains("No hay tareas para marcar"), "Completar sin tareas");
        comprobar(salida.contains("No hay tareas para eliminar"), "Eliminar sin tareas");
        comprobar(gestor.getCantidadTareas() == 1, "Entradas inválidas no alteran tareas");
        salida = ejecutar(new GestorTareas(), "1\na\n1\nb\n1\nc\n1\nd\n1\ne\n1\n5\n");
        comprobar(salida.contains("Límite de 10 alcanzado"), "Informar límite en menú");
    }

    private static String ejecutar(GestorTareas gestor, String entrada) {
        PrintStream original = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try (Scanner scanner = new Scanner(entrada);
             PrintStream consola = new PrintStream(salida, true, StandardCharsets.UTF_8)) {
            System.setOut(consola);
            new MenuTareas(gestor, scanner).iniciar();
        } finally {
            System.setOut(original);
        }
        return salida.toString(StandardCharsets.UTF_8);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void esperarExcepcion(Class<? extends RuntimeException> tipo, Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException e) {
            comprobar(tipo.isInstance(e), "Tipo de excepción inesperado: " + e);
            return;
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName());
    }
}
