package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Tiposdocumentos implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idtipodocumento;
    private String tipodocumento;
    private String documento;
    private Boolean estado;
    private LocalDateTime created_at;

}