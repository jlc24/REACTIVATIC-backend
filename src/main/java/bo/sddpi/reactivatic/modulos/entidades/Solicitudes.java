package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Solicitudes implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idsolicitud;
    private Long idcliente;
    private String solicitud;
    private Long idempresa;
    private Integer cantidadProductos;
    private Boolean estado;
    private LocalDateTime created_at;
    
    private Clientes cliente;
    private Empresas empresa;
    private List<Solicitudesproductos> solicitudproductos;
}
