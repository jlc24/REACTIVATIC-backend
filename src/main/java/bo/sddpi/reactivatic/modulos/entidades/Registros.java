package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Registros implements Serializable{

    private static final long serialVersionUID = 1L;

    private String primernombre;
    private String segundonombre;
    private String primerapellido;
    private String segundoapellido;
    private String dip;
    private String celular;
    private String correo;
}

