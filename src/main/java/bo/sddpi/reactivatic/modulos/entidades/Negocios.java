package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Negocios implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idnegocio;
    private Long idbenficio;
    private Long idbeneficioempresa;
    private Long idpersona;
    private LocalTime horainicio;
    private LocalTime horafin;
    private Long duracion;
    private Long mesa;
    private BigDecimal cantidad;
    private Integer estadoempresa;
    private Integer estadopersona;
    private LocalDateTime created_at;

    private Beneficios beneficio;
    private Beneficiosempresas beneficioempresa;
    private Personas persona;
}
