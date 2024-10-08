package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Colores implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idcolor;
    private Long idproducto;
    private String color;
    private String codigo;
    private Boolean estado;
    private LocalDateTime created_at;

    private Productos producto;
}
