package bo.profesional.cesarnvf.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.profesional.cesarnvf.modulos.entidades.Usuarios;

public interface IUsuariosRep {

    byte[] datosXLS(List<Usuarios> datos) throws IOException;

    byte[] datosPDF(List<Usuarios> datos) throws IOException;

}