package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper
public class TelefonoMapperMongo {

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	public TelefonoDocument fromDomainToAdapter(Phone phone) {
		TelefonoDocument telefonoDocument = new TelefonoDocument();
		telefonoDocument.setId(phone.getNumber());
		telefonoDocument.setOper(phone.getCompany());
		telefonoDocument.setPrimaryDuenio(validateDuenio(phone.getOwner()));
		log.warn("Mapping from domain to adapter"+ telefonoDocument);
		return telefonoDocument;
	}

	private PersonaDocument validateDuenio(@NonNull Person owner) {
		log.info("null owner: "+owner);
		return owner != null ? personaMapperMongo.fromDomainToAdapter(owner) : new PersonaDocument();
	}

	public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
		Phone phone = new Phone();
		phone.setNumber(telefonoDocument.getId());
		phone.setCompany(telefonoDocument.getOper());
		phone.setOwner(validateOwner(telefonoDocument.getPrimaryDuenio()));
		log.warn("Mapping from adapter to domain"+ phone);
		return phone;
	}

	private @NonNull Person validateOwner(PersonaDocument duenio) {
		Person owner = new Person();
		log.info("null owner MONGO: "+duenio);
		owner.setIdentification(duenio.getId());
		owner.setFirstName(duenio.getNombre());
		owner.setLastName(duenio.getApellido());
		//si es genero MALE PONER gender.MALE
		if("M".equals(duenio.getGenero())) {
			owner.setGender(Gender.MALE);
		}
		else{
			owner.setGender(Gender.FEMALE);
		}
		owner.setAge(duenio.getEdad());
		return owner;
	}
}