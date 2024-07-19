package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Procesar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idcliente;
    private String nombre;
    private Integer celular;
    private String correo;
    private String clave;
}
