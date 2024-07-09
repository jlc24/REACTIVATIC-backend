package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Categorias implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idcategoria;
    private String categoria;
    private String ruta;
    private String iconocategoria;
    private Long orden;
}