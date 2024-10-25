package bo.sddpi.reactivatic.modulos.ctrls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.IAsistenciasAod;
import bo.sddpi.reactivatic.modulos.aods.IAsistenciasempresasAod;
import bo.sddpi.reactivatic.modulos.aods.IBeneficiosAod;
import bo.sddpi.reactivatic.modulos.aods.IBeneficiosempresasAod;
import bo.sddpi.reactivatic.modulos.entidades.Asistencias;
import bo.sddpi.reactivatic.modulos.entidades.Asistenciasempresas;
import bo.sddpi.reactivatic.modulos.entidades.Beneficios;
import bo.sddpi.reactivatic.modulos.entidades.Beneficiosempresas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/apirest/asistencias")
public class AsistenciasCtrl {

    @Autowired
    IAsistenciasAod iAsistenciasAod;

    @Autowired
    IBeneficiosAod iBeneficiosAod;

    @Autowired
    IBeneficiosempresasAod iBeneficiosempresasAod;

    @Autowired
    IAsistenciasempresasAod iAsistenciasempresasAod;

    @GetMapping("/{id}")
    ResponseEntity<?> dato(@PathVariable Long id){
        Asistencias dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iAsistenciasAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no tiene asistencias"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Asistencias>(dato, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Asistencias asistencia, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Asistencias newasistencia = new Asistencias();
            newasistencia.setIdbeneficio(asistencia.getIdbeneficio());
            newasistencia.setDias(asistencia.getDias());
            newasistencia.setDuraciondias(asistencia.getDuraciondias());
            newasistencia.setDuracioncurso(asistencia.getDuracioncurso());
            iAsistenciasAod.adicionar(newasistencia);

            Long idasistencia = newasistencia.getIdasistencia();

            List<Beneficiosempresas> empresas = iBeneficiosempresasAod.datosl(asistencia.getIdbeneficio());
            Beneficios beneficio = iBeneficiosAod.dato(asistencia.getIdbeneficio());

            LocalDate fechainicio = beneficio.getFechainicio().toLocalDate();
            LocalDate fechafin = beneficio.getFechafin().toLocalDate();
            List<LocalDate> fechas = generarFechas(fechainicio, fechafin);
            
            for (Beneficiosempresas beneficioEmpresa : empresas) {
                for (LocalDate fecha : fechas) {
                    Asistenciasempresas asistenciaEmpresa = new Asistenciasempresas();
                    asistenciaEmpresa.setIdasistencia(idasistencia);
                    asistenciaEmpresa.setIdbeneficioempresa(beneficioEmpresa.getIdbeneficioempresa());
                    asistenciaEmpresa.setAsistencia(false);
                    asistenciaEmpresa.setFecha(fecha);

                    iAsistenciasempresasAod.adicionar(asistenciaEmpresa);
                }
            }
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    public List<LocalDate> generarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<LocalDate> fechasLaborales = new ArrayList<>();
        LocalDate fechaActual = fechaInicio;

        // Itera sobre los días entre la fecha de inicio y la fecha de fin
        while (!fechaActual.isAfter(fechaFin)) {
            // Verifica si el día actual es un día laboral (lunes a viernes)
            DayOfWeek diaSemana = fechaActual.getDayOfWeek();
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                fechasLaborales.add(fechaActual);
            }
            // Avanza al siguiente día
            fechaActual = fechaActual.plusDays(1);
        }

        return fechasLaborales;
    }
    
    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Asistencias dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iAsistenciasAod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }
    
}
