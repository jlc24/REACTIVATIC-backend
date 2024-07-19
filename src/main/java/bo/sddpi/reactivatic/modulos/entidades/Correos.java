package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Correos implements Serializable {

    private static final long serialVersionUID = 1L;

    private String correo;
    private String contenido;
    private String tema;
}
