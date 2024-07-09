package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tiposdocumentos implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idtipodocumento;
    private String tipodocumento;

}