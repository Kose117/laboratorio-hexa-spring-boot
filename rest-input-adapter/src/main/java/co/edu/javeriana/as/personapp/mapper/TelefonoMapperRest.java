package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public class TelefonoMapperRest {
    private static final Logger log = LoggerFactory.getLogger(TelefonoMapperRest.class);
    public TelefonoResponse fromDomainToAdapterRestMaria(Phone phone) {
        return fromDomainToAdapterRest(phone, "MariaDB");
    }

    public TelefonoResponse fromDomainToAdapterRestMongo(Phone phone) {
        return fromDomainToAdapterRest(phone, "MongoDB");
    }

    public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
        if (phone.getOwner() == null || phone.getOwner().getIdentification() == null) {
            log.warn("Owner or identification is null for phone: " + phone);
            // Manejo de error o retorno de un valor predeterminado
            return new TelefonoResponse(phone.getNumber(), phone.getCompany(), "Unknown", database, "Error: Owner not found");
        }
        return new TelefonoResponse(
                phone.getNumber(),
                phone.getCompany(),
                phone.getOwner().getIdentification() + "",
                database,
                "OK");
    }
    
    

    public Phone fromAdapterToDomain(TelefonoRequest telefonoRequest, Person owner) {
        Phone phone = new Phone();
        phone.setNumber(telefonoRequest.getNumber());
        phone.setCompany(telefonoRequest.getOper());
        phone.setOwner(owner);
        return phone;
    }
}
