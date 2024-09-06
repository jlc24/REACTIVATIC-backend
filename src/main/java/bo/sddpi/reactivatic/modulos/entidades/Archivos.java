package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Archivos implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idarchivo;
    private Long idusuario;
    private Long idpersona;
    private Long idempresa;
    private Long idproducto;
    private String filename;
    private String data;
    private String mimetype;
    private Boolean estado;
    private LocalDateTime created_at;

    private Productos producto;
}
