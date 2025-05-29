package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudiosMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class EstudiosInputAdapterCli {

    //MariaDB
    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    //MongoDB
    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private EstudiosMapperCli estudiosMapperCli;

    //Puertos de entrada a la aplicación
    StudyInputPort studyInputPort;
    ProfessionInputPort professionInputPort;
    PersonInputPort personInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial StudyEntity in Input Adapter");
        List<EstudiosModelCli> estudios = studyInputPort.findAll().stream()
            .map(estudiosMapperCli::fromDomainToAdapterCli)
            .collect(Collectors.toList());
        imprimirTabla(estudios);
    }

    public void crearEstudios(EstudiosModelCli estudios, String dbOption) {
        log.info("Into crearEstudios StudyEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(dbOption);
            Person person = personInputPort.findOne(Integer.parseInt(estudios.getIdPerson()));
            Profession profession = professionInputPort.findOne(Integer.parseInt(estudios.getIdProfession()));
            studyInputPort.create(estudiosMapperCli.fromAdapterCliToDomain(estudios, profession, person));
            System.out.println("Estudios creado correctamente " + estudios.toString());
            imprimirTabla(List.of(estudios)); // Imprimir la tabla con el estudio creado
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al crear el estudio");
        }
    }

    public void buscarEstudio(String dbOption, Integer idProfesion, Integer idPersona) {
        log.info("Into buscarEstudioPorProfesionYPersona StudyEntity in Input Adapter");
        try {
            Study estudio = studyInputPort.findOne(idProfesion, idPersona);
            if (estudio != null) {
                List<EstudiosModelCli> estudios = List.of(estudiosMapperCli.fromDomainToAdapterCli(estudio));
                imprimirTabla(estudios); // Imprimir la tabla con el estudio encontrado
            } else {
                System.out.println("No se encontró ningún estudio para la profesión con ID: " + idProfesion +
                        " y la persona con ID: " + idPersona);
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            System.out.println("Error al buscar estudio por profesión y persona");
        }
    }

    private void imprimirTabla(List<EstudiosModelCli> estudios) {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-20s %-30s %-20s%n", "ID Persona", "ID Profesion", "Universidad", "Fecha de Graduación");
        System.out.println("---------------------------------------------------------------------------");
        for (EstudiosModelCli estudio : estudios) {
            System.out.printf("%-15s %-20s %-20s %-30s %-20s%n",
                    estudio.getIdPerson(),
                    estudio.getIdProfession(),
                    estudio.getUniversityName(),
                    estudio.getGraduationDate());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public void editarEstudio(EstudiosModelCli estudios, String dbOption) {
        log.info("Into editarEstudio StudyEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(dbOption);
            Integer idPerson = Integer.parseInt(estudios.getIdPerson());
            Integer idProfession = Integer.parseInt(estudios.getIdProfession());
            Person person = personInputPort.findOne(idPerson);
            Profession profession = professionInputPort.findOne(idProfession);
            studyInputPort.edit(idProfession, idPerson, estudiosMapperCli.fromAdapterCliToDomain(estudios, profession, person));
            System.out.println("Estudio editado correctamente " + estudios.toString());
            imprimirTabla(List.of(estudios)); // Imprimir la tabla con el estudio editado
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al editar el estudio");
        }
    }

    public void eliminarEstudio(String dbOption, Integer idProfesion, Integer idPersona) {
        log.info("Into eliminarEstudio StudyEntity in Input Adapter");
        try {
            boolean resultado = studyInputPort.drop(idProfesion, idPersona);
            if (resultado) {
                System.out.println("Estudio eliminado correctamente: " + idProfesion + " " + idPersona);
            } else {
                System.out.println("No se encontró ningún estudio para la profesión con ID: " + idProfesion +
                        " y la persona con ID: " + idPersona);
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            System.out.println("Error al buscar estudio por profesión y persona");
        }
    }
}
