package bo.profesional.cesarnvf.modulos.aods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Roles;

@Mapper
public interface IRolesAod {

    @Select("select idrol, rol from roles where idrol=#{id}")
    Roles dato(Long id);

}

