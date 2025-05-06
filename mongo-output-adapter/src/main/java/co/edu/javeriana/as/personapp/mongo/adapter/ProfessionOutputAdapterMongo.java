package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {
    @Autowired
    private ProfesionRepositoryMongo professionRepositoryMongo;

    @Autowired
    private EstudiosRepositoryMongo studyRepositoryMongo;

    @Autowired
    private ProfesionMapperMongo professionMapperMongo;

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on Adapter MongoDB");
        try {
            ProfesionDocument persistedProfesion = professionRepositoryMongo.save(professionMapperMongo.fromDomainToAdapter(profession));
            return professionMapperMongo.fromAdapterToDomain(persistedProfesion);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return profession;
        }
    }

    @Override
    public Boolean delete(Integer professionId) {
        log.debug("Into delete on Adapter MongoDB");
        
        // Verifica si la profesión existe
        if (professionRepositoryMongo.existsById(professionId)) {
            // Elimina todos los estudios asociados a la profesión
            ProfesionDocument profesion = professionRepositoryMongo.findById(professionId).orElse(null);

            studyRepositoryMongo.deleteByPrimaryProfesion(profesion);

            // Elimina la profesión
            professionRepositoryMongo.deleteById(professionId);
            
            // Verifica si la profesión fue eliminada correctamente
            return !professionRepositoryMongo.existsById(professionId);
        } else {
            log.warn("No profession found with ID: " + professionId);
            return false; // Retorna falso si no se encontró la profesión
        }
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on Adapter MongoDB");
        return professionRepositoryMongo.findAll().stream().map(professionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        log.debug("Into findById on Adapter MongoDB");
        if (professionRepositoryMongo.findById(identification).isEmpty()) {
            return null;
        } else {
            return professionMapperMongo.fromAdapterToDomain(professionRepositoryMongo.findById(identification).get());
        }
    }
}
