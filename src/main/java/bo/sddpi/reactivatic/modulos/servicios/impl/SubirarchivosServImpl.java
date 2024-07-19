package bo.sddpi.reactivatic.modulos.servicios.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;

@Service
public class SubirarchivosServImpl implements ISubirarchivosServ {

    @Value("${path.archivos}")
    private String rutaprincipal;

    private Path rutaperfiles = Paths.get(rutaprincipal,"perfiles");

    private Path rutaperfilesempresas = Paths.get(rutaprincipal,"empresas");

    @Override
    public void cargarimagen(Long id, MultipartFile archivo) {
        try {
            Files.deleteIfExists(this.rutaperfiles.resolve(id.toString() + ".png"));
            Files.copy(archivo.getInputStream(), this.rutaperfiles.resolve(id.toString() + ".png"));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource descargarimagen(String archivo) {
        try {
            Path file = rutaperfiles.resolve(archivo + ".png");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void cargarimagenproducto(Long id, MultipartFile archivo) {
        try {
            Path archivodestino = Paths.get(rutaprincipal+"/productos").resolve(id.toString() + ".png").toAbsolutePath();
            Files.copy(archivo.getInputStream(), archivodestino);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public void eliminarimagenproducto(Long id) {
        try {
            Path file = Paths.get(rutaprincipal+"/productos").resolve(id.toString() + ".png");
            Files.deleteIfExists(file);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public void redimensionarproducto(Long id) {
        int ancho = 300;
        int alto = 300;
        try {
            fredimensionarp(id, rutaprincipal+"/productos", ancho, alto);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo redimensionar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public void fusionarproducto(Long id) {
        try {
            ffusionarp(id, rutaprincipal+"/productos");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo redimensionar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource descargarimagenproducto(String archivo) {
        try {
            Path file = Paths.get(rutaprincipal+"/productos").resolve(archivo + ".png");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                file = Paths.get(rutaprincipal+"/productos").resolve("sinproducto.png");
                resource = new UrlResource(file.toUri());
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void cargarimagenempresa(Long id, MultipartFile archivo) {
        try {
            Path archivodestino = Paths.get(rutaprincipal+"/empresas").resolve(id.toString() + ".png").toAbsolutePath();
            Files.copy(archivo.getInputStream(), archivodestino);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource descargarimagenempresa(String archivo) {
        try {
            Path file = Paths.get(rutaprincipal+"/empresas").resolve(archivo + ".png");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                file = Paths.get(rutaprincipal+"/empresas").resolve("sinempresa.png");
                resource = new UrlResource(file.toUri());
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void redimensionarempresa(Long id) {
        int ancho = 300;
        int alto = 300;
        try {
            fredimensionarp(id, rutaprincipal+"/empresas", ancho, alto);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo redimensionar el archivo. Error: " + e.getMessage());
        }
    }

    public static void fredimensionarp(Long id, String rutaperfiles, int ancho, int alto) throws IOException {
        try {
            File entradaarchivo = new File(rutaperfiles + "/" + id.toString() + ".png");
            File salidaarchivo = new File(rutaperfiles + "/" + id.toString() + ".png");
            BufferedImage entradaImagen = ImageIO.read(entradaarchivo);
            BufferedImage salidaImagen = new BufferedImage(ancho, alto, entradaImagen.getType());
            Graphics2D g2d = salidaImagen.createGraphics();
            g2d.drawImage(entradaImagen, 0, 0, ancho, alto, null);
            g2d.dispose();
            ImageIO.write(salidaImagen, "png", salidaarchivo);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void ffusionarp(Long id, String rutaperfiles) throws IOException {
        try {
            File entradaarchivo = new File(rutaperfiles + "/" + id.toString() + ".png");
            File salidaarchivo = new File(rutaperfiles + "/" + id.toString() + ".png");
            File productoarchivo = new File(rutaperfiles + "/producto.png");
            BufferedImage entradaImagen = ImageIO.read(entradaarchivo);
            BufferedImage productoImagen =ImageIO.read(productoarchivo);
            int w = Math.max(entradaImagen.getWidth(), productoImagen.getWidth());
            int h = Math.max(entradaImagen.getHeight(), productoImagen.getHeight());
            BufferedImage fusionado = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = fusionado.createGraphics();
            g.drawImage(entradaImagen, 0, 0, null);
            g.drawImage(productoImagen, 0, 0, null);
            g.dispose();
            ImageIO.write(fusionado, "png", salidaarchivo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
