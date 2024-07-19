package bo.sddpi.reactivatic.modulos.servicios;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ISubirarchivosServ {

    public void cargarimagen(Long id, MultipartFile archivo);

    public Resource descargarimagen(String archivo);

    public void cargarimagenproducto(Long id, MultipartFile archivo);

    public void eliminarimagenproducto(Long id);

    public void redimensionarproducto(Long id);

    public void fusionarproducto(Long id);

    public Resource descargarimagenproducto(String archivo);

    public void cargarimagenempresa(Long id, MultipartFile archivo);

    public void redimensionarempresa(Long id);

    public Resource descargarimagenempresa(String archivo);

}