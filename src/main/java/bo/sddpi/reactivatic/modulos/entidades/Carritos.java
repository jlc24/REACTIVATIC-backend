package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Carritos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idcarrito;
    private Long idcliente;
    private Long idproducto;
    private String imagen;
    private Long idprecio;
    private Long idcolor;
    private Long idmaterial;
    private Long idtamano;
    private Long cantidad;
    private LocalDateTime created_at;
    
    private Productos producto;
    private Precios precio;
    private Colores color;
    private Materiales material;
    private Tamanos tamano;

}



