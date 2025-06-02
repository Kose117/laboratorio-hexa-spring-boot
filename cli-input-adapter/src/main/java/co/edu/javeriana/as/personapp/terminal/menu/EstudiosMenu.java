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
    private static final int PERSISTENCIA_MARIADB    = 1;
    private static final int PERSISTENCIA_MONGODB    = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO   = 1;
    private static final int OPCION_CREAR      = 2;
    private static final int OPCION_ACTUALIZAR = 3;
    private static final int OPCION_BUSCAR     = 4;
    private static final int OPCION_ELIMINAR   = 5;

    /* ═════════════════════════════  FLUJO PRINCIPAL  ═════════════════════════════ */

    public void iniciarMenu(EstudiosInputAdapterCli cli, Scanner kb) {
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
                        cli.setStudyOutputPortInjection(DATABASE);
                        menuOpciones(cli, kb);
                        break;
                    case PERSISTENCIA_MONGODB:
                        DATABASE = "MONGO";
                        cli.setStudyOutputPortInjection(DATABASE);
                        menuOpciones(cli, kb);
                        break;
                    default:
                        log.warn("La opción elegida no es válida: " + opcion);
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!salir);
    }

    private void menuOpciones(EstudiosInputAdapterCli cli, Scanner kb) {
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
                        cli.crearEstudios(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_ACTUALIZAR:
                        cli.editarEstudio(leerEntidad(kb), DATABASE);
                        break;
                    case OPCION_BUSCAR:
                        buscarEstudio(cli, kb);
                        break;
                    case OPCION_ELIMINAR:
                        cli.eliminarEstudio(DATABASE, leerIdProfesion(kb), leerIdPersona(kb));
                        break;
                    default:
                        log.warn("La opción elegida no es válida: " + opcion);
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!salir);
    }

    /* ═════════════════════════════  MENÚS EN CAJA ASCII  ═════════════════════════════ */

    private void mostrarMenuOpciones() {
        System.out.println("+----------------------------------------------------+");
        System.out.println("|            M E N U   D E   E S T U D I O S         |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("| 1 |  Ver todos los estudios                        |");
        System.out.println("| 2 |  Crear estudio                                 |");
        System.out.println("| 3 |  Actualizar estudio                            |");
        System.out.println("| 4 |  Buscar estudio                                |");
        System.out.println("| 5 |  Eliminar estudio                              |");
        System.out.println("| 0 |  Regresar                                      |");
        System.out.println("+----------------------------------------------------+");
        System.out.print ("Seleccione opción ▶ ");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("+--------------------------------+");
        System.out.println("|        M O T O R   D B         |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1 |  MariaDB                   |");
        System.out.println("| 2 |  MongoDB                   |");
        System.out.println("| 0 |  Regresar                  |");
        System.out.println("+--------------------------------+");
        System.out.print ("Seleccione opción ▶ ");
    }

    /* ═════════════════════════════  LECTURA DE DATOS  ═════════════════════════════ */

    private int leerOpcion(Scanner kb) {
        try {
            return kb.nextInt();
        } catch (InputMismatchException e) {
            kb.nextLine();
            System.out.println("Por favor ingrese un número válido.");
            return leerOpcion(kb);
        }
    }

    public EstudiosModelCli leerEntidad(Scanner kb) {
        try {
            EstudiosModelCli m = new EstudiosModelCli();
            kb.nextLine();
            System.out.print("ID persona        ▶ ");
            m.setIdPerson(kb.nextLine());
            System.out.print("ID profesión      ▶ ");
            m.setIdProfession(kb.nextLine());
            System.out.print("Universidad       ▶ ");
            m.setUniversityName(kb.nextLine());
            m.setGraduationDate(leerFecha(kb));
            return m;
        } catch (Exception e) {
            System.out.println("Datos incorrectos, intente de nuevo.");
            return leerEntidad(kb);
        }
    }

    private LocalDate leerFecha(Scanner kb) {
        try {
            System.out.print("Fecha (dd/mm/yyyy)▶ ");
            String[] f = kb.nextLine().split("/");
            return LocalDate.of(Integer.parseInt(f[2]), Integer.parseInt(f[1]), Integer.parseInt(f[0]));
        } catch (Exception e) {
            System.out.println("Formato inválido, intente de nuevo.");
            return leerFecha(kb);
        }
    }

    private void buscarEstudio(EstudiosInputAdapterCli cli, Scanner kb) {
        int idProf = leerIdProfesion(kb);
        int idPers = leerIdPersona(kb);
        cli.buscarEstudio(DATABASE, idProf, idPers);
    }

    private int leerIdProfesion(Scanner kb) {
        try {
            System.out.print("ID profesión      ▶ ");
            return kb.nextInt();
        } catch (InputMismatchException e) {
            kb.nextLine();
            System.out.println("ID inválido.");
            return leerIdProfesion(kb);
        }
    }

    private int leerIdPersona(Scanner kb) {
        try {
            System.out.print("ID persona        ▶ ");
            return kb.nextInt();
        } catch (InputMismatchException e) {
            kb.nextLine();
            System.out.println("ID inválido.");
            return leerIdPersona(kb);
        }
    }
}
