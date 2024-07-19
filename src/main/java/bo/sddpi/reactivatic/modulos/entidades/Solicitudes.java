package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class Solicitudes implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idsolicitud;
    private Long idempresa;
    private Long idcliente;
    private String solicitud;
    private LocalDate fecha;
    private LocalTime hora;
    private Boolean estado;
    
    private Empresas empresa;
    private Clientes cliente;
    private List<Solicitudesproductos> solicitudproductos;
}
