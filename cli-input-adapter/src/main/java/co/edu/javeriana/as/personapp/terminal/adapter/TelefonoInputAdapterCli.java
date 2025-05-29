package co.edu.javeriana.as.personapp.terminal.adapter;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

    //MariaDB
    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    //MongoDB
    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private TelefonoMapperCli telefonoMapperCli;

    private PhoneInputPort phoneInputPort;
    private PersonInputPort personInputPort;

    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial TelefonoEntity in Input Adapter");
        List<TelefonoModelCli> telefonos = phoneInputPort.findAll().stream()
                .map(telefonoMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        imprimirTabla(telefonos);
    }

    private void imprimirTabla(List<TelefonoModelCli> telefonos) {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s%n", "Número", "ID Persona", "Compañia");
        System.out.println("---------------------------------------------------------------------------");
        for (TelefonoModelCli telefono : telefonos) {
            System.out.printf("%-15s %-15s %-20s%n",
                    telefono.getNumber(),
                    telefono.getIdPerson(),
                    telefono.getCompany());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public void crearTelefono(TelefonoModelCli telefonoModelCli, String database) {
        log.info("Into crear TelefonoEntity in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            // Find person by id
            Person owner = personInputPort.findOne(Integer.parseInt(telefonoModelCli.getIdPerson()));
            Phone phone = phoneInputPort.create(telefonoMapperCli.fromAdapterCliToDomain(telefonoModelCli, owner));
            System.out.println("Telefono creado correctamente: " + phone.toString());
            
            // Imprimir la tabla de teléfonos después de crear uno nuevo
            historial(); // Llama a historial para imprimir la tabla
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al crear el telefono");
        }
    }
    

    public void editarTelefono(TelefonoModelCli telefonoModelCli, String database) {
        log.info("Into editar TelefonoEntity in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            //Find person by id
            Person owner = personInputPort.findOne(Integer.parseInt(telefonoModelCli.getIdPerson()));
            Phone phone = phoneInputPort.edit(telefonoModelCli.getNumber(), telefonoMapperCli.fromAdapterCliToDomain(telefonoModelCli, owner));
            System.out.println("Telefono editado correctamente: " + phone.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al editar el telefono");
        }
    }

    public void eliminarTelefono(String database, String number) {
        log.info("Into eliminar TelefonoEntity in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            boolean resultado = phoneInputPort.drop(number);
            if (resultado)
                System.out.println("Telefono eliminado correctamente: " + number);
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al eliminar el telefono");
        }
    }

    public void buscarTelefono(String database, String number) {
        log.info("Into buscar TelefonoEntity in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            Phone phone = phoneInputPort.findOne(number);
            TelefonoModelCli telefonoModelCli = telefonoMapperCli.fromDomainToAdapterCli(phone);
            System.out.println("Telefono encontrado: " + telefonoModelCli.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al buscar el telefono");
        }
    }
}
