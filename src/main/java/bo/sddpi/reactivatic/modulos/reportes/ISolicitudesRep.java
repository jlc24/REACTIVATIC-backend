package bo.sddpi.reactivatic.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.sddpi.reactivatic.modulos.entidades.Solicitudes;

public interface ISolicitudesRep {

    byte[] datosXLS(List<Solicitudes> datos) throws IOException;

    byte[] datosPDF(List<Solicitudes> datos) throws IOException;

}
