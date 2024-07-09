package bo.profesional.cesarnvf.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.profesional.cesarnvf.modulos.entidades.Empresas;

public interface IEmpresasRep {

    byte[] datosXLS(List<Empresas> datos) throws IOException;

    byte[] datosPDF(List<Empresas> datos) throws IOException;

}
