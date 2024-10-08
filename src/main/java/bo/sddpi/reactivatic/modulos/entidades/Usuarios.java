package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Usuarios implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idusuario;
    private Long idpersona;
    private String usuario;
    private String clave;
    private Boolean estado;
    private Long idcargo;
    private LocalDateTime created_at;
    
    private Personas persona;
    private Roles rol;
    private Cargos cargo;

}