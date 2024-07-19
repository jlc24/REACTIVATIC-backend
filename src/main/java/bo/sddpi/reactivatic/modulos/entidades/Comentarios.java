package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comentarios implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long idcomentario;
    private String comentario;
    private Long idcliente;
    private Integer valoracion;
    private Long idproducto;
    private Boolean estado;
    private LocalDateTime created_at;

    private Clientes cliente;
    private Productos producto;
}
