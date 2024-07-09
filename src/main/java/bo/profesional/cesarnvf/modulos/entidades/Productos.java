package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Productos implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idproducto;
    private Long idempresa;
    private String producto;
    private String descripcion;
    private BigDecimal preciocompra;
    private BigDecimal precioventa;
    private Long cantidad;
    private Empresas empresa;
}
