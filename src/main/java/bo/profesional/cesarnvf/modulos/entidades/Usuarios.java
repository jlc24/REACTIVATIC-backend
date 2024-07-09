package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Usuarios implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idusuario;
    private Long idpersona;
    private String usuario;
    private String clave;
    private Personas persona;
    private Boolean estado;
    private Personas personas;

}