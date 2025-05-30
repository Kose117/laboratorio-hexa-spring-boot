package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper
public class EstudiosMapperMaria {

    @Autowired
    private PersonaMapperMaria personaMapperMaria;

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    public EstudiosEntity fromDomainToAdapter(Study study) {
        EstudiosEntityPK estudioPK = new EstudiosEntityPK();
        estudioPK.setCcPer(study.getPerson().getIdentification());
        estudioPK.setIdProf(study.getProfession().getIdentification());
        
        EstudiosEntity estudio = new EstudiosEntity();
        estudio.setEstudiosPK(estudioPK);
        estudio.setFecha(validateFecha(study.getGraduationDate()));
        estudio.setUniver(validateUniver(study.getUniversityName()));
        estudio.setPersona(personaMapperMaria.fromDomainToAdapter(study.getPerson()));
        estudio.setProfesion(profesionMapperMaria.fromDomainToAdapter(study.getProfession()));
        return estudio;
    }

    private Date validateFecha(LocalDate graduationDate) {
        return graduationDate != null
                ? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
                : null;
    }

    private String validateUniver(String universityName) {
        return universityName != null ? universityName : "";
    }

    public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
        Study study = new Study();
		
		log.warn("Mapping from adapter to domain"+ estudiosEntity+ " "+estudiosEntity.getPersona());

        // En lugar de llamar al adaptador, construir directamente con identificador
        study.setPerson(validatePrimaryPersona(estudiosEntity.getPersona()));
        study.setProfession(validatePrimaryProfesion(estudiosEntity.getProfesion()));
        
        study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
        study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
        

        log.warn("Resultado"+ study);

        return study; // Retornar el objeto Study construido
    }

    private LocalDate validateGraduationDate(Date graduationDate) {
		log.warn("Mapping validateGraduationDate: {}", graduationDate);

        if (graduationDate == null) {
            return null; // o manejar el caso de error según tu lógica
        }

        // Usar Calendar para obtener los componentes de la fecha
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(graduationDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Los meses empiezan en 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el LocalDate usando los componentes
        LocalDate localGraduationDate = LocalDate.of(year, month, day);

        // Imprimir la fecha convertida (opcional)
        log.info("Converted graduation date to LocalDate: {}", localGraduationDate);
        
        return localGraduationDate;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}

    private @NonNull Person validatePrimaryPersona(@NonNull PersonaEntity persona) {
        Person owner = new Person();
        owner.setIdentification(persona.getCc()); // Asigna solo el ID
        owner.setFirstName(persona.getNombre());
        owner.setLastName(persona.getApellido());
        owner.setAge(persona.getEdad());
        if(persona.getGenero()=='M')
        {
            owner.setGender(Gender.MALE);
        }
        else
        {
            owner.setGender(Gender.FEMALE);
        }
        return owner;
    }

    private @NonNull Profession validatePrimaryProfesion(@NonNull ProfesionEntity profesion) {
        Profession profession = new Profession();
        profession.setIdentification(profesion.getId()); // Asigna solo el ID
        profession.setDescription(profesion.getDes());
        profesion.setNom(profesion.getNom());
        return profession;
    }
}
