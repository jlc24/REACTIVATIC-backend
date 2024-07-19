package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Beneficios implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long idbeneficios;
    private String beneficio;
    private Integer participacion;
    private Date fecha;
    private String lugar;
    private Boolean estado;
    private LocalDateTime created_at;
}
