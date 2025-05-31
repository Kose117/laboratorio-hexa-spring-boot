package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

    private static String DATABASE = "MARIA";

    private static final int OPCION_REGRESAR_MODULOS            = 0;
    private static final int PERSISTENCIA_MARIADB               = 1;
    private static final int PERSISTENCIA_MONGODB               = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO                    = 1;
    private static final int OPCION_CREAR                       = 2;
    private static final int OPCION_ACTUALIZAR                  = 3;
    private static final int OPCION_BUSCAR                      = 4;
    private static final int OPCION_ELIMINAR                    = 5;

    /* ═══════════════════════════  FLUJO PRINCIPAL  ═══════════════════════════ */

    public void iniciarMenu(TelefonoInputAdapterCli cli, Scanner kb) {
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
                        cli.setPhoneOutputPortInjection(DATABASE);
                        menuOpciones(cli, kb);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        cli.setPhoneOutputPortInjection(DATABASE);
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

    private void menuOpciones(TelefonoInputAdapterCli cli, Scanner kb) {
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
                        cli.crearTelefono(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        cli.editarTelefono(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        cli.buscarTelefono(DATABASE, leerNumero(kb));
                        break;
                    case OPCION_ELIMINAR:
                        cli.eliminarTelefono(DATABASE, leerNumero(kb));
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
        System.out.println("|        M E N Ú   D E   T E L É F O N O S          |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("| 1 |  Ver todos los teléfonos                       |");
        System.out.println("| 2 |  Crear teléfono                                |");
        System.out.println("| 3 |  Actualizar teléfono                           |");
        System.out.println("| 4 |  Buscar teléfono                               |");
        System.out.println("| 5 |  Eliminar teléfono                             |");
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

    private String leerNumero(Scanner kb) {
        kb.nextLine();
        System.out.print("Número ▶ ");
        String n = kb.nextLine().trim();
        if (n.isEmpty()) {
            System.out.println("El número no puede estar vacío.");
            return leerNumero(kb);
        }
        return n;
    }

    public TelefonoModelCli leerEntidad(Scanner kb) {
        kb.nextLine();
        TelefonoModelCli t = new TelefonoModelCli();
        System.out.print("Número ▶ ");
        String num = kb.nextLine().trim();
        if (num.isEmpty()) return leerEntidad(kb);
        t.setNumber(num);

        System.out.print("Compañía ▶ ");
        String comp = kb.nextLine().trim();
        if (comp.isEmpty()) return leerEntidad(kb);
        t.setCompany(comp);

        System.out.print("ID persona ▶ ");
        String id = kb.nextLine().trim();
        if (id.isEmpty()) return leerEntidad(kb);
        t.setIdPerson(id);

        return t;
    }
}
