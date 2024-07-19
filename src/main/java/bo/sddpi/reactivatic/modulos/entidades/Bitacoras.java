package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Bitacoras implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long idbitacora;
    private Long idusuario;
    private String bitacora;
    private LocalDate fechabitacora;
    private LocalTime horabitacora;
    private Usuarios usuario;
}