package bo.profesional.cesarnvf.modulos.aods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Tiposextensiones;

@Mapper
public interface ITiposextensionesAod {

    @Select("SELECT idtipoextension, tipoextension FROM tiposextensiones where idtipoextension=#{id} ")
    Tiposextensiones dato(Long id);
}