package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Asociaciones implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idasociacion;
    private String asociacion;
    private String descripcion;
    private LocalDate fechacreacion;
    private String representantelegal;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;

}
