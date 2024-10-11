package bo.sddpi.reactivatic.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.sddpi.reactivatic.modulos.entidades.Beneficios;
import bo.sddpi.reactivatic.modulos.entidades.Beneficiosempresas;

public interface IPlanillasRep {

    byte[] planillaInscripcionXLS(List<Beneficiosempresas> datos) throws IOException;

    byte[] planillaInscripcionPDF(Beneficios dato) throws IOException;
    byte[] planillaRegistroPDF(List<Beneficiosempresas> datos, Beneficios dato) throws IOException;
    byte[] planillaAsistenciaPDF(List<Beneficiosempresas> datos, Beneficios dato) throws IOException;
}
