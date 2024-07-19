package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Parametros implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idparametro;
    private Long idusuario;
    private String parametro;
    private String valor;
    private Usuarios usuario;
}