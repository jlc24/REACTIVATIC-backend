package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Tiposgeneros implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idtipogenero;
    private String tipogenero;
    private Boolean estado;
    private LocalDateTime created_at;
}