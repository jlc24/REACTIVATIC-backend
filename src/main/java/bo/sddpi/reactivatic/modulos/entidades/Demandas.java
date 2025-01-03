package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Demandas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long iddemanda;
    private Long idempresa;
    private String tipodemanda;
    private String demanda;
    private Boolean estado;
    private LocalDateTime created_at;

    private Empresas empresa;
}
