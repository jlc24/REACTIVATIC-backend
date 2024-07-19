package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Tiposextensiones implements Serializable{

	private static final long serialVersionUID = 1L;

    private Long idtipoextension;
    private String tipoextension;
    private String sigla;
    private Boolean estado;
    private LocalDateTime created_At;
}