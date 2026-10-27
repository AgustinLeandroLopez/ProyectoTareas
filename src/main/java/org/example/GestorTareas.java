package org.example;

import java.util.ArrayList;
import java.util.List;

public class GestorTareas {
    private static final int MAX_TAREAS = 10;
    private final List<Tarea> tareas = new ArrayList<>();
    private int contadorId = 1;

    public Tarea crearTarea(String descripcion) {
        if (tareas.size() >= MAX_TAREAS) {
            //Excepcion personalizada para indicar que se ha alcanzado el límite de tareas
            throw new LimiteTareasException("Límite de tareas alcanzado");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            //Excepcion personalizada para indicar que la descripción no puede estar vacía
            throw new DescripcionVaciaException("La descripción no puede estar vacía");
        }
        if (descripcion.contains(";")) {
            throw new IllegalArgumentException("La descripción no puede contener punto y coma (;)");
        }
        Tarea tarea = new Tarea(contadorId, descripcion.trim());
        tareas.add(tarea);
        contadorId++;
        return tarea;
    }

    public void cargarTareas(List<Tarea> tareasGuardadas) {
        if (tareasGuardadas.size() > MAX_TAREAS) {
            throw new LimiteTareasException(
                    "El archivo supera el límite de tareas"
            );
        }
        // Validar todo antes de modificar las tareas en memoria.
        List<Integer> ids = new ArrayList<>();
        for (Tarea tarea : tareasGuardadas) {
            int id = tarea.getId();
            if (id <= 0) {
                throw new IllegalArgumentException("El ID debe ser mayor que cero: " + id);
            }
            if (ids.contains(id)) {
                throw new IllegalArgumentException("ID repetido en el archivo: " + id);
            }
            ids.add(id);

            String descripcion = tarea.getDescripcion();
            if (descripcion == null || descripcion.trim().isEmpty()) {
                throw new DescripcionVaciaException(
                        "La descripción de la tarea con ID " + id + " no puede estar vacía"
                );
            }
        }

        // Reemplazar la lista en memoria por las tareas del archivo.
        tareas.clear();
        tareas.addAll(tareasGuardadas);
        // La siguiente tarea tendrá un ID mayor a los cargados.
        contadorId = tareas.stream()
                .mapToInt(Tarea::getId)
                .max()
                .orElse(0) + 1;
    }

    public List<Tarea> getTareas() {
        return List.copyOf(tareas);
    }

    public int getCantidadTareas() {
        return tareas.size();
    }

    public int getMaxTareas() {
        return MAX_TAREAS;
    }

    public boolean marcarCompletada(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                tarea.marcarComoCompletada();
                return true;
            }
        }
        return false;
    }

    public boolean eliminarTarea(int id) {
        return tareas.removeIf(tarea -> tarea.getId() == id);
    }
}
