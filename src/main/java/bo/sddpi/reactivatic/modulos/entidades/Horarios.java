package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Horarios implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private LocalTime inicio;
    private LocalTime fin;
    private Integer duracion;
}
