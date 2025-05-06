package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;

import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMongo estudioRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudioMapperMongo;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        try {
            EstudiosDocument persistedEstudio = estudioRepositoryMongo.save(estudioMapperMongo.fromDomainToAdapter(study));
            return estudioMapperMongo.fromAdapterToDomain(persistedEstudio);
        }catch (MongoWriteException e)
        {
            log.warn(e.getMessage());
            return study;
        }
    }

    @Override
    public Boolean delete(Integer professionID, Integer personID) {
        log.debug("Into delete on Adapter MongoDB");
        EstudiosDocument estudio = estudioRepositoryMongo.findByPrimaryProfesionAndPrimaryPersona(professionID, personID);
        if (estudio == null) {
            return false;
        }
        estudioRepositoryMongo.delete(estudio);
        return estudioRepositoryMongo.findByPrimaryProfesionAndPrimaryPersona(professionID, personID) == null;
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return estudioRepositoryMongo.findAll().stream().map(estudioMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer proffesionID, Integer personID) {
        log.debug("Into findById on Adapter MongoDB");
        EstudiosDocument estudio = estudioRepositoryMongo.findByPrimaryProfesionAndPrimaryPersona(proffesionID, personID);
        if (estudio == null) {
            return null;
        } else {
            return estudioMapperMongo.fromAdapterToDomain(estudio);
        }
    }
}