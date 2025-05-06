package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface ProfessionInputPort {
    void setPersistence(ProfessionOutputPort professionPersistence);

    Profession create(Profession profession);

    Profession edit(Integer identification, Profession profession) throws NoExistException;

    Boolean drop(Integer identification) throws NoExistException;

    List<Profession> findAll();

    Profession findOne(Integer identification) throws NoExistException;

    Integer count();
    
    List<Study> getStudies(Integer identification) throws NoExistException;
}
