package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Solicitudesproductos implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idsolicitudproducto;
    private Long idsolicitud;
    private Long idproducto;
    private Long cantidad;
    private BigDecimal precioventa;
    private BigDecimal total;
    private Productos producto;
}
