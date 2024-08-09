package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Enlaces implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idenlace;
    private Long idcategoria;
    private String enlace;
    private String ruta;
    private String iconoenlace;
    private Long orden;
    private Boolean estado;
    private LocalDateTime created_at;
    
    private Categorias categoria;
}