package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Enlacesroles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idenlacerol;
    private Long idenlace;
    private Long idrol;
    private Enlaces enlace;
    private Roles rol;
}