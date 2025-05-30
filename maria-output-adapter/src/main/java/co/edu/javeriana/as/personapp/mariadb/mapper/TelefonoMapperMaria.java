package co.edu.javeriana.as.personapp.mariadb.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper
public class TelefonoMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	public TelefonoEntity fromDomainToAdapter(Phone phone) {
		log.warn("Mapping from domain to adapter"+ phone);

		TelefonoEntity telefonoEntity = new TelefonoEntity();
		telefonoEntity.setNum(phone.getNumber());
		telefonoEntity.setOper(phone.getCompany());
		telefonoEntity.setDuenio(validateDuenio(phone.getOwner()));
		return telefonoEntity;
	}

	private PersonaEntity validateDuenio(@NonNull Person owner) {
		return owner != null ? personaMapperMaria.fromDomainToAdapter(owner) : new PersonaEntity();
	}

	public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
		Phone phone = new Phone();
		phone.setNumber(telefonoEntity.getNum());
		phone.setCompany(telefonoEntity.getOper());
		phone.setOwner(validateOwner(telefonoEntity.getDuenio()));
		return phone;
	}

	private @NonNull Person validateOwner(PersonaEntity duenio) {
		Person owner = new Person();
		log.info("null owner: "+duenio);
		owner.setIdentification(duenio.getCc());
		owner.setFirstName(duenio.getNombre());
		owner.setLastName(duenio.getApellido());
		//si es genero MALE PONER gender.MALE
		if(duenio.getGenero() == 'M') {
			owner.setGender(Gender.MALE);
		}
		else{
			owner.setGender(Gender.FEMALE);
		}
		owner.setAge(duenio.getEdad());
		return owner;
	}
}