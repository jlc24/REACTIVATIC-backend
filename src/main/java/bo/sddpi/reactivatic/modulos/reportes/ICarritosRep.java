package bo.sddpi.reactivatic.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.sddpi.reactivatic.modulos.entidades.Carritos;

public interface ICarritosRep {

    byte[] datosXLS(List<Carritos> datos) throws IOException;

    byte[] datosPDF(List<Carritos> datos) throws IOException;

}
