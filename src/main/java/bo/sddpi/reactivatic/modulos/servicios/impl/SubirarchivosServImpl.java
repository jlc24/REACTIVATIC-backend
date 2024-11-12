package bo.sddpi.reactivatic.modulos.servicios.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

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
    private Path rutaBeneficios;

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
        rutaBeneficios = Paths.get(rutaprincipal, "/imagenes/beneficios");

        try {
            Files.createDirectories(rutaUsuarios);
            Files.createDirectories(rutaRepresentantes);
            Files.createDirectories(rutaEmpresas);
            Files.createDirectories(rutaProductos);
            Files.createDirectories(rutaBeneficios);

            if (!os.contains("win")) {
                Files.setPosixFilePermissions(rutaUsuarios, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaRepresentantes, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaEmpresas, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaProductos, PosixFilePermissions.fromString("rwxr-xr-x"));
                Files.setPosixFilePermissions(rutaBeneficios, PosixFilePermissions.fromString("rwxr-xr-x"));
            }

        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear los directorios de archivos. Error: " + e.getMessage());
        }

        try (InputStream inputStreamLogo = new ClassPathResource("fondologo.png").getInputStream()) {
            Files.copy(inputStreamLogo, rutaProductos.resolve("productos.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo fondologo.png: " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinCarnet = new ClassPathResource("sincarnet.png").getInputStream()) {
            Files.copy(inputStreamSinCarnet, rutaRepresentantes.resolve("carnet_anverso.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sincarnet.png (anverso): " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinCarnetReverso = new ClassPathResource("sincarnet.png").getInputStream()) {
            Files.copy(inputStreamSinCarnetReverso, rutaRepresentantes.resolve("carnet_reverso.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sincarnet.png (reverso): " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinUsuario = new ClassPathResource("sinusuario.png").getInputStream()) {
            Files.copy(inputStreamSinUsuario, rutaUsuarios.resolve("sinusuario.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sinusuario.png: " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinImagen = new ClassPathResource("sinimagen.png").getInputStream()) {
            Files.copy(inputStreamSinImagen, rutaEmpresas.resolve("sinempresa.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sinimagen.png (empresa): " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinImagenBeneficio = new ClassPathResource("sinbeneficio.png").getInputStream()) {
            Files.copy(inputStreamSinImagenBeneficio, rutaEmpresas.resolve("sinformulario.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sinformulario.png (formulario): " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinImagenProducto = new ClassPathResource("sinimagen.png").getInputStream()) {
            Files.copy(inputStreamSinImagenProducto, rutaProductos.resolve("sinproducto.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sinimagen.png (producto): " + e.getMessage(), e);
        }

        try (InputStream inputStreamSinImagenBeneficio = new ClassPathResource("sinbeneficio.png").getInputStream()) {
            Files.copy(inputStreamSinImagenBeneficio, rutaBeneficios.resolve("sinbeneficio.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al copiar el archivo sinbeneficio.png (beneficio): " + e.getMessage(), e);
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
                Path directorioEmpresas = Paths.get(rutaEmpresas.toString(), id.toString());
                try {
                    Files.createDirectories(directorioEmpresas);
                    rutaDestino = directorioEmpresas.resolve(id.toString() + "_logo.png");
                } catch (Exception e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "carousel":
                Path directorioEmpresasCar = Paths.get(rutaEmpresas.toString(), id.toString() + "/carousel");
                try {
                    Files.createDirectories(directorioEmpresasCar);
                    rutaDestino = directorioEmpresasCar.resolve(archivo.getOriginalFilename());
                } catch (Exception e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "formularioa":
                Path directorioEmpresasForA = Paths.get(rutaEmpresas.toString(), id.toString() + "/formulario");
                try {
                    Files.createDirectories(directorioEmpresasForA);
                    rutaDestino = directorioEmpresasForA.resolve(id.toString() + "_formulario_a.png");
                } catch (Exception e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "formulariob":
                Path directorioEmpresasForB = Paths.get(rutaEmpresas.toString(), id.toString() + "/formulario");
                try {
                    Files.createDirectories(directorioEmpresasForB);
                    rutaDestino = directorioEmpresasForB.resolve(id.toString() + "_formulario_b.png");
                } catch (Exception e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
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
            case "convocatoria":
                Path directorioBeneficioConv = Paths.get(rutaBeneficios.toString(), id.toString());
                try {
                    Files.createDirectories(directorioBeneficioConv);
                    rutaDestino = directorioBeneficioConv.resolve(id.toString() + "_convocatoria.png");
                } catch (IOException e) {
                    throw new RuntimeException("No se pudo crear el directorio. Error: " + e.getMessage());
                }
                break;
            case "afiche":
                Path directorioBeneficioAf = Paths.get(rutaBeneficios.toString(), id.toString());
                try {
                    Files.createDirectories(directorioBeneficioAf);
                    rutaDestino = directorioBeneficioAf.resolve(id.toString() + "_afiche.png");
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
                Path rutaEmpresaEspecifica = rutaEmpresas.resolve(id.toString());
                rutaEspecifica = rutaEmpresaEspecifica.resolve(id.toString() + "_logo.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaEmpresas.resolve("sinempresa.png")));
                }
                break;
            case "carousel":
                Path rutaCarouselEspecifica = rutaEmpresas.resolve(id.toString() + "/carousel");
                if (!Files.exists(rutaCarouselEspecifica)) {
                    imagenes.add(convertToBase64(rutaEmpresas.resolve("sinempresa.png")));
                } else if (Files.exists(rutaCarouselEspecifica) && Files.isDirectory(rutaCarouselEspecifica)) {
                    try (Stream<Path> paths = Files.walk(rutaCarouselEspecifica)) {
                        List<Path> imagenesEncontradas = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                        if (imagenesEncontradas.isEmpty()) {
                            imagenes.add(convertToBase64(rutaEmpresas.resolve("sinempresa.png")));
                        }
                        imagenesEncontradas.forEach(path -> imagenes.add(convertToBase64(path)));
                    } catch (IOException e) {
                        throw new RuntimeException("Error al leer los archivos de empresa: " + e.getMessage());
                    }
                } else {
                    imagenes.add(convertToBase64(rutaEmpresas.resolve("sinempresa.png")));
                }
                break;
            case "formularioA":
                Path rutaFormularioEspecificoA = rutaEmpresas.resolve(id.toString() + "/formulario");
                rutaEspecifica = rutaFormularioEspecificoA.resolve(id.toString() + "_formulario_a.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaEmpresas.resolve("sinformulario.png")));
                }
                break;
            case "formularioB":
                Path rutaFormularioEspecificoB = rutaEmpresas.resolve(id.toString() + "/formulario");
                rutaEspecifica = rutaFormularioEspecificoB.resolve(id.toString() + "_formulario_b.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaEmpresas.resolve("sinformulario.png")));
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
                        List<Path> imagenesEncontradas = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                        if (imagenesEncontradas.isEmpty()) {
                            imagenes.add(convertToBase64(rutaProductos.resolve("sinproducto.png")));
                        }
                        imagenesEncontradas.forEach(path -> imagenes.add(convertToBase64(path)));
                    } catch (IOException e) {
                        throw new RuntimeException("Error al leer los archivos de productos: " + e.getMessage());
                    }
                } else {
                    imagenes.add(convertToBase64(rutaProductos.resolve("sinproducto.png")));
                }
                break;
            case "convocatoria":
                Path directorioBeneficioConv = rutaBeneficios.resolve(id.toString());
                rutaEspecifica = directorioBeneficioConv.resolve(id.toString() + "_convocatoria.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaBeneficios.resolve("sinbeneficio.png")));
                }
                break;
            case "afiche":
                Path directorioBeneficioAf = rutaBeneficios.resolve(id.toString());
                rutaEspecifica = directorioBeneficioAf.resolve(id.toString() + "_afiche.png");
                if (Files.exists(rutaEspecifica)) {
                    imagenes.add(convertToBase64(rutaEspecifica));
                } else {
                    imagenes.add(convertToBase64(rutaBeneficios.resolve("sinbeneficio.png")));
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
                rutaArchivo = buscarArchivoEnDirectorio(directorio, archivo);
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
                try (Stream<Path> paths = Files.list(rutaProducto)) {
                    Optional<Path> firstImage = paths
                        .filter(Files::isRegularFile)
                        .findFirst();
                    
                    if (firstImage.isPresent()) {
                        return new UrlResource(firstImage.get().toUri());
                    }
                }
            }

            Path defaultImage = rutaProductos.resolve("sinproducto.png");
            return new UrlResource(defaultImage.toUri());
        }
         catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al directorio: " + e.getMessage());
        }
    }

    @Override
    public String descargarnombreimagen(String id) {
        try {
            Path rutaProducto = rutaProductos.resolve(id);
            if (Files.exists(rutaProducto) && Files.isDirectory(rutaProducto)) {
                try (Stream<Path> paths = Files.list(rutaProducto)) {
                    Optional<Path> firstImage = paths
                        .filter(Files::isRegularFile)
                        .findFirst();
                    
                    if (firstImage.isPresent()) {
                        return firstImage.get().getFileName().toString();
                    }
                }
            }
            return "sinproducto.png";
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al directorio: " + e.getMessage());
        }
    }

    @Override
    public Resource descargarimageneproductocarrito(String id, String archivo) {
        try {
            // Supone que `rutaProductos` ya es un Path
            Path file = rutaProductos.resolve(id+'/'+archivo);
    
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
        try {
            // Supone que `rutaProductos` ya es un Path
            Path fileDirectorio = rutaEmpresas.resolve(archivo);
            Path file = fileDirectorio.resolve(archivo + "_logo.png");
    
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

    public byte[] generarCarnetPdf(Long idPersona) throws DocumentException, IOException {
        Path representanteDir = rutaRepresentantes.resolve(String.valueOf(idPersona));
        
        // Verificamos si el directorio del representante existe
        if (!Files.exists(representanteDir) || !Files.isDirectory(representanteDir)) {
            throw new IOException("El usuario no tiene registrado sus credenciales.");
        }

        Path anversoPath = representanteDir.resolve("carnet_anverso.png");
        Path reversoPath = representanteDir.resolve("carnet_reverso.png");

        // Verificamos si los archivos de imagen existen
        if (!Files.exists(anversoPath)) {
            throw new IOException("La imagen 'carnet_anverso.png' no fue encontrada");
        }

        if (!Files.exists(reversoPath)) {
            throw new IOException("La imagen 'carnet_reverso.png' no fue encontrada");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        try {
            byte[] anversoBytes = Files.readAllBytes(anversoPath);
            byte[] reversoBytes = Files.readAllBytes(reversoPath);

            Image anverso = Image.getInstance(anversoBytes);
            Image reverso = Image.getInstance(reversoBytes);

            float anchoCm = 8.5f;
            float altoCm = 5.5f;
            float puntosPorCm = 28.35f;
            float espacioEntreImagenes = altoCm * puntosPorCm;

            anverso.scaleAbsolute(anchoCm * puntosPorCm, altoCm * puntosPorCm);
            reverso.scaleAbsolute(anchoCm * puntosPorCm, altoCm * puntosPorCm);

            float posicionX = (PageSize.LETTER.getWidth() - anverso.getScaledWidth()) / 2;
            float posicionYAnverso = (PageSize.LETTER.getHeight() + espacioEntreImagenes) / 2;
            float posicionYReverso = posicionYAnverso - espacioEntreImagenes - anverso.getScaledHeight();

            anverso.setAbsolutePosition(posicionX, posicionYAnverso);
            reverso.setAbsolutePosition(posicionX, posicionYReverso);

            // Agregamos las imágenes al documento
            document.add(anverso);
            document.add(reverso);

            // Verificamos si se añadieron páginas al documento
            if (writer.getPageNumber() == 0) {
                throw new DocumentException("No se pudieron añadir imágenes al documento PDF.");
            }

        } catch (BadElementException | IOException e) {
            throw new IOException("Error al cargar las imágenes: " + e.getMessage());
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] generarFormularioPdf(Long idempresa) throws DocumentException, IOException {
        Path empresaDir = rutaEmpresas.resolve(String.valueOf(idempresa));
        
        // Verificamos si el directorio del representante existe
        if (!Files.exists(empresaDir) || !Files.isDirectory(empresaDir)) {
            throw new IOException("El usuario no tiene registrado sus credenciales.");
        }

        Path formAPath = empresaDir.resolve("formulario/" + idempresa + "_formulario_a.png");
        Path formBPath = empresaDir.resolve("formulario/" + idempresa + "_formulario_b.png");

        // Verificamos si los archivos de imagen existen
        if (!Files.exists(formAPath)) {
            throw new IOException("La imagen '" + idempresa + "_formulario_a.png' no fue encontrada");
        }

        if (!Files.exists(formBPath)) {
            throw new IOException("La imagen '" + idempresa + "_formulario_b.png' no fue encontrada");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        try {
            byte[] formABytes = Files.readAllBytes(formAPath);
            byte[] formBBytes = Files.readAllBytes(formBPath);

            Image formularioA = Image.getInstance(formABytes);
            Image formularioB = Image.getInstance(formBBytes);

            // Ajustamos cada imagen al tamaño de la página carta
            formularioA.scaleToFit(PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight());
            formularioB.scaleToFit(PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight());

            // Centramos cada imagen en la página
            formularioA.setAbsolutePosition(
                (PageSize.LETTER.getWidth() - formularioA.getScaledWidth()) / 2,
                (PageSize.LETTER.getHeight() - formularioA.getScaledHeight()) / 2
            );
            formularioB.setAbsolutePosition(
                (PageSize.LETTER.getWidth() - formularioB.getScaledWidth()) / 2,
                (PageSize.LETTER.getHeight() - formularioB.getScaledHeight()) / 2
            );

            // Agregamos la primera imagen a la primera página
            document.add(formularioA);
            document.newPage(); // Crea una nueva página para la siguiente imagen

            // Agregamos la segunda imagen a la segunda página
            document.add(formularioB);

        } catch (BadElementException | IOException e) {
            throw new IOException("Error al cargar las imágenes: " + e.getMessage());
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

}
