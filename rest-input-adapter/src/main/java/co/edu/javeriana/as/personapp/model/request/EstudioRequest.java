package co.edu.javeriana.as.personapp.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioRequest {
    private String id_pro;
    private String id_cc;
    private String univer;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private String database;
}