package bo.sddpi.reactivatic.modulos.servicios;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ISubirarchivosServ {

    public void cargarimagen(Long id, MultipartFile archivo);

    public Resource descargarimagen(String archivo);

    public void uploadimagen(Long id, MultipartFile archivo, String tipo);

    public List<Map<String, String>> downloadimagen(Long id, String tipo);

    public void cargarimagenproducto(Long id, MultipartFile archivo);

    public void eliminarimagenproducto(Long id);

    public void redimensionarproducto(Long id);

    public void fusionarproducto(Long id);

    public Resource descargarimagenproducto(String archivo);

    public void cargarimagenempresa(Long id, MultipartFile archivo);

    public void redimensionarempresa(Long id);

    public Resource descargarimagenempresa(String archivo);

}