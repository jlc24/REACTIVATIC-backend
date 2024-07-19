package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Usuariosroles implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idusuariorol;
    private Long idusuario;
    private Long idrol;

    private Usuarios usuario;
    private Roles rol;

}