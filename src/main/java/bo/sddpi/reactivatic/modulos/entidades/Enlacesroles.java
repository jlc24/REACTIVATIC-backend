package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Enlacesroles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idenlacerol;
    private Long idenlace;
    private Long idrol;
    private Boolean estado;
    
    private Enlaces enlace;
    private Roles rol;
}