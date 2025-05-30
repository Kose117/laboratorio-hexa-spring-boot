package co.edu.javeriana.as.personapp.mongo.repository;

import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, Integer> {
    public EstudiosDocument findByPrimaryProfesionAndPrimaryPersona(Integer professionID, Integer personID);

    void deleteByPrimaryProfesion(ProfesionDocument profession);
    void deleteByPrimaryPersona(PersonaDocument person);
}