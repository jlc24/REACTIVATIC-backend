package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Submenus implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String ruta;
    private String icono;


}