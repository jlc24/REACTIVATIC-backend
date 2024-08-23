package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import bo.sddpi.reactivatic.modulos.aods.ILocalidadesAod;
import bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod;
import bo.sddpi.reactivatic.modulos.entidades.Municipios;


@RestController
@RequestMapping("/apirest/municipios")
public class MunicipiosCtrl {

    @Autowired
    IMunicipiosAod iMunicipiosAod;

    @Autowired
    ILocalidadesAod ilocalidadesAod;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Municipios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            datos = iMunicipiosAod.datos(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Municipios>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iMunicipiosAod.cantidad(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id) {
        Municipios dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iMunicipiosAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Municipios>(dato, HttpStatus.OK);
    }

    
    

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Municipios dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarmunicipio = iMunicipiosAod.verificarmunicipio(dato.getMunicipio());
        if (verificarmunicipio != null) {
            mensajes.put("mensaje", "El municipio ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                iMunicipiosAod.adicionar(dato);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Municipio agregado exitosamente en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        }
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Municipios dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iMunicipiosAod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/l")
    ResponseEntity<?> datosl() {
        List<Municipios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iMunicipiosAod.datosl();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Municipios>>(datos, HttpStatus.OK);
    }

    @PutMapping("/cambiarestado")
    ResponseEntity<?> cambiarestado(@RequestBody Municipios municipio){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iMunicipiosAod.cambiarestado(municipio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificaco el estado del rubro");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }
}