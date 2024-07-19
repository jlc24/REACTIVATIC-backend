package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Categorias implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idcategoria;
    private String categoria;
    private String ruta;
    private String iconocategoria;
    private Long orden;
    private Boolean estado;
    private LocalDateTime created_at;
}