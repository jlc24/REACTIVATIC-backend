package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Subrubros implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idsubrubro;
    private Long idrubro;
    private String subrubro;
    private Boolean estado;
    private LocalDateTime created_at;
    
    private Rubros rubro;
}
