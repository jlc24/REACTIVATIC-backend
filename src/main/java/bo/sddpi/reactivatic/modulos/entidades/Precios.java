package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Precios implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idprecio;
    private Long idproducto;
    private BigDecimal precio;
    private Long idtamano;
    private Long idmaterial;
    private String cantidad;
    private Boolean estado;
    private LocalDateTime created_at;

    private Productos producto;
    private Tamanos tamano;
    private Materiales material;
}
