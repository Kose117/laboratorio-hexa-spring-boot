package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

    @Autowired
    private PersonaInputAdapterRest personaInputAdapterRest;

    @Operation(summary = "Obtener personas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personas retornadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Personas no encontradas")
    })
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonaResponse>> personas(@PathVariable String database) {
        log.info("Into personas REST API");
        try {
            List<PersonaResponse> personas = personaInputAdapterRest.historial(database.toUpperCase());
            return ResponseEntity.ok(personas);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request) {
        log.info("Creating person");
        try {
            PersonaResponse persona = personaInputAdapterRest.crearPersona(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(persona);
        } catch (InvalidOptionException e) {
            log.warn("Invalid option: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error creating person: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar persona por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @GetMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> findPersonById(
            @Parameter(description = "Nombre de la base de datos", required = true, example = "myDatabase")
            @PathVariable String database,
            @Parameter(description = "Cédula de la persona", required = true, example = "1234567890")
            @PathVariable String cc) {
        log.info("Into personaById REST API");
        try {
            PersonaResponse persona = personaInputAdapterRest.findOne(database, cc);
            return ResponseEntity.ok(persona);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar persona por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @DeleteMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> deletePersonaById(
            @Parameter(description = "Nombre de la base de datos", required = true, example = "myDatabase")
            @PathVariable String database,
            @Parameter(description = "Cédula de la persona", required = true, example = "1234567890")
            @PathVariable String cc) {
        log.info("Into deletePersonaById REST API");
        try {
            PersonaResponse response = personaInputAdapterRest.eliminarPersona(database, cc);
            return ResponseEntity.ok(response);
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar persona por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> updatePersonaById(
            @Parameter(description = "Solicitud de actualización de persona", required = true)
            @RequestBody PersonaRequest request) {
        log.info("Into updatePersonaById REST API");
        try {
            PersonaResponse persona = personaInputAdapterRest.editarPersona(request);
            return ResponseEntity.ok(persona);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
