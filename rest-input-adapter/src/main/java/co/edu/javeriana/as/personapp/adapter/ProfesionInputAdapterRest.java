package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperRest profesionMapperRest;

    private ProfessionInputPort professionInputPort;

    /** inyecta el puerto correcto y devuelve \"MARIA\" o \"MONGO\" */
    private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<ProfesionResponse> historial(String database) {
        log.info("Consultando historial en ProfesionInputAdapterRest");
        try {
            String db = setProfessionOutputPortInjection(database);
            return professionInputPort.findAll().stream()
                    .map(p -> db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                            ? profesionMapperRest.fromDomainToAdapterRestMaria(p)
                            : profesionMapperRest.fromDomainToAdapterRestMongo(p))
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", database);
            throw new RuntimeException("Opción de base de datos no válida: " + database);
        } catch (Exception e) {
            log.error("Error al obtener historial: {}", e.getMessage());
            throw new RuntimeException("Error al obtener historial de profesiones.");
        }
    }

    public ProfesionResponse createProfession(ProfesionRequest request) {
        try {
            String db = setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = profesionMapperRest.fromAdapterToDomain(request);
            Profession created = professionInputPort.create(profession);
            return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                    ? profesionMapperRest.fromDomainToAdapterRestMaria(created)
                    : profesionMapperRest.fromDomainToAdapterRestMongo(created);
        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", request.getDatabase());
            throw new RuntimeException("Opción de base de datos no válida: " + request.getDatabase());
        } catch (Exception e) {
            log.error("Error al crear profesión: {}", e.getMessage());
            throw new RuntimeException("Error al crear la profesión.");
        }
    }

    public ProfesionResponse findOneProfession(String database, String id) {
        try {
            String db = setProfessionOutputPortInjection(database);
            Profession profession = professionInputPort.findOne(Integer.parseInt(id));
            return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                    ? profesionMapperRest.fromDomainToAdapterRestMaria(profession)
                    : profesionMapperRest.fromDomainToAdapterRestMongo(profession);
        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", database);
            throw new RuntimeException("Opción de base de datos no válida: " + database);
        } catch (NumberFormatException e) {
            log.error("ID no válido: {}", id);
            throw new RuntimeException("ID de profesión no válido: " + id);
        } catch (Exception e) {
            log.error("Error al buscar profesión: {}", e.getMessage());
            throw new RuntimeException("Error al buscar la profesión con ID: " + id);
        }
    }

    public ProfesionResponse eliminarProfesion(String database, String id) {
        try {
            setProfessionOutputPortInjection(database);
            boolean isDeleted = professionInputPort.drop(Integer.parseInt(id));
            return new ProfesionResponse(isDeleted ? Integer.parseInt(id) : 0,
                    "DELETED", "DELETED", "Profesión eliminada", "DELETED");
        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", database);
            throw new RuntimeException("Opción de base de datos no válida: " + database);
        } catch (NumberFormatException e) {
            log.error("ID no válido: {}", id);
            throw new RuntimeException("ID de profesión no válido: " + id);
        } catch (Exception e) {
            log.error("Error al eliminar profesión: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la profesión con ID: " + id);
        }
    }

    public ProfesionResponse editarProfesion(ProfesionRequest request) {
        try {
            String db = setProfessionOutputPortInjection(request.getDatabase());
            Profession updated = professionInputPort.edit(
                    request.getId(), profesionMapperRest.fromAdapterToDomain(request));
            return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                    ? profesionMapperRest.fromDomainToAdapterRestMaria(updated)
                    : profesionMapperRest.fromDomainToAdapterRestMongo(updated);
        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", request.getDatabase());
            throw new RuntimeException("Opción de base de datos no válida: " + request.getDatabase());
        } catch (Exception e) {
            log.error("Error al editar profesión: {}", e.getMessage());
            throw new RuntimeException("Error al editar la profesión.");
        }
    }
}
