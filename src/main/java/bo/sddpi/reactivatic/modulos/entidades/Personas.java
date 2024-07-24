package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Personas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idpersona;
    private Long idtipogenero;
    private String primerapellido;
    private String segundoapellido;
    private String primernombre;
    //private String segundonombre;
    //private LocalDate fechanacimiento;
    private String dip;
    private String complementario;
    private Long idtipodocumento;
    private Long idtipoextension;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;
    private Integer formacion;
    private Integer estadocivil;
    private Integer hijos;
    private Boolean estado;
    private LocalDateTime created_at;

    private Tiposgeneros tipogenero;
    private Tiposdocumentos tipodocumento;
    private Tiposextensiones tipoextension;

    private Usuarios usuario;
    private Roles rol;
}
