package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Localidades implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idlocalidad;
    private Long idmunicipio;
    private String localidad;
    private Boolean estado;
    private LocalDateTime created_at;

    private Municipios municipio;
}
