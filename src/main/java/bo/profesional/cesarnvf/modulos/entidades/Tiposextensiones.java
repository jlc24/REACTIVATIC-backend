package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tiposextensiones implements Serializable{

	private static final long serialVersionUID = 1L;

    private Long idtipoextension;
    private String tipoextension;
    private String sigla;
}