package co.edu.javeriana.as.personapp.controller;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

    @Autowired
    private EstudioInputAdapterRest estudioInputAdapterRest;

    @Operation(summary = "Obtener historial de estudios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudios retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estudios no encontrados")
    })
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EstudioResponse> historial(
            @Parameter(description = "Base de datos donde se quiere buscar", required = true, example = "1")
            @PathVariable String database) {
        log.info("Into estudios REST API");
        List<EstudioResponse> estudios = estudioInputAdapterRest.historial(database.toUpperCase());
        if (estudios.isEmpty()) {
            throw new RuntimeException("Estudios no encontrados para la base de datos: " + database);
        }
        return estudios;
    }

    @Operation(summary = "Crear estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse crearEstudio(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Solicitud de creación de estudio")
            @RequestBody EstudioRequest estudioRequest) {
        log.info("Into crearEstudio REST API");
        return estudioInputAdapterRest.createStudy(estudioRequest);
    }

    @Operation(summary = "Buscar un estudio específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudio encontrado"),
            @ApiResponse(responseCode = "404", description = "Estudio no encontrado")
    })
    @GetMapping(path = "/find/{database}/{id_person}/{id_profession}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse findOne(
            @Parameter(description = "Base de datos donde se busca", required = true) @PathVariable String database,
            @Parameter(description = "ID de la persona", required = true) @PathVariable String id_person,
            @Parameter(description = "ID de la profesión", required = true) @PathVariable String id_profession) {
        log.info("Into findOne REST API");
        EstudioResponse estudioResponse = estudioInputAdapterRest.findOne(database, id_person, id_profession);
        if (estudioResponse == null) {
            throw new RuntimeException("Estudio no encontrado para la persona ID: " + id_person + " y profesión ID: " + id_profession);
        }
        return estudioResponse;
    }

    @Operation(summary = "Eliminar un estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estudio no encontrado")
    })
    @DeleteMapping(path = "/{database}/{id_person}/{id_profession}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse deleteStudio(
            @Parameter(description = "Base de datos donde se quiere buscar", required = true) @PathVariable String database,
            @Parameter(description = "ID de la persona", required = true) @PathVariable String id_person,
            @Parameter(description = "ID de la profesión", required = true) @PathVariable String id_profession) {
        log.info("Into deleteStudio REST API");
        return estudioInputAdapterRest.deleteStudio(database, id_person, id_profession);
    }

    @Operation(summary = "Actualizar un estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudio actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse editStudio(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Solicitud de actualización de estudio")
            @RequestBody EstudioRequest estudioRequest) {
        log.info("Into editStudio REST API");
        return estudioInputAdapterRest.editStudio(estudioRequest);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRuntimeException(RuntimeException e) {
        log.error("Error: " + e.getMessage());
        return e.getMessage();
    }
}
