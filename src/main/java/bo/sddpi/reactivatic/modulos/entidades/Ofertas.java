package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Ofertas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idoferta;
    private Long idempresa;
    private String tipooferta;
    private String oferta;
    private Boolean estado;
    private LocalDateTime created_at;

    private Empresas empresa;
}
