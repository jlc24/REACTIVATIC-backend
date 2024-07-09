package bo.profesional.cesarnvf.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.profesional.cesarnvf.modulos.entidades.Solicitudes;

public interface ISolicitudesRep {

    byte[] datosXLS(List<Solicitudes> datos) throws IOException;

    byte[] datosPDF(List<Solicitudes> datos) throws IOException;

}
