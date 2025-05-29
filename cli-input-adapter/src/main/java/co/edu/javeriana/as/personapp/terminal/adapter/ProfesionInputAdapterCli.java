package co.edu.javeriana.as.personapp.terminal.adapter;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperCli profesionMapperCli;

    private ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial ProfessionEntity in Input Adapter");
        List<ProfesionModelCli> profesiones = professionInputPort.findAll().stream()
                .map(profesionMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        imprimirTabla(profesiones);
    }

    private void imprimirTabla(List<ProfesionModelCli> profesiones) {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-30s%n", "ID", "Nombre", "Descripción");
        System.out.println("---------------------------------------------------------------------------");
        for (ProfesionModelCli profesion : profesiones) {
            System.out.printf("%-10d %-20s %-30s%n",
                    profesion.getId(),
                    profesion.getName(),
                    profesion.getDescription());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public void crearProfesion(ProfesionModelCli profesionModelCli, String dbOption) {
        log.info("Into crearProfesion ProfessionEntity in Input Adapter");
        try {
            setProfessionOutputPortInjection(dbOption);
            professionInputPort.create(profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
            System.out.println("Profesion creada con éxito: " + profesionModelCli.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al crear profesión");
        }
    }

    public void editarProfesion(ProfesionModelCli profesionModelCli, String dbOption) {
        log.info("Into editarProfesion ProfessionEntity in Input Adapter");
        try {
            setProfessionOutputPortInjection(dbOption);
            Profession profession = professionInputPort.edit(profesionModelCli.getId(), profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
            System.out.println("Profesion editada con éxito: " + profession.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al editar: " + profesionModelCli.toString());
        }
    }

    public void eliminarProfesion(String dbOption, int cc) {
        log.info("Into eliminarProfesion ProfessionEntity in Input Adapter");
        try {
            setProfessionOutputPortInjection(dbOption);
            boolean resultado = professionInputPort.drop(cc);
            if (resultado) {
                System.out.println("Profesion eliminada con éxito: " + cc);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al eliminar profesión");
        }
    }

    public void buscarProfesion(String dbOption, int cc) {
        log.info("Into buscarProfesion ProfessionEntity in Input Adapter");
        try {
            setProfessionOutputPortInjection(dbOption);
            Profession profession = professionInputPort.findOne(cc);
            ProfesionModelCli profesionModelCli = profesionMapperCli.fromDomainToAdapterCli(profession);
            System.out.println("Profesion encontrada: " + profesionModelCli.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al buscar profesión");
        }
    }
}
