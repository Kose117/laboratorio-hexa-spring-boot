package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersistence;

    public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public void setPersistence(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Into create on Application Domain");
        return phonePersistence.save(phone);
    }

    @Override
    public Phone edit(String number, Phone phone) throws NoExistException {
        
        Phone oldPerson = phonePersistence.findById(number);
        log.warn("Old phone: " + phone);
        if (oldPerson != null)
            return phonePersistence.save(phone);
        throw new NoExistException(
                "The phone with id " + number + " does not exist into db, cannot be edited");
    }

    @Override
    public Boolean drop(String number) throws NoExistException {
        Phone oldPhone = phonePersistence.findById(number);
        if (oldPhone != null)
            return phonePersistence.delete(number);
        throw new NoExistException(
                "The phone with id " + number + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Phone> findAll() {
        log.info("Output: " + phonePersistence.getClass());
        return phonePersistence.find();
    }

    @Override
    public Phone findOne(String number) throws NoExistException {
        Phone oldPhone = phonePersistence.findById(number);
        if (oldPhone != null)
            return oldPhone;
        throw new NoExistException(
                "The phone with id " + number + " does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public Person getPerson(String identification) throws NoExistException {
        Phone phone = phonePersistence.findById(identification);
        if (phone != null)
            return phone.getOwner();
        throw new NoExistException(
                "The phone with id " + identification + " does not exist into db, cannot be found");
    }
}
