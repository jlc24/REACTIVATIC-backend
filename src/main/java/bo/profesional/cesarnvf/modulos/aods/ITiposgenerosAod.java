package bo.profesional.cesarnvf.modulos.aods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Tiposgeneros;

@Mapper
public interface ITiposgenerosAod {

    @Select("SELECT idtipogenero, tipogenero FROM tiposgeneros where idtipogenero=#{id} ")
    Tiposgeneros dato(Long id);

}