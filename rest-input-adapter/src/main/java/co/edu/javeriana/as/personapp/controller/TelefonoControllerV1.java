package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
public class TelefonoControllerV1 {
    @Autowired
    private TelefonoInputAdapterRest telefonoInputAdapterRest;

    @Operation(summary = "Obtener historial de teléfonos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfonos retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Teléfonos no encontrados")
    })
    @GetMapping(path="/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TelefonoResponse>> historial(
            @Parameter(description = "Base de datos donde se quiere buscar", required = true, example = "myDatabase")
            @PathVariable String database){
        log.info("Into telefonos REST API");
        try {
            List<TelefonoResponse> telefonos = telefonoInputAdapterRest.historial(database.toUpperCase());
            return ResponseEntity.ok(telefonos);
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + database + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.warn("No telephones found in database: " + database + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelefonoResponse> crearTelefono(@RequestBody TelefonoRequest request) {
        log.info("Esta en el metodo crearTelefono en el controller del api");
        try {
            TelefonoResponse telefonoResponse = telefonoInputAdapterRest.createPhone(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(telefonoResponse);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn("Error creating phone: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "Buscar teléfono por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Teléfono no encontrado")
    })
    @GetMapping(path = "/{database}/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelefonoResponse> findPhoneById(
            @Parameter(description = "Base de datos donde se quiere buscar", required = true, example = "myDatabase")
            @PathVariable String database,
            @Parameter(description = "Número de teléfono", required = true, example = "123456789")
            @PathVariable String num){
        log.info("Into telefonoById REST API");
        try {
            TelefonoResponse telefono = telefonoInputAdapterRest.findOne(database, num);
            return ResponseEntity.ok(telefono);
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + database + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.warn("Phone not found with number: " + num + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Eliminar teléfono por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Teléfono no encontrado")
    })
    @DeleteMapping(path = "/{database}/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelefonoResponse> deletePhone(
            @Parameter(description = "Base de datos donde se quiere buscar", required = true, example = "myDatabase")
            @PathVariable String database,
            @Parameter(description = "Número de teléfono", required = true, example = "123456789")
            @PathVariable String num) {
        log.info("Into deleteTelefonoById REST API");
        try {
            TelefonoResponse response = telefonoInputAdapterRest.deletePhone(database, num);
            return ResponseEntity.ok(response);
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + database + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.warn("Phone not found with number: " + num + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Actualizar teléfono")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelefonoResponse> updatePhone(
            @Parameter(description = "Solicitud de actualización de teléfono", required = true)
            @RequestBody TelefonoRequest request) {
        log.info("Into updateTelefonoById REST API");
        try {
            TelefonoResponse response = telefonoInputAdapterRest.editPhone(request);
            return ResponseEntity.ok(response);
        } catch (InvalidOptionException e) {
            log.warn("Invalid database option: " + request.getDatabase() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.warn("Phone not found with number: " + request.getNumber() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
