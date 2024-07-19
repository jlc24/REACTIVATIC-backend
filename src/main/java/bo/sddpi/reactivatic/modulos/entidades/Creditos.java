package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Creditos implements Serializable{

    private static final long serialVersionUID = 1L;

    private String autor;
    private String profesion;
    private String cargo;
    private String appnombre;
    private LocalDate fechacreacion;
    private String institucion;
    private String unidad;
    private String version;


}