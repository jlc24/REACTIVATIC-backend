package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Carritos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idcarrito;
    private Long idcliente;
    private Long idproducto;
    private Long cantidad;
    private LocalDate fecha;
    private Productos producto;

}



