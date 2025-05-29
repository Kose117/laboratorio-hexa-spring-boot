package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private PersonaMapperRest personaMapperRest;

    private PersonInputPort personInputPort;

    private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<PersonaResponse> historial(String database) throws InvalidOptionException, NoExistException {
        log.info("Into historial PersonaEntity in Input Adapter");
        setPersonOutputPortInjection(database);
        List<PersonaResponse> personas = personInputPort.findAll()
            .stream()
            .map(personaMapperRest::fromDomainToAdapterRestMaria)
            .collect(Collectors.toList());
        
        if (personas.isEmpty()) {
            throw new NoExistException("No personas found in database: " + database);
        }
        return personas;
    }

    public PersonaResponse findOne(String database, String identification) throws InvalidOptionException, NoExistException {
        log.info("Into findOne PersonaEntity in Input Adapter");
        setPersonOutputPortInjection(database);
        Person person = personInputPort.findOne(Integer.parseInt(identification));
        if (person == null) {
            throw new NoExistException("Person not found with identification: " + identification);
        }
        return personaMapperRest.fromDomainToAdapterRestMaria(person);
    }

    public PersonaResponse crearPersona(PersonaRequest personaRequest) throws InvalidOptionException {
        log.info("Into crearPersona in Input Adapter");
        setPersonOutputPortInjection(personaRequest.getDatabase());
        Person person = personaMapperRest.fromAdapterToDomain(personaRequest);
        personInputPort.create(person);
        return personaMapperRest.fromDomainToAdapterRestMaria(person);
    }

    public PersonaResponse eliminarPersona(String database, String identification) throws InvalidOptionException, NoExistException {
        log.info("Into eliminarPersona in Input Adapter");
        setPersonOutputPortInjection(database);
        boolean deleted = personInputPort.drop(Integer.parseInt(identification));
        if (!deleted) {
            throw new NoExistException("Person not found with identification: " + identification);
        }
        return new PersonaResponse("Person deleted", "", "", "", "", database, "");
    }

    public PersonaResponse editarPersona(PersonaRequest personaRequest) throws InvalidOptionException, NoExistException {
        log.info("Into editarPersona in Input Adapter");
        setPersonOutputPortInjection(personaRequest.getDatabase());
        Person updatedPerson = personInputPort.edit(Integer.parseInt(personaRequest.getDni()), personaMapperRest.fromAdapterToDomain(personaRequest));
        if (updatedPerson == null) {
            throw new NoExistException("Person not found with DNI: " + personaRequest.getDni());
        }
        return personaMapperRest.fromDomainToAdapterRestMaria(updatedPerson);
    }
}
