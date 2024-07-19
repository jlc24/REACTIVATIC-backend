package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Municipios implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idmunicipio;
    private String municipio;
    private Boolean estado;
    private LocalDateTime created_at;
}
