package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}
	public void historial() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> personas = personInputPort.findAll().stream()
				.map(personaMapperCli::fromDomainToAdapterCli)
				.collect(Collectors.toList());
	
		imprimirTabla(personas);
	}
	
	private void imprimirTabla(List<PersonaModelCli> personas) {
		System.out.println("---------------------------------------------------------------------------");
		System.out.printf("%-15s %-20s %-20s %-10s %-10s%n", "Identificación", "Nombre", "Apellido", "Género", "Edad");
		System.out.println("---------------------------------------------------------------------------");
		for (PersonaModelCli persona : personas) {
			System.out.printf("%-15d %-20s %-20s %-10s %-10d%n",
					persona.getCc(),
					persona.getNombre(),
					persona.getApellido(),
					persona.getGenero(),
					persona.getEdad());
		}
		System.out.println("---------------------------------------------------------------------------");
	}

	public void crearPersona(PersonaModelCli persona, String dbOption) {
		log.info("Into crear PersonaEntity in Input Adapter");
		try {
			setPersonOutputPortInjection(dbOption);
			personInputPort.create(personaMapperCli.fromAdapterCliToDomain(persona));
			// Imprimimos la tabla con la persona creada
			imprimirTabla(List.of(persona)); // Puedes necesitar ajustar esto si persona no es del tipo PersonaModelCli.
		} catch (Exception e) {
			log.warn(e.getMessage());
			System.out.println("Error al crear persona");
		}
	}

	public void editarPersona(PersonaModelCli persona, String dbOption) {
		log.info("Into editar PersonaEntity in Input Adapter");
		try {
			setPersonOutputPortInjection(dbOption);
			personInputPort.edit(persona.getCc(), personaMapperCli.fromAdapterCliToDomain(persona));
			// Imprimimos la tabla con la persona editada
			imprimirTabla(List.of(persona));
		} catch (Exception e) {
			log.warn(e.getMessage());
			System.out.println("Error al editar persona");
		}
	}

	public void eliminarPersona(String dbOption, int cc) {
		log.info("Into eliminar PersonaEntity in Input Adapter");
		try {
			setPersonOutputPortInjection(dbOption);
			boolean resultado = personInputPort.drop(cc);
			if (resultado)
				System.out.println("Persona eliminada correctamente: " + cc);
		} catch (Exception e) {
			log.warn(e.getMessage());
			System.out.println("Error al eliminar persona");
		}
	}

	public void buscarPersona(String dbOption, int cc) {
		log.info("Into buscar PersonaEntity in Input Adapter");
		try {
			setPersonOutputPortInjection(dbOption);
			Person person = personInputPort.findOne(cc);
			if (person != null) {
				PersonaModelCli persona = personaMapperCli.fromDomainToAdapterCli(person);
				// Aquí creamos una lista solo con la persona encontrada
				List<PersonaModelCli> personaList = List.of(persona);
				// Imprimimos la tabla
				imprimirTabla(personaList);
			} else {
				System.out.println("Persona no encontrada con identificación: " + cc);
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
			System.out.println("Error al buscar persona");
		}
	}

}
