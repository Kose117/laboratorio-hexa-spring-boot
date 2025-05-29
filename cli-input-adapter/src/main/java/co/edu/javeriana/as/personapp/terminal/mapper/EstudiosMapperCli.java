package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;

@Mapper
public class EstudiosMapperCli {

    public EstudiosModelCli fromDomainToAdapterCli(Study study)
    {
        EstudiosModelCli estudioModelCli = new EstudiosModelCli();
        estudioModelCli.setGraduationDate(study.getGraduationDate());
        estudioModelCli.setUniversityName(study.getUniversityName());
        estudioModelCli.setIdPerson(study.getPerson().getIdentification()+"");
        estudioModelCli.setIdProfession(study.getProfession().getIdentification()+"");
        return estudioModelCli;
    }

    public Study fromAdapterCliToDomain(EstudiosModelCli estudioModelCli, Profession profession, Person person)
    {
        Study study = new Study();
        study.setGraduationDate(estudioModelCli.getGraduationDate());
        study.setUniversityName(estudioModelCli.getUniversityName());
        study.setProfession(profession);
        study.setPerson(person);
        return study;
    }
}