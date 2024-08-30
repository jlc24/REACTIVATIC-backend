package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Materiales implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idmaterial;
    private Long idproducto;
    private String material;
    private boolean estado;
    private LocalDateTime created_at;

    private Productos producto;
}
