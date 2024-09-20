package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Tiposbeneficios;

@Mapper
public interface ITiposbeneficiosAod {

    @Select("SELECT * FROM tiposbeneficios where idtipobeneficio=#{id} ")
    Tiposbeneficios dato(Long id);

    @Select("SELECT * FROM tiposbeneficios WHERE estado=true ORDER BY tipobeneficio ")
    List<Tiposbeneficios> listar();

}
