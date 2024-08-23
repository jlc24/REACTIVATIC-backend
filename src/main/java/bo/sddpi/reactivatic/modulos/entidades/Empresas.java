package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Empresas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idempresa;
    private Long idrubro;
    private Long idsubrubro;
    private Long idmunicipio;
    private Long idlocalidad;
    private Long idrepresentante;
    private Long idasociacion;
    private String empresa;
    private String tipo;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;
    private String facebook;
    private String twitter;
    private String instagram;
    private String paginaweb;
    private String nform;
    private Integer registrosenasag;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String descripcion;
    private String nit;
    private Boolean bancamovil;
    private Date fechaapertura;
    private String servicios;
    private Integer capacidad;
    private String unidadmedida;
    private Integer motivo;
    private String otromotivo;
    private Boolean familiar;
    private Integer involucrados;
    private String otrosinvolucrados;
    private Integer trabajadores;
    private Integer participacion;
    private String capacitacion;
    private String zona;
    private String referencia;
    private String transporte;
    private Long idusuario;
    private Date fechareg;
    private String razonsocial;
    private Boolean estado;
    private LocalDateTime created_at;

    private Representantes representante;
    private Subrubros subrubro;
    private Localidades localidad;
    private Asociaciones asociacion;
    private Usuarios usuario;
}

