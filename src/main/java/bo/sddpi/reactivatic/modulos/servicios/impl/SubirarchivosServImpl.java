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
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            Resource recursoArchivoLogo = new ClassPathResource("fondologo.png");
            Resource recursoArchivoSinCarnet = new ClassPathResource("sincarnet.png");
            Resource recursoArchivoSinUsuario = new ClassPathResource("sinusuario.png");
            Resource recursoArchivoSinImagen = new ClassPathResource("sinimagen.png");
            File archivoFondologo = recursoArchivoLogo.getFile();
            File archivoSinCarnet = recursoArchivoSinCarnet.getFile();
            File archivoSinUsuario = recursoArchivoSinUsuario.getFile();
            File archivoSinImagen = recursoArchivoSinImagen.getFile();
            
            Files.copy(archivoFondologo.toPath(), rutaProductos.resolve("productos.png"), StandardCopyOption.REPLACE_EXISTING);

            Files.copy(archivoSinUsuario.toPath(), rutaUsuarios.resolve("sinusuario.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSinCarnet.toPath(), rutaRepresentantes.resolve("carnet_anverso.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSinCarnet.toPath(), rutaRepresentantes.resolve("carnet_reverso.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSinImagen.toPath(), rutaEmpresas.resolve("sinempresa.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(archivoSinImagen.toPath(), rutaProductos.resolve("sinproducto.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo copiar el archivo de logo. Error: " + e.getMessage());
        }
    }

    private Path rutaperfiles = Paths.get(rutaprincipal,"perfiles");

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
                rutaDestino = Paths.get(rutaUsuarios.toString(), id.toString() + ".png");
                break;
            case "empresas":
                rutaDestino = Paths.get(rutaEmpresas.toString(), id.toString() + "_logo.png");
                break;
            case "repanverso":
                Path directorioRepAnverso = Paths.get(rutaRepresentantes.toString(), id.toString());
                try {
                    Files.createDirectories(directorioRepAnverso);
                    rutaDestino = directorioRepAnverso.resolve("carnet_anverso.png");
                } catch (IOException e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "repreverso":
                Path directorioRepReverso = Paths.get(rutaRepresentantes.toString(), id.toString());
                try {
                    Files.createDirectories(directorioRepReverso);
                    rutaDestino = directorioRepReverso.resolve("carnet_reverso.png");
                } catch (IOException e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "productos":
                Path directorioProductos = Paths.get(rutaProductos.toString(), id.toString());
                try {
                    Files.createDirectories(directorioProductos);
                    rutaDestino = directorioProductos.resolve(archivo.getOriginalFilename());
                } catch (IOException e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de archivo no reconocido: " + tipo);
        }

        try {
            Files.deleteIfExists(rutaDestino);
            Files.write(rutaDestino, archivo.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> downloadimagen(Long id, String tipo) {
        List<Map<String, String>> imagenes = new ArrayList<>();

        Path rutaEspecifica;
        switch (tipo.toLowerCase()) {
            case "usuarios":
                rutaEspecifica = rutaUsuarios.resolve(id.toString() + ".png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaUsuarios.resolve("sinusuario.png")));
                }
                break;
            case "empresas":
                rutaEspecifica = rutaEmpresas.resolve(id.toString() + "_logo.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaEmpresas.resolve("sinempresa.png")));
                }
                break;
            case "repanverso":
                Path directorioRepAnverso = rutaRepresentantes.resolve(id.toString());
                rutaEspecifica = directorioRepAnverso.resolve("carnet_anverso.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaRepresentantes.resolve("carnet_anverso.png")));
                }
                break;
            case "repreverso":
                Path directorioRepReverso = rutaRepresentantes.resolve(id.toString());
                rutaEspecifica = directorioRepReverso.resolve("carnet_reverso.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaRepresentantes.resolve("carnet_reverso.png")));
                }
                break;
            case "productos":
                Path rutaProductosEspecifica = rutaProductos.resolve(id.toString());
                if (!Files.exists(rutaProductosEspecifica)) {
                    imagenes.add(convertToBase64(rutaProductos.resolve("sinproducto.png")));
                } else if (Files.exists(rutaProductosEspecifica) && Files.isDirectory(rutaProductosEspecifica)) {
                    try (Stream<Path> paths = Files.walk(rutaProductosEspecifica)) {
                        // paths.filter(Files::isRegularFile)
                        //     .forEach(path -> imagenes.add(convertToBase64(path)));
                        List<Path> imagenesEncontradas = paths.filter(Files::isRegularFile).collect(Collectors.toList());
            
                        if (imagenesEncontradas.isEmpty()) {
                            // Si la ruta existe, pero no hay imágenes
                            imagenes.add(convertToBase64(rutaProductos.resolve("sinproducto.png")));
                        }
                        
                        // Convertir las imágenes encontradas a Base64
                        imagenesEncontradas.forEach(path -> imagenes.add(convertToBase64(path)));
                    } catch (IOException e) {
                        throw new RuntimeException("Error al leer los archivos de productos: " + e.getMessage());
                    }
                } else {
                    imagenes.add(convertToBase64(rutaProductos.resolve("sinproducto.png")));
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de archivo no reconocido");
        }
        return imagenes;
    }

    private Map<String, String> convertToBase64(Path path) {
        Map<String, String> imagen = new HashMap<>();
        try {
            byte[] bytes = Files.readAllBytes(path);
            String base64 = Base64.getEncoder().encodeToString(bytes);
            imagen.put("filename", path.getFileName().toString());
            imagen.put("mimeType", Files.probeContentType(path));
            imagen.put("data", base64);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage());
        }
        return imagen;
    }

    public void eliminarimagen(Long id, String archivo, String tipo ) {
        Path rutaArchivo;
        Path directorio;
    
        switch (tipo.toLowerCase()) {
            case "usuarios":
                rutaArchivo = Paths.get(rutaUsuarios.toString(), id.toString() + ".png");
                break;
            case "empresas":
                rutaArchivo = Paths.get(rutaEmpresas.toString(), id.toString() + "_logo.png");
                break;
            case "repanverso":
                rutaArchivo = Paths.get(rutaRepresentantes.toString(), id.toString(), "carnet_anverso.png");
                //rutaArchivo = buscarArchivoEnDirectorio(directorio, archivo + ".png");
                break;
            case "repreverso":
                rutaArchivo = Paths.get(rutaRepresentantes.toString(), id.toString(), "carnet_reverso.png");
                //rutaArchivo = buscarArchivoEnDirectorio(directorio, archivo + ".png");
                break;
            case "productos":
                directorio = Paths.get(rutaProductos.toString(), id.toString());
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
    public Resource descargarimagenproducto(String id) {
        try {
            Path rutaProducto = rutaProductos.resolve(id);
            if (Files.exists(rutaProducto) && Files.isDirectory(rutaProducto)) {
            // Obtener el primer archivo en el directorio
                try (Stream<Path> paths = Files.list(rutaProducto)) {
                    Optional<Path> firstImage = paths
                        .filter(Files::isRegularFile)
                        //.filter(path -> path.toString().endsWith(".png")) // Filtrar por tipo de archivo si es necesario
                        .findFirst();
                    
                    if (firstImage.isPresent()) {
                        return new UrlResource(firstImage.get().toUri());
                    }
                }
            }

            // Si no hay imágenes, retornar una imagen por defecto
            Path defaultImage = rutaProductos.resolve("sinproducto.png");
            return new UrlResource(defaultImage.toUri());
        }
    
            // Resource resource = new UrlResource(file.toUri());
    
            // if (resource.exists() || resource.isReadable()) {
            //     return resource;
            // } else {
            //     file = rutaProductos.resolve("sinproducto.png");
            //     resource = new UrlResource(file.toUri());
            //     return resource;
            // }
         catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al directorio: " + e.getMessage());
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
            // Supone que `rutaProductos` ya es un Path
            Path file = rutaEmpresas.resolve(archivo + "_logo.png");
    
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
            fscaleImage(id, rutaEmpresas.toString(), ancho, alto);
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
