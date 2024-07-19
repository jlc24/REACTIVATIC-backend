package bo.sddpi.reactivatic.modulos.entidades;

import java.io.Serializable;

import lombok.Data;

@Data
public class Beneficiosempresas implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long idbeneficioempresa;
    private Long idbeneficio;
    private Long idempresa;

    private Beneficios beneficio;
    private Empresas empresa;

}
