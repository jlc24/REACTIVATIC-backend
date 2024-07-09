package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Subrubros implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idsubrubro;
    private Long idrubro;
    private String subrubro;
    private Rubros rubro;
}
