package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Boolean estado;
    private LocalDateTime created_at;

    private Empresas empresa;
}
