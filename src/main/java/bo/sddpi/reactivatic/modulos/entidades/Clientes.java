package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Clientes implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idcliente;
    private Long idpersona;

    private Personas persona;
}
