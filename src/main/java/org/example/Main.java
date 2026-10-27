package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestorTareas gestor = new GestorTareas();
        try (Scanner scanner = new Scanner(System.in)) {
            MenuTareas menu = new MenuTareas(gestor, scanner);
            menu.iniciar();
        }
    }
}