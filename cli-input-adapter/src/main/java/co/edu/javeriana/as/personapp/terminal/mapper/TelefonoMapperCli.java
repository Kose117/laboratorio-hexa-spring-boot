package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

    public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
        TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
        telefonoModelCli.setNumber(phone.getNumber());
        telefonoModelCli.setCompany(phone.getCompany());
        telefonoModelCli.setIdPerson(phone.getOwner().getIdentification()+"");
        return telefonoModelCli;
    }

    public Phone fromAdapterCliToDomain(TelefonoModelCli telefonoModelCli, Person Owner) {
        Phone phone = new Phone();
        phone.setNumber(telefonoModelCli.getNumber());
        phone.setCompany(telefonoModelCli.getCompany());
        phone.setOwner(Owner);
        return phone;
    }
}