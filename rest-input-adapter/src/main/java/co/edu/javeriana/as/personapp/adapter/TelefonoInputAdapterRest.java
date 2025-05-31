package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private TelefonoMapperRest telefonoMapperRest;

    private PersonInputPort personInputPort;
    private PhoneInputPort phoneInputPort;

    /** inyecta los puertos correctos y devuelve \"MARIA\" o \"MONGO\" */
    private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort  = new PhoneUseCase(phoneOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort  = new PhoneUseCase(phoneOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<TelefonoResponse> historial(String database)
            throws InvalidOptionException, NoExistException {

        log.info("Into historial TelefonoEntity in Input Adapter");
        String db = setPhoneOutputPortInjection(database);

        List<TelefonoResponse> phones = phoneInputPort.findAll()
                .stream()
                .map(p -> db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                        ? telefonoMapperRest.fromDomainToAdapterRestMaria(p)
                        : telefonoMapperRest.fromDomainToAdapterRestMongo(p))
                .collect(Collectors.toList());

        if (phones.isEmpty()) {
            throw new NoExistException("No phones found in database: " + database);
        }
        return phones;
    }

    public TelefonoResponse createPhone(TelefonoRequest request)
            throws InvalidOptionException, NoExistException {

        log.info("Into createPhone in Input Adapter");
        String db = setPhoneOutputPortInjection(request.getDatabase());

        Person owner = personInputPort.findOne(Integer.parseInt(request.getOwner()));
        if (owner == null) {
            throw new NoExistException("Owner not found with ID: " + request.getOwner());
        }
        Phone phone = phoneInputPort.create(
                telefonoMapperRest.fromAdapterToDomain(request, owner));

        return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                ? telefonoMapperRest.fromDomainToAdapterRestMaria(phone)
                : telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
    }

    public TelefonoResponse findOne(String database, String number)
            throws InvalidOptionException, NoExistException {

        log.info("Into findOne TelefonoEntity in Input Adapter");
        String db = setPhoneOutputPortInjection(database);

        Phone phone = phoneInputPort.findOne(number);
        if (phone == null) {
            throw new NoExistException("Phone not found with number: " + number);
        }

        return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                ? telefonoMapperRest.fromDomainToAdapterRestMaria(phone)
                : telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
    }

    public TelefonoResponse deletePhone(String database, String number)
            throws InvalidOptionException, NoExistException {

        log.info("Into deletePhone in Input Adapter");
        setPhoneOutputPortInjection(database);

        boolean deleted = phoneInputPort.drop(number);
        if (!deleted) {
            throw new NoExistException("Phone not found with number: " + number);
        }
        return new TelefonoResponse("Phone deleted", "", "", database, "DELETED");
    }

    public TelefonoResponse editPhone(TelefonoRequest request)
            throws InvalidOptionException, NoExistException {

        log.info("Into editPhone in Input Adapter");
        String db = setPhoneOutputPortInjection(request.getDatabase());

        Person owner = personInputPort.findOne(Integer.parseInt(request.getOwner()));
        if (owner == null) {
            throw new NoExistException("Owner not found with ID: " + request.getOwner());
        }
        Phone updated = phoneInputPort.edit(
                request.getNumber(),
                telefonoMapperRest.fromAdapterToDomain(request, owner));

        if (updated == null) {
            throw new NoExistException("Phone not found with number: " + request.getNumber());
        }

        return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                ? telefonoMapperRest.fromDomainToAdapterRestMaria(updated)
                : telefonoMapperRest.fromDomainToAdapterRestMongo(updated);
    }
}
