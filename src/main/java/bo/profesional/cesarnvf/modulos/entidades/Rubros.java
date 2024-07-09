package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Rubros implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idrubro;
    private String rubro;
    private Long cantidad;
}
