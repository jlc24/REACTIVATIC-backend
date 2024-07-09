package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tiposgeneros implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idtipogenero;
    private String tipogenero;
}