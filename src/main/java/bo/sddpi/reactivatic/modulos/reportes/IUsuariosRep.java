package bo.sddpi.reactivatic.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.sddpi.reactivatic.modulos.entidades.Usuarios;

public interface IUsuariosRep {

    byte[] datosXLS(List<Usuarios> datos) throws IOException;

    byte[] datosPDF(List<Usuarios> datos) throws IOException;

}