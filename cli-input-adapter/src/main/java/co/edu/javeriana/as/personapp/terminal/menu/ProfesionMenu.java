package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

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

    public void iniciarMenu(ProfesionInputAdapterCli cli, Scanner kb) {
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
                        cli.setProfessionOutputPortInjection(DATABASE);
                        menuOpciones(cli, kb);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        cli.setProfessionOutputPortInjection(DATABASE);
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

    private void menuOpciones(ProfesionInputAdapterCli cli, Scanner kb) {
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
                        cli.crearProfesion(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        cli.editarProfesion(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        cli.buscarProfesion(DATABASE, leerIdentificacion(kb));
                        break;
                    case OPCION_ELIMINAR:
                        cli.eliminarProfesion(DATABASE, leerIdentificacion(kb));
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
        System.out.println("|        M E N Ú   D E   P R O F E S I O N E S       |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("| 1 |  Ver todas las profesiones                     |");
        System.out.println("| 2 |  Crear profesión                               |");
        System.out.println("| 3 |  Actualizar profesión                          |");
        System.out.println("| 4 |  Buscar profesión                              |");
        System.out.println("| 5 |  Eliminar profesión                            |");
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
            System.out.print("ID profesión ▶ ");
            return kb.nextInt();
        } catch (InputMismatchException e) {
            kb.nextLine();
            System.out.println("ID inválido, intente de nuevo.");
            return leerIdentificacion(kb);
        }
    }

    public ProfesionModelCli leerEntidad(Scanner kb) {
        try {
            ProfesionModelCli p = new ProfesionModelCli();
            System.out.print("ID profesión ▶ ");
            p.setId(kb.nextInt());
            kb.nextLine();
            System.out.print("Nombre        ▶ ");
            p.setName(kb.nextLine());
            System.out.print("Descripción   ▶ ");
            p.setDescription(kb.nextLine());
            return p;
        } catch (InputMismatchException e) {
            System.out.println("Datos inválidos, intente nuevamente.");
            kb.nextLine();
            return leerEntidad(kb);
        }
    }
}
