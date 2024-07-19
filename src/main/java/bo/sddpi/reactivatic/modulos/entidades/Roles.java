package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Roles implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idrol;
    private String rol;
    private LocalDateTime created_at;

}