package bo.profesional.cesarnvf.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Enlaces implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idenlace;
    private Long idcategoria;
    private String enlace;
    private String ruta;
    private String iconoenlace;
    private Long orden;
    private Categorias categoria;

}