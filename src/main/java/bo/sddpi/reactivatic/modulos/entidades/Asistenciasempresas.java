package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Asistenciasempresas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idasistenciaempresa;
    private Long idasistencia;
    private Long idbeneficioempresa;
    private Boolean asistencia;
    private LocalDate fecha;
    private Boolean estado;
    private LocalDateTime created_at;

    private Asistencias asistencias;
    private Beneficiosempresas beneficiosempresa;
}
