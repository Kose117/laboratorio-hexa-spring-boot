package co.edu.javeriana.as.personapp.terminal.menu;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class TelefonoMenu {

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

    public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
                        telefonoInputAdapterCli.setPhoneOutputPortInjection(DATABASE);
                        menuOpciones(telefonoInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        telefonoInputAdapterCli.setPhoneOutputPortInjection(DATABASE);
                        menuOpciones(telefonoInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
                        log.info("Visualizando todos los teléfonos.");
                        telefonoInputAdapterCli.historial();
                        break;
                    case OPCION_CREAR:
                        log.info("Creando un nuevo teléfono.");
                        telefonoInputAdapterCli.crearTelefono(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        log.info("Actualizando un teléfono existente.");
                        telefonoInputAdapterCli.editarTelefono(leerEntidad(keyboard), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        log.info("Buscando un teléfono.");
                        telefonoInputAdapterCli.buscarTelefono(DATABASE, leerNumero(keyboard));
                        break;
                    case OPCION_ELIMINAR:
                        log.info("Eliminando un teléfono.");
                        telefonoInputAdapterCli.eliminarTelefono(DATABASE, leerNumero(keyboard));
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
        System.out.println("MENÚ DE TELÉFONOS");
        System.out.println(OPCION_VER_TODO + " - Ver todos los teléfonos");
        System.out.println(OPCION_CREAR + " - Crear un teléfono");
        System.out.println(OPCION_ACTUALIZAR + " - Actualizar un teléfono");
        System.out.println(OPCION_BUSCAR + " - Buscar un teléfono");
        System.out.println(OPCION_ELIMINAR + " - Eliminar un teléfono");
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

    private String leerNumero(Scanner keyboard) {
        try {
            System.out.print("Ingrese el número: ");
            return keyboard.nextLine();
        } catch (Exception e) {
            log.warn("Error al ingresar el número.");
            return leerNumero(keyboard);
        }
    }

    public TelefonoModelCli leerEntidad(Scanner keyboard) {
        TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
        
        // Leer número de teléfono
        System.out.print("Ingrese el número: ");
        String numero = keyboard.nextLine().trim(); // Leer el número y eliminar espacios
        if (numero.isEmpty()) {
            System.out.println("El número no puede estar vacío. Intente de nuevo.");
            return leerEntidad(keyboard); // Vuelve a pedir la entrada si está vacío
        }
        telefonoModelCli.setNumber(numero);
    
        // Leer compañía
        System.out.print("Ingrese la compañía: ");
        String compania = keyboard.nextLine().trim(); // Leer la compañía y eliminar espacios
        if (compania.isEmpty()) {
            System.out.println("La compañía no puede estar vacía. Intente de nuevo.");
            return leerEntidad(keyboard); // Vuelve a pedir la entrada si está vacío
        }
        telefonoModelCli.setCompany(compania);
    
        // Leer ID de persona
        System.out.print("Ingrese el ID de la persona: ");
        String idPersona = keyboard.nextLine().trim();
        if (idPersona.isEmpty()) {
            System.out.println("El ID de la persona no puede estar vacío. Intente de nuevo.");
            return leerEntidad(keyboard); // Vuelve a pedir la entrada si está vacío
        }
        telefonoModelCli.setIdPerson(idPersona);
    
        return telefonoModelCli;
    }
}
