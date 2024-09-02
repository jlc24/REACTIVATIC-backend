package bo.sddpi.reactivatic.modulos.servicios.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;

@Service
public class SubirarchivosServImpl implements ISubirarchivosServ {

    @Value("${path.archivos}")
    private String rutaprincipal;

    @Value("${path.archivos.windows}")
    private String rutaWindows;

    @Value("${path.archivos.linux}")
    private String rutaLinux;

    private Path rutaUsuarios;
    private Path rutaRepresentantes;
    private Path rutaEmpresas;
    private Path rutaProductos;

    @PostConstruct
    public void init() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            rutaprincipal = rutaWindows;
        } else {
            rutaprincipal = rutaLinux;
        }
        rutaUsuarios = Paths.get(rutaprincipal, "/imagenes/usuarios");
        rutaRepresentantes = Paths.get(rutaprincipal, "/imagenes/representantes");
        rutaEmpresas = Paths.get(rutaprincipal, "/imagenes/empresas");
        rutaProductos = Paths.get(rutaprincipal, "/imagenes/productos");

        try {
            Files.createDirectories(rutaUsuarios);
            Files.createDirectories(rutaRepresentantes);
            Files.createDirectories(rutaEmpresas);
            Files.createDirectories(rutaProductos);
            boolean canRead = Files.isReadable(rutaUsuarios);
            boolean canWrite = Files.isWritable(rutaUsuarios);

            System.out.println("Permisos para el directorio:");
            System.out.println("Readable: " + canRead);
            System.out.println("Writable: " + canWrite);

            if (!os.contains("win")) {
                Files.setPosixFilePermissions(rutaUsuarios, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaRepresentantes, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaEmpresas, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaProductos, PosixFilePermissions.fromString("rwxr-xr-x"));
            }

        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear los directorios de archivos. Error: " + e.getMessage());
        }

        try {
            Resource recursoArchivo = new ClassPathResource("fondologo.png");
            Resource recursoArchivoSin = new ClassPathResource("sin.png");
            File archivoFondologo = recursoArchivo.getFile();
            File archivoSin = recursoArchivoSin.getFile();
            
            Files.copy(archivoFondologo.toPath(), rutaUsuarios.resolve("usuarios.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoFondologo.toPath(), rutaRepresentantes.resolve("representantes.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoFondologo.toPath(), rutaEmpresas.resolve("empresas.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoFondologo.toPath(), rutaProductos.resolve("productos.png"), StandardCopyOption.REPLACE_EXISTING);

            Files.copy(archivoSin.toPath(), rutaUsuarios.resolve("sinusuario.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSin.toPath(), rutaRepresentantes.resolve("sinrepresentante.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSin.toPath(), rutaEmpresas.resolve("sinempresa.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSin.toPath(), rutaProductos.resolve("sinproducto.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo copiar el archivo de logo. Error: " + e.getMessage());
        }
    }

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
    public void uploadimagen(Long id, MultipartFile archivo, String tipo) {
        Path rutaDestino;

        switch (tipo.toLowerCase()) {
            case "usuarios":
                rutaDestino = Paths.get(rutaprincipal, "usuarios", id.toString() + ".png");
                break;
            case "representantes":
                Path directorioRepresentantes = Paths.get(rutaprincipal, "representantes", id.toString());
                try {
                    Files.createDirectories(directorioRepresentantes);
                    rutaDestino = directorioRepresentantes.resolve(archivo.getOriginalFilename());
                } catch (IOException e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "empresas":
                rutaDestino = Paths.get(rutaprincipal, "empresas", id.toString() + "_logo.png");
                break;
            case "producto":
                Path directorioProductos = Paths.get(rutaprincipal, "productos", id.toString());
                try {
                    Files.createDirectories(directorioProductos);
                    rutaDestino = directorioProductos.resolve(archivo.getOriginalFilename());
                } catch (IOException e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                return;
            default:
                throw new IllegalArgumentException("Tipo de archivo no reconocido: " + tipo);
        }

        try {
            Files.deleteIfExists(rutaDestino);
            Files.copy(archivo.getInputStream(), rutaDestino);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public List<Resource> downloadimagen(Long id, String tipo) {
        Path rutaDestino;

        switch (tipo.toLowerCase()) {
            case "usuario":
                rutaDestino = Paths.get(rutaprincipal, "usuarios", id.toString() + ".png");
                return listSingleFile(rutaDestino);
            case "representante":
                rutaDestino = Paths.get(rutaprincipal, "representantes", id.toString());
                return listFilesInDirectory(rutaDestino);
            case "empresa":
                rutaDestino = Paths.get(rutaprincipal, "empresas", id.toString() + "_logo.png");
                return listSingleFile(rutaDestino);
            case "producto":
                rutaDestino = Paths.get(rutaprincipal, "productos", id.toString());
                return listFilesInDirectory(rutaDestino);
            default:
                throw new IllegalArgumentException("Tipo de archivo no reconocido: " + tipo);
        }
    }

    public void eliminarimagen(Long id, String archivo, String tipo ) {
        Path rutaArchivo;
        Path directorio;
    
        switch (tipo) {
            case "usuarios":
                rutaArchivo = Paths.get(rutaprincipal, "usuarios", id.toString() + ".png");
                break;
            case "representantes":
                directorio = Paths.get(rutaprincipal, "representantes", id.toString());
                rutaArchivo = buscarArchivoEnDirectorio(directorio, archivo + ".png");
                break;
            case "empresas":
                rutaArchivo = Paths.get(rutaprincipal, "empresas", id.toString() + "_logo.png");
                break;
            case "productos":
                directorio = Paths.get(rutaprincipal, "productos", id.toString());
                rutaArchivo = buscarArchivoEnDirectorio(directorio, archivo + ".png");
                break;
            default:
                throw new IllegalArgumentException("Tipo de archivo no válido");
        }
    
        try {
            Files.deleteIfExists(rutaArchivo);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar el archivo. Error: " + e.getMessage());
        }
    }

    private Path buscarArchivoEnDirectorio(Path directorio, String nombreArchivo) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorio, nombreArchivo)) {
            for (Path archivo : stream) {
                return archivo;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al buscar el archivo en el directorio. Error: " + e.getMessage());
        }
        return null;
    }

    private List<Resource> listSingleFile(Path rutaDestino) {
        List<Resource> resources = new ArrayList<>();
        try {
            if (Files.exists(rutaDestino) && Files.isRegularFile(rutaDestino)) {
                resources.add(new UrlResource(rutaDestino.toUri()));
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
        return resources;
    }

    private List<Resource> listFilesInDirectory(Path directorio) {
        List<Resource> resources = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorio)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    resources.add(new UrlResource(file.toUri()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al listar archivos en el directorio. Error: " + e.getMessage());
        }
        return resources;
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
        int ancho = 600;
        int alto = 600;
        try {
            fscaleImage(id, rutaprincipal+"/productos", ancho, alto);
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
        // try {
        //     Path file = Paths.get(rutaProductos).resolve(archivo + ".png");
        //     Resource resource = new UrlResource(file.toUri());
        //     if (resource.exists() || resource.isReadable()) {
        //         return resource;
        //     } else {
        //         file = Paths.get(rutaProductos).resolve("sinproducto.png");
        //         resource = new UrlResource(file.toUri());
        //         return resource;
        //     }
        // } catch (MalformedURLException e) {
        //     throw new RuntimeException("Error: " + e.getMessage());
        // }
        try {
            // Supone que `rutaProductos` ya es un Path
            Path file = rutaProductos.resolve(archivo + ".png");
    
            // Crear el recurso desde el Path
            Resource resource = new UrlResource(file.toUri());
    
            // Verificar si el recurso existe y es legible
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                // Si el archivo no existe, devolver una imagen por defecto
                file = rutaProductos.resolve("sinproducto.png");
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
        // try {
        //     Path file = Paths.get(rutaprincipal).resolve(archivo + ".png");
        //     Resource resource = new UrlResource(file.toUri());
        //     if (resource.exists() || resource.isReadable()) {
        //         return resource;
        //     } else {
        //         file = Paths.get(rutaprincipal).resolve("sinempresa.png");
        //         resource = new UrlResource(file.toUri());
        //         return resource;
        //     }
        // } catch (MalformedURLException e) {
        //     throw new RuntimeException("Error: " + e.getMessage());
        // }
        try {
            // Supone que `rutaProductos` ya es un Path
            Path file = rutaEmpresas.resolve(archivo + ".png");
    
            // Crear el recurso desde el Path
            Resource resource = new UrlResource(file.toUri());
    
            // Verificar si el recurso existe y es legible
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                // Si el archivo no existe, devolver una imagen por defecto
                file = rutaEmpresas.resolve("sinempresa.png");
                resource = new UrlResource(file.toUri());
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void redimensionarempresa(Long id) {
        int ancho = 600;
        int alto = 600;
        try {
            fscaleImage(id, rutaprincipal+"/empresas", ancho, alto);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo redimensionar el archivo. Error: " + e.getMessage());
        }
    }

    public static void fscaleImage(Long id, String rutaperfiles, int maxAncho, int maxAlto) throws IOException {
        try {
            File entradaarchivo = new File(rutaperfiles + "/" + id.toString() + ".png");
            File salidaarchivo = new File(rutaperfiles + "/" + id.toString() + ".png");
            BufferedImage entradaImagen = ImageIO.read(entradaarchivo);

            int anchoOriginal = entradaImagen.getWidth();
            int altoOriginal = entradaImagen.getHeight();

            double ratioAncho = (double) maxAncho / anchoOriginal;
            double ratioAlto = (double) maxAlto / altoOriginal;
            double ratioFinal = Math.min(ratioAncho, ratioAlto);

            int nuevoAncho = (int) (anchoOriginal * ratioFinal);
            int nuevoAlto = (int) (altoOriginal * ratioFinal);

            BufferedImage salidaImagen = new BufferedImage(nuevoAncho, nuevoAlto, entradaImagen.getType());
            Graphics2D g2d = salidaImagen.createGraphics();
            g2d.drawImage(entradaImagen, 0, 0, nuevoAncho, nuevoAlto, null);
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
