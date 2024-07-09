package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Representantes implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idrepresentante;
    private Long idpersona;
    private String representante;
    private Personas persona;

}
