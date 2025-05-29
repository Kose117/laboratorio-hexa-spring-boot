package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

    private static String DATABASE = "MARIA";
    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR = 2;
    private static final int OPCION_ACTUALIZAR = 3;
    private static final int OPCION_BUSCAR = 4;
    private static final int OPCION_ELIMINAR = 5;

    public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        DATABASE = "MARIA";
                        personaInputAdapterCli.setPersonOutputPortInjection(DATABASE);
                        menuOpciones(personaInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        personaInputAdapterCli.setPersonOutputPortInjection(DATABASE);
                        menuOpciones(personaInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        break;
                    case OPCION_VER_TODO:
                        log.info("Visualizando todas las personas.");
                        personaInputAdapterCli.historial();
                        break;
                    case OPCION_CREAR:
                        log.info("Creando una nueva persona.");
                        personaInputAdapterCli.crearPersona(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        log.info("Actualizando una persona existente.");
                        personaInputAdapterCli.editarPersona(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        log.info("Buscando una persona.");
                        personaInputAdapterCli.buscarPersona(DATABASE, leerIdentificacion(keyboard));
                        break;
                    case OPCION_ELIMINAR:
                        log.info("Eliminando una persona.");
                        personaInputAdapterCli.eliminarPersona(DATABASE, leerIdentificacion(keyboard));
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println("MENÚ DE PERSONAS");
        System.out.println(OPCION_VER_TODO + " - Get All Personas");
        System.out.println(OPCION_CREAR + " - Create Persona");
        System.out.println(OPCION_ACTUALIZAR + " - Update Persona");
        System.out.println(OPCION_BUSCAR + " - Find Persona");
        System.out.println(OPCION_ELIMINAR + " - Delete Persona");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " - Regresar");
        System.out.println("----------------------");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println("SELECCIONAR MOTOR DE PERSISTENCIA");
        System.out.println(PERSISTENCIA_MARIADB + " - MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " - MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " - Regresar");
        System.out.println("----------------------");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.nextLine(); // Limpiar el buffer
            return leerOpcion(keyboard);
        }
    }

    private int leerIdentificacion(Scanner keyboard) {
        try {
            System.out.print("Ingrese la identificación: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.nextLine(); // Limpiar el buffer
            return leerIdentificacion(keyboard);
        }
    }

    public PersonaModelCli leerEntidad(Scanner keyboard) {
        try {
            PersonaModelCli personaModelCli = new PersonaModelCli();
            System.out.print("Ingrese la identificación: ");
            personaModelCli.setCc(keyboard.nextInt());
            keyboard.nextLine();
            System.out.print("Ingrese el nombre: ");
            personaModelCli.setNombre(keyboard.nextLine());
            System.out.print("Ingrese el apellido: ");
            personaModelCli.setApellido(keyboard.nextLine());
            System.out.println("Ingrese el género (M/F): ");
            personaModelCli.setGenero(keyboard.nextLine());
            System.out.println("Ingrese la edad: ");
            personaModelCli.setEdad(keyboard.nextInt());
            keyboard.nextLine();
            return personaModelCli;
        } catch (InputMismatchException e) {
            System.out.println("Datos incorrectos, ingrese los datos nuevamente.");
            keyboard.nextLine(); // Limpiar el buffer
            return leerEntidad(keyboard);
        }
    }
}
