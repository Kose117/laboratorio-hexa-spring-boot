package co.edu.javeriana.as.personapp.terminal.menu;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudiosInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class EstudiosMenu {

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

    public void iniciarMenu(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        log.info("Regresando a los módulos.");
                        break;
                    case PERSISTENCIA_MARIADB:
                        DATABASE = "MARIA";
                        estudiosInputAdapterCli.setStudyOutputPortInjection("MARIA");
                        log.info("Seleccionado motor de persistencia: MariaDB.");
                        menuOpciones(estudiosInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        estudiosInputAdapterCli.setStudyOutputPortInjection("MONGO");
                        log.info("Seleccionado motor de persistencia: MongoDB.");
                        menuOpciones(estudiosInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida: " + opcion);
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        log.info("Regresando al menú de motores de persistencia.");
                        break;
                    case OPCION_VER_TODO:
                        log.info("Visualizando todos los estudios.");
                        estudiosInputAdapterCli.historial();
                        break;
                    case OPCION_CREAR:
                        log.info("Creando un nuevo estudio.");
                        estudiosInputAdapterCli.crearEstudios(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        log.info("Actualizando un estudio existente.");
                        estudiosInputAdapterCli.editarEstudio(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        log.info("Buscando un estudio.");
                        buscarEstudio(estudiosInputAdapterCli, keyboard);
                        break;
                    case OPCION_ELIMINAR:
                        log.info("Eliminando un estudio.");
                        estudiosInputAdapterCli.eliminarEstudio(DATABASE, leerIdProfesion(keyboard), leerIdPersona(keyboard));
                        break;
                    default:
                        log.warn("La opción elegida no es válida: " + opcion);
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números. Error: " + e.getMessage());
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println("MENÚ DE ESTUDIOS");
        System.out.println(OPCION_VER_TODO + " - Get All Estudios");
        System.out.println(OPCION_CREAR + " - Create Estudio");
        System.out.println(OPCION_ACTUALIZAR + " - Update Estudio");
        System.out.println(OPCION_BUSCAR + " - Find Estudio");
        System.out.println(OPCION_ELIMINAR + " - Delete Estudio");
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

    public EstudiosModelCli leerEntidad(Scanner keyboard) {
        try {
            EstudiosModelCli estudiosModelCli = new EstudiosModelCli();
            keyboard.nextLine();
            System.out.println("Ingrese la identificación de la persona:");
            estudiosModelCli.setIdPerson(keyboard.nextLine());
            System.out.println("Ingrese la identificación de la profesión:");
            estudiosModelCli.setIdProfession(keyboard.nextLine());
            System.out.println("Ingrese el nombre de la universidad:");
            estudiosModelCli.setUniversityName(keyboard.nextLine());
            estudiosModelCli.setGraduationDate(leerFecha(keyboard));
            return estudiosModelCli;
        } catch (Exception e) {
            System.out.println("Datos incorrectos, ingrese los datos nuevamente.");
            return leerEntidad(keyboard);
        }
    }

    public LocalDate leerFecha(Scanner keyboard) {
        try {
            System.out.println("Ingrese la fecha de graduación (dd/mm/yyyy):");
            String fecha = keyboard.nextLine();
            String[] fechaArray = fecha.split("/");
            return LocalDate.of(Integer.parseInt(fechaArray[2]), Integer.parseInt(fechaArray[1]), Integer.parseInt(fechaArray[0]));
        } catch (Exception e) {
            System.out.println("Fecha no válida, ingrese nuevamente.");
            return leerFecha(keyboard);
        }
    }

    private void buscarEstudio(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        Integer idProfesion = leerIdProfesion(keyboard);
        Integer idPersona = leerIdPersona(keyboard);
        estudiosInputAdapterCli.buscarEstudio(DATABASE, idProfesion, idPersona);
    }
    
    private Integer leerIdProfesion(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("El ID de la profesión debe ser un número.");
            keyboard.nextLine(); // Limpiar el buffer del teclado
            return leerIdProfesion(keyboard);
        }
    }
    
    private Integer leerIdPersona(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la persona: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("El ID de la persona debe ser un número.");
            keyboard.nextLine(); // Limpiar el buffer del teclado
            return leerIdPersona(keyboard);
        }
    }
}
