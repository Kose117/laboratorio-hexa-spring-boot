package co.edu.javeriana.as.personapp.adapter;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortOut;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private EstudioMapperRest estudioMapperRest;

    private PersonInputPort personInputPort;
    private StudyInputPort studyInputPort;
    private ProfessionInputPort professionInputPort;

    public String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortOut);
            personInputPort = new PersonUseCase(personOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<EstudioResponse> historial(String database) {
        log.info("Into historial PersonaEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(database);
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream()
                        .map(estudioMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream()
                        .map(estudioMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public EstudioResponse createStudy(EstudioRequest estudioRequest) {
        log.info("Into crearEstudio StudyEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(estudioRequest.getDatabase());
            Person person = personInputPort.findOne(Integer.parseInt(estudioRequest.getId_cc()));
            log.info("Into crearEstudio persona: " + person);
            Profession profession = professionInputPort.findOne(Integer.parseInt(estudioRequest.getId_pro()));
            log.info("Into crearEstudio profession: " + profession);
            Study study = studyInputPort.create(estudioMapperRest.fromAdapterToDomain(estudioRequest, profession, person));
            log.info("Into crearEstudio StudyEntity: " + study);
            return estudioMapperRest.fromDomainToAdapterRestMaria(study);
        } catch (Exception e) {
            log.warn("Error creating study: " + e.getMessage());
            throw new RuntimeException("Error creating study", e);
        }
    }

    public EstudioResponse findOne(String database, String id_person, String id_profession) {
        try {
            setStudyOutputPortInjection(database);
            log.info("Into findOne StudyEntity in Input Adapter: " + id_person + " " + id_profession);
            Study study = studyInputPort.findOne(Integer.parseInt(id_profession), Integer.parseInt(id_person));
            log.info("Found StudyEntity: " + study);
            return estudioMapperRest.fromDomainToAdapterRestMaria(study);
        } catch (Exception e) {
            log.warn("Error finding study: " + e.getMessage());
            throw new RuntimeException("Error finding study", e);
        }
    }

    public EstudioResponse deleteStudio(String database, String id_person, String id_profession) {
        try {
            setStudyOutputPortInjection(database);
            studyInputPort.drop(Integer.parseInt(id_profession), Integer.parseInt(id_person));
            return new EstudioResponse("DELETED", "DELETED", LocalDate.now(), "DELETED", database, "DELETED");
        } catch (Exception e) {
            log.warn("Error deleting study: " + e.getMessage());
            throw new RuntimeException("Error deleting study", e);
        }
    }

    public EstudioResponse editStudio(EstudioRequest estudioRequest) {
        try {
            setStudyOutputPortInjection(estudioRequest.getDatabase());
            log.info("Into updateEstudio StudyEntity in Input Adapter: " + estudioRequest);
            Person person = personInputPort.findOne(Integer.parseInt(estudioRequest.getId_cc()));
            log.info("Into updateEstudio persona: " + person);
            Profession profession = professionInputPort.findOne(Integer.parseInt(estudioRequest.getId_pro()));
            log.info("Into updateEstudio profession: " + profession);
            Study updatedStudy = studyInputPort.edit(
                    Integer.parseInt(estudioRequest.getId_pro()),
                    Integer.parseInt(estudioRequest.getId_cc()),
                    estudioMapperRest.fromAdapterToDomain(estudioRequest, profession, person));
            return estudioMapperRest.fromDomainToAdapterRestMaria(updatedStudy);
        } catch (Exception e) {
            log.warn("Error updating study: " + e.getMessage());
            throw new RuntimeException("Error updating study", e);
        }
    }
}
