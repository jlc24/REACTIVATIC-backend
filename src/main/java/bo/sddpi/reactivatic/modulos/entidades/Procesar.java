package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Procesar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idcliente;
    private String primerapellido;
    private String segundoapellido;
    private String primernombre;
    private String usuario;
    private Integer celular;
    private String direccion;
    private String correo;
    private String clave;
}
