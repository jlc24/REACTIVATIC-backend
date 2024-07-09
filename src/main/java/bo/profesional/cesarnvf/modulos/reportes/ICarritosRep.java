package bo.profesional.cesarnvf.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.profesional.cesarnvf.modulos.entidades.Carritos;

public interface ICarritosRep {

    byte[] datosXLS(List<Carritos> datos) throws IOException;

    byte[] datosPDF(List<Carritos> datos) throws IOException;

}
