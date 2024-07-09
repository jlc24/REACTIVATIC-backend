package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Reportes implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;
    private String entidad;
    private Integer cantidad;

}
