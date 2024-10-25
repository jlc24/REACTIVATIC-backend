package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Asistencias implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idasistencia;
    private Long idbeneficio;
    private Integer dias;
    private Integer duraciondias;
    private Integer duracioncurso;
    private Boolean estado;
    private LocalDateTime created_at;

    private Beneficios beneficio;
}
