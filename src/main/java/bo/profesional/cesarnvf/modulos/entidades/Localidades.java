package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Localidades implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idlocalidad;
    private Long idmunicipio;
    private String localidad;
    private Municipios municipio;
}
