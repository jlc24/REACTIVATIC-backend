package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Empresas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idempresa;
    private Long idsubrubro;
    private Long idlocalidad;
    private Long idrepresentante;
    private Long idasociacion;
    private String empresa;
    private String descripcion;
    private String tipo;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;
    private String facebook;
    private String twitter;
    private String instagram;
    private String paginaweb;
    private String otro;
    private Integer registrosenasag;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Representantes representante;
    private Subrubros subrubro;
    private Localidades localidad;
    private Asociaciones asociacion;
}

