package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.EstudioRequest;

import java.time.LocalDate;

public class EstudioResponse extends EstudioRequest {

    private String status;

    public EstudioResponse(String id_pro, String id_cc, LocalDate date, String univer, String database, String status) {
        super(id_pro, id_cc, univer, date, database);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

