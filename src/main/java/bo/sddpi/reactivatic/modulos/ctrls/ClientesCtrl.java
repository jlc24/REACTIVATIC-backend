package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bo.sddpi.reactivatic.modulos.aods.IClientesAod;
import bo.sddpi.reactivatic.modulos.entidades.Clientes;

@RestController
@RequestMapping("/apirest/clientes")
public class ClientesCtrl {
    @Autowired
    private IClientesAod iClientesAod;

    //BUSCAR CLIENTES->PAGINACION->CANTIDAD
    @GetMapping
    ResponseEntity<?> buscar(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad){
        List<Clientes> clientes = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            clientes = iClientesAod.buscar(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Clientes>>(clientes, HttpStatus.OK);
    }

    //LISTA DE CLIENTES
    @GetMapping("/l")
    ResponseEntity<?> listar(){
        List<Clientes> clientes;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            clientes = iClientesAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
}
