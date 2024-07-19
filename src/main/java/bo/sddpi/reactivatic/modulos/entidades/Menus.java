package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Menus implements Serializable {

	private static final long serialVersionUID = 1L;

    private String nombre;
    private String ruta;
    private String icono;
    private List<Submenus> submenu;

}