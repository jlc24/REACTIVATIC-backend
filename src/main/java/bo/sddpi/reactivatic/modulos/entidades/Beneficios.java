package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Beneficios implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long idbeneficio;
    private String beneficio;
    private String descripcion;
    private Long idtipobeneficio;
    private Integer mesas;
    private Integer duracion;
    private Long idmunicipio;
    private String direccion;
    private LocalDateTime fechainicio;
    private LocalDateTime fechafin;
    private Long idcapacitador;
    private Integer capacidad;
    private Long idusuario;
    private Boolean estado;
    private LocalDateTime created_at;

    private Tiposbeneficios tipobeneficio;
    private Municipios municipio;
    private Usuarios capacitador;
    private Usuarios usuario;
}
