package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Atributos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idatributo;
    private Long idproducto;
    private String atributo;
    private String detalle;
    private Boolean estado;
    private LocalDateTime created_at;

    private Productos producto;
}
