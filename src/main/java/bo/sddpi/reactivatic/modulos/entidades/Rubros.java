package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Rubros implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idrubro;
    private String rubro;
    private Long cantidad;
    private Boolean estado;
    private LocalDateTime created_at;
}
