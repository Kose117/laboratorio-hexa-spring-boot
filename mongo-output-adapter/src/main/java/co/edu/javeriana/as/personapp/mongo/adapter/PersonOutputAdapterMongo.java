package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.PersonaMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("personOutputAdapterMongo")
public class PersonOutputAdapterMongo implements PersonOutputPort {
	
	@Autowired
    private PersonaRepositoryMongo personaRepositoryMongo;
	
	@Autowired
    private EstudiosRepositoryMongo studyRepositoryMongo;
	
	@Autowired
	private PersonaMapperMongo personaMapperMongo;
	
	@Override
	public Person save(Person person) {
		log.debug("Into save on Adapter MongoDB");
		try {
			PersonaDocument persistedPersona = personaRepositoryMongo.save(personaMapperMongo.fromDomainToAdapter(person));
			return personaMapperMongo.fromAdapterToDomain(persistedPersona);
		} catch (MongoWriteException e) {
			log.warn(e.getMessage());
			return person;
		}		
	}

	@Override
public Boolean delete(Integer personId) {
    log.debug("Into delete on Adapter MongoDB");

    // Verifica si la persona existe
    if (personaRepositoryMongo.existsById(personId)) {
        // Encuentra la persona usando su ID
        PersonaDocument persona = personaRepositoryMongo.findById(personId).orElse(null);

        // Elimina todos los estudios asociados a la persona
        if (persona != null) {
            studyRepositoryMongo.deleteByPrimaryPersona(persona);
        }

        // Elimina la persona
        personaRepositoryMongo.deleteById(personId);

        // Verifica si la persona fue eliminada correctamente
        return !personaRepositoryMongo.existsById(personId);
    } else {
        log.warn("No person found with ID: " + personId);
        return false; // Retorna falso si no se encontró la persona
    }
}


	@Override
	public List<Person> find() {
		log.debug("Into find on Adapter MongoDB");
		return personaRepositoryMongo.findAll().stream().map(personaMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Person findById(Integer identification) {
		log.debug("Into findById on Adapter MongoDB");
		if (personaRepositoryMongo.findById(identification).isEmpty()) {
			return null;
		} else {
			return personaMapperMongo.fromAdapterToDomain(personaRepositoryMongo.findById(identification).get());
		}
	}

}
