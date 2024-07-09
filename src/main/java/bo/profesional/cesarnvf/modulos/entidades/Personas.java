package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Personas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idpersona;
    private Long idtipogenero;
    private String primerapellido;
    private String segundoapellido;
    private String primernombre;
    private String segundonombre;
    private LocalDate fechanacimiento;
    private String dip;
    private String numerocomplementario;
    private Long idtipodocumento;
    private Long idtipoextension;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;
    private Boolean estado;
    private Tiposgeneros tipogenero;
    private Tiposdocumentos tipodocumento;
    private Tiposextensiones tipoextension;

}
