package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Ofertas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idoferta;
    private Long idproducto;
    private String oferta;
    private LocalDateTime duracion;
    private Boolean estado;
    private LocalDateTime created_at;

    private Productos producto;
}
