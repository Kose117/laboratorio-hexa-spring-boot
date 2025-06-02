package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.terminal.adapter.EstudiosInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class MenuPrincipal {

    @Autowired private PersonaInputAdapterCli   personaInputAdapterCli;
    @Autowired private ProfesionInputAdapterCli profesionInputAdapterCli;
    @Autowired private TelefonoInputAdapterCli  telefonoInputAdapterCli;
    @Autowired private EstudiosInputAdapterCli  estudiosInputAdapterCli;

    private static final int SALIR            = 0;
    private static final int MODULO_PERSONA   = 1;
    private static final int MODULO_PROFESION = 2;
    private static final int MODULO_TELEFONO  = 3;
    private static final int MODULO_ESTUDIOS  = 4;

    private final PersonaMenu  personaMenu;
    private final ProfesionMenu profesionMenu;
    private final TelefonoMenu  telefonoMenu;
    private final EstudiosMenu  estudiosMenu;
    private final Scanner       keyboard;

    public MenuPrincipal() {
        this.personaMenu   = new PersonaMenu();
        this.profesionMenu = new ProfesionMenu();
        this.telefonoMenu  = new TelefonoMenu();
        this.estudiosMenu  = new EstudiosMenu();
        this.keyboard      = new Scanner(System.in);
    }

    public void inicio() {
        boolean salir = false;
        do {
            mostrarMenu();
            int opcion = leerOpcion();
            switch (opcion) {
                case SALIR:
                    salir = true;
                    log.info("Saliendo del sistema.");
                    break;
                case MODULO_PERSONA:
                    personaMenu.iniciarMenu(personaInputAdapterCli, keyboard);
                    break;
                case MODULO_PROFESION:
                    profesionMenu.iniciarMenu(profesionInputAdapterCli, keyboard);
                    break;
                case MODULO_TELEFONO:
                    telefonoMenu.iniciarMenu(telefonoInputAdapterCli, keyboard);
                    break;
                case MODULO_ESTUDIOS:
                    estudiosMenu.iniciarMenu(estudiosInputAdapterCli, keyboard);
                    break;
                default:
                    log.warn("La opción elegida no es válida: " + opcion);
            }
        } while (!salir);
        keyboard.close();
    }

    /* ════════════════════════  PRESENTACIÓN EN ASCII  ════════════════════════ */

    private void mostrarMenu() {
        System.out.println("+----------------------------------------------------+");
        System.out.println("|              M E N U   P R I N C I P A L           |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("| 1 |  Personas                                      |");
        System.out.println("| 2 |  Profesiones                                   |");
        System.out.println("| 3 |  Teléfonos                                     |");
        System.out.println("| 4 |  Estudios                                      |");
        System.out.println("| 0 |  Salir                                         |");
        System.out.println("+----------------------------------------------------+");
        System.out.print ("Seleccione opción ▶ ");
    }

    /* ════════════════════════  UTILIDADES DE LECTURA  ════════════════════════ */

    private int leerOpcion() {
        try {
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.next();          // limpia token inválido
            return leerOpcion();
        }
    }
}
