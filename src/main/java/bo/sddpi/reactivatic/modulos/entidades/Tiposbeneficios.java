package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Tiposbeneficios implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idtipobeneficio;
    private String tipobeneficio;
    private Boolean estado;
    private LocalDateTime created_at ;
}
