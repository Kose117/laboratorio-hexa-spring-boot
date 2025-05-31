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
    private static final int PERSISTENCIA_MARIADB    = 1;
    private static final int PERSISTENCIA_MONGODB    = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO   = 1;
    private static final int OPCION_CREAR      = 2;
    private static final int OPCION_ACTUALIZAR = 3;
    private static final int OPCION_BUSCAR     = 4;
    private static final int OPCION_ELIMINAR   = 5;

    /* ═══════════════════════════  FLUJO PRINCIPAL  ═══════════════════════════ */

    public void iniciarMenu(PersonaInputAdapterCli cli, Scanner kb) {
        boolean salir = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(kb);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        salir = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        DATABASE = "MARIA";
                        cli.setPersonOutputPortInjection(DATABASE);
                        menuOpciones(cli, kb);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        cli.setPersonOutputPortInjection(DATABASE);
                        menuOpciones(cli, kb);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!salir);
    }

    private void menuOpciones(PersonaInputAdapterCli cli, Scanner kb) {
        boolean salir = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(kb);
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        salir = true;
                        break;
                    case OPCION_VER_TODO:
                        cli.historial();
                        break;
                    case OPCION_CREAR:
                        cli.crearPersona(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        cli.editarPersona(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        cli.buscarPersona(DATABASE, leerIdentificacion(kb));
                        break;
                    case OPCION_ELIMINAR:
                        cli.eliminarPersona(DATABASE, leerIdentificacion(kb));
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!salir);
    }

    /* ═══════════════════════════  MENÚ EN ASCII BOX  ═════════════════════════ */

    private void mostrarMenuOpciones() {
        System.out.println("+----------------------------------------------------+");
        System.out.println("|            M E N Ú   D E   P E R S O N A S         |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("| 1 |  Ver todas las personas                        |");
        System.out.println("| 2 |  Crear persona                                 |");
        System.out.println("| 3 |  Actualizar persona                            |");
        System.out.println("| 4 |  Buscar persona                                |");
        System.out.println("| 5 |  Eliminar persona                              |");
        System.out.println("| 0 |  Regresar                                      |");
        System.out.println("+----------------------------------------------------+");
        System.out.print ("Seleccione opción ▶ ");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("+--------------------------------+");
        System.out.println("|       M O T O R   D E   B D    |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1 |  MariaDB                   |");
        System.out.println("| 2 |  MongoDB                   |");
        System.out.println("| 0 |  Regresar                  |");
        System.out.println("+--------------------------------+");
        System.out.print ("Seleccione opción ▶ ");
    }

    /* ═══════════════════════════  UTILIDADES DE LECTURA  ═════════════════════ */

    private int leerOpcion(Scanner kb) {
        try {
            return kb.nextInt();
        } catch (InputMismatchException e) {
            kb.nextLine();
            System.out.println("Por favor ingrese un número válido.");
            return leerOpcion(kb);
        }
    }

    private int leerIdentificacion(Scanner kb) {
        try {
            System.out.print("ID persona ▶ ");
            return kb.nextInt();
        } catch (InputMismatchException e) {
            kb.nextLine();
            System.out.println("ID inválido, intente de nuevo.");
            return leerIdentificacion(kb);
        }
    }

    public PersonaModelCli leerEntidad(Scanner kb) {
        try {
            PersonaModelCli p = new PersonaModelCli();
            System.out.print("ID persona ▶ ");
            p.setCc(kb.nextInt());
            kb.nextLine();
            System.out.print("Nombre      ▶ ");
            p.setNombre(kb.nextLine());
            System.out.print("Apellido    ▶ ");
            p.setApellido(kb.nextLine());
            System.out.print("Género (M/F)▶ ");
            p.setGenero(kb.nextLine());
            System.out.print("Edad        ▶ ");
            p.setEdad(kb.nextInt());
            kb.nextLine();
            return p;
        } catch (InputMismatchException e) {
            System.out.println("Datos inválidos, intente nuevamente.");
            kb.nextLine();
            return leerEntidad(kb);
        }
    }
}
